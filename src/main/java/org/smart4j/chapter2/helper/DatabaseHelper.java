package org.smart4j.chapter2.helper;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smart4j.chapter2.util.CollectionUtil;
import org.smart4j.chapter2.util.PropsUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public final class DatabaseHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseHelper.class);

    private static final ThreadLocal<Connection> CONNECTION_HOLDER;
    private static final QueryRunner QUERY_RUNNER;
    private static final BasicDataSource DATA_SOURCE;

    /**
     * 加载数据库配置
     */
    static{
        CONNECTION_HOLDER = new ThreadLocal<Connection>();
        QUERY_RUNNER = new QueryRunner();
        DATA_SOURCE = new BasicDataSource();

        Properties props = PropsUtil.loadProps("config.properties");
        String driver = props.getProperty("jdbc.driver");
        String url = props.getProperty("jdbc.url");
        String username = props.getProperty("jdbc.username");
        String password = props.getProperty("jdbc.password");

        DATA_SOURCE.setDriverClassName(driver);
        DATA_SOURCE.setUrl(url);
        DATA_SOURCE.setUsername(username);
        DATA_SOURCE.setPassword(password);
    }

    /**
     * 获取数据库连接
     * @return
     */
    public static Connection getConnection(){
        Connection conn = CONNECTION_HOLDER.get();
        if(conn == null){
            try {
                conn = DATA_SOURCE.getConnection();
            } catch (SQLException e) {
                LOGGER.error("get connection failure",e);
                throw new RuntimeException(e);
            }finally {
                CONNECTION_HOLDER.set(conn);
            }
        }
        return conn;
    }

//    /**
//     * 关闭数据库连接 采用数据库连接池已经不需要了
//     * @param
//     */
//    public static void closeConnection(){
//        Connection conn = CONNECTION_HOLDER.get();
//        if(conn != null){
//            try {
//                conn.close();
//            } catch (SQLException e) {
//                LOGGER.error("close connection failure",e);
//                throw new RuntimeException(e);
//            }finally {
//                CONNECTION_HOLDER.remove();
//            }
//        }
//    }

    /**
     * 查询实体列表
     * @param entityClass
     * @param sql
     * @param params
     * @param <T>
     * @return
     */
    public static <T> List<T> queryEntityList(Class<T> entityClass,String sql,Object... params){
        List<T> entityList;
        try{
            Connection conn = getConnection();
            entityList = QUERY_RUNNER.query(conn,sql,new BeanListHandler<T>(entityClass),params);
        }catch (SQLException e){
            LOGGER.error("query entity list failure",e);
            throw new RuntimeException(e);
        } 
        return entityList;
    }

    /**
     * 查询实体
     * @param entityClass
     * @param sql
     * @param params
     * @param <T>
     * @return
     */
    public static <T> T queryEntity(Class<T> entityClass,String sql,Object... params){
        T entity;
        try{
            Connection conn = getConnection();
            entity = QUERY_RUNNER.query(conn,sql,new BeanHandler<T>(entityClass),params);
        }catch (SQLException e){
            LOGGER.error("query entity failure",e);
            throw new RuntimeException(e);
        } 
        return entity;
    }

    /**
     * 执行复杂查询语句
     * @param sql
     * @param params
     * @return
     */
    public static List<Map<String,Object>> executeQuery(String sql, Object... params){
        List<Map<String,Object>> result;
        try{
            Connection conn = getConnection();
            result = QUERY_RUNNER.query(conn,sql,new MapListHandler(),params);
        }catch (SQLException e){
            LOGGER.error("query entity failure",e);
            throw new RuntimeException(e);
        } 
        return result;
    }

    /**
     * 执行更新语句(包括insert,update,delete)
     * @param sql
     * @param params
     * @return
     */
    public static int executeUpdate(String sql, Object... params){
        int rows = 0;
        try{
            Connection conn = getConnection();
            rows = QUERY_RUNNER.update(conn,sql,params);
        }catch (SQLException e){
            LOGGER.error("execute update failure",e);
            throw new RuntimeException(e);
        } 
        return rows;
    }

    /**
     * 插入实体
     * @param entityClass
     * @param fieldMap
     * @param <T>
     * @return
     */
    public static <T> boolean insertEntity(Class<T> entityClass, Map<String,Object> fieldMap){
        if(CollectionUtil.isEmpty(fieldMap)){
            LOGGER.error("can not insert entity：fieldMap is empty");
            return false;
        }
        String sql = "INSERT INTO "+getTableName(entityClass);
        StringBuilder columns = new StringBuilder("( ");
        StringBuilder values = new StringBuilder("( ");
        for(String fieldName: fieldMap.keySet()){
            columns.append(fieldName).append(", ");
            values.append("?, ");
        }
        columns.replace(columns.lastIndexOf(","),columns.length(),")");
        values.replace(values.lastIndexOf(","),values.length(),")");
        sql += columns+" VALUES "+values;

        Object[] params = fieldMap.values().toArray();

        return executeUpdate(sql,params) == 1;
    }

    /**
     * 更新实体
     * @param entityClass
     * @param fieldMap
     * @param id
     * @param <T>
     * @return
     */
    public static <T> boolean updateEntity(Class<T> entityClass,long id, Map<String,Object> fieldMap){
        if(CollectionUtil.isEmpty(fieldMap)){
            LOGGER.error("can not update entity：fieldMap is empty");
            return false;
        }
        String sql = "UPDATE "+getTableName(entityClass);
        StringBuilder columns = new StringBuilder();
        for(String fieldName: fieldMap.keySet()){
            columns.append(fieldName).append("=?, ");
        }
        sql += columns.substring(0,columns.lastIndexOf(","))+" WHERE id = ? ";
        List<Object> paramsList = new ArrayList<Object>();
        paramsList.addAll(fieldMap.values());
        paramsList.add(id);
        Object[] params = paramsList.toArray();

        return executeUpdate(sql,params) == 1;
    }


    /**
     * 删除实体
     * @param entityClass
     * @param id
     * @param <T>
     * @return
     */
    public static <T> boolean deleteEntity(Class<T> entityClass,long id){
        String sql = "DELETE FROM "+getTableName(entityClass);
        sql +=" WHERE id = ? ";
        return executeUpdate(sql,id) == 1;
    }

    /**
     * 获取表名称
     * @param entityClass
     * @return
     */
    private static String getTableName(Class<?> entityClass){
        return entityClass.getSimpleName();
    }

    /**
     * 执行SQL文件
     * @param fileName
     */
    public static void executeSqlFile(String fileName){
        String file = fileName;
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(file);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        try{
            String sql;
            while(null != (sql = br.readLine())){
                executeUpdate(sql);
            }
        } catch (IOException e) {
            LOGGER.error("execute sql file failure",e);
            throw new RuntimeException(e);
        }
    }
}

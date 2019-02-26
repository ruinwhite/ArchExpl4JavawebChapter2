package org.smart4j.chapter2.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 属性文件工具类
 */
public final class PropsUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(PropsUtil.class);

    public static Properties loadProps(String fileName){
        Properties props = null;
        InputStream is = null;
        try{
            is = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
            if(is == null){
                throw new FileNotFoundException(fileName+ " file is not found");
            }
            props = new Properties();
            props.load(is);
        }catch(IOException e){
            LOGGER.error("load properties file failure", e);
        }finally {
            if(is != null){
                try {
                    is.close();
                } catch (IOException e) {
                    LOGGER.error("close input stream failure", e);
                }
            }
        }
        return props;
    }

    /**
     * 获取字符串属性(默认值为空字符串)
     * @param props
     * @param key
     * @return
     */
    public static String getString(Properties props,String key){
        return getString(props, key, "");
    }

    /**
     * 获取字符串属性(默认值可以指定为defaultValue)
     * @param props
     * @param key
     * @return
     */
    public static String getString(Properties props,String key,String defaultValue){
        String stringValue = defaultValue;
        if(props.containsKey(key)){
            stringValue = props.getProperty(key);
        }
        return stringValue;
    }

    /**
     * 获取int属性(默认值为0)
     * @param props
     * @param key
     * @return
     */
    public static int getInt(Properties props,String key){
        return getInt(props, key, 0);
    }

    /**
     * 获取int属性(默认值可以指定为defaultValue)
     * @param props
     * @param key
     * @return
     */
    public static int getInt(Properties props,String key,int defaultValue){
        int intValue = defaultValue;
        if(props.containsKey(key)){
            intValue = CastUtil.castInt(props.getProperty(key));
        }
        return intValue;
    }

    /**
     * 获取boolean属性(默认值为false)
     * @param props
     * @param key
     * @return
     */
    public static boolean getBoolean(Properties props,String key){
        return getBoolean(props, key, false);
    }

    /**
     * 获取boolean属性(默认值可以指定为defaultValue)
     * @param props
     * @param key
     * @return
     */
    public static boolean getBoolean(Properties props,String key,boolean defaultValue){
        boolean booleanValue = defaultValue;
        if(props.containsKey(key)){
            booleanValue = CastUtil.castBoolean(props.getProperty(key));
        }
        return booleanValue;
    }
}

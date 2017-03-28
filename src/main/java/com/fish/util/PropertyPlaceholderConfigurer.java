package com.fish.util;

import java.io.IOException;
import java.util.Properties;

public class PropertyPlaceholderConfigurer extends
org.springframework.beans.factory.config.PropertyPlaceholderConfigurer{
    private static Properties props;

    public Properties mergeProperties() throws IOException {
     props = super.mergeProperties();
     PropertyPlaceholderConfigurer.init(props);
     return props;
    }

    public static String getProperty(String key) {
        return props.getProperty(key);
    }
    
    
    static void init(Properties props) {
        if(props!=null){
            props.putAll(props);
        }
    }
}

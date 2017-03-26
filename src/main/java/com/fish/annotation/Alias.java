/**  
 * ---------------------------------------------------------------------------
 * Copyright (c) 2016, 深圳市华康全景信息技术有限公司- All Rights Reserved.
 * Project Name:ma-common  
 * File Name:Alias.java  
 * Package Name:com.hk.ma.common.annotation
 * Author   luolong
 * Date:2016年7月27日上午10:34:31
 * ---------------------------------------------------------------------------  
*/  
  
package com.fish.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Alias {
	
	public static final String TYPE_DATE="java.util.Date";
	
	String value() default "";
	
	String type() default "";
}
  

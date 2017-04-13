package com.fish.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fish.annotation.Alias;
import com.fish.annotation.Request;
import com.fish.annotation.Type;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;


public class ClassUtil {
	
	private static Logger logger = LoggerFactory.getLogger(ClassUtil.class);
	
	/**
     * 根据字段名获取值
     * @param obj 访问对象
     * @param filedname  对象的属性
     * @return 返回对象的属性值
     * @throws Exception
     */
	public static Object getFieldValue(Object obj,String filedname) throws Exception{
		Class<?> clazz = obj.getClass();  
        PropertyDescriptor pd = new PropertyDescriptor(filedname, clazz);  
        Method getMethod = pd.getReadMethod();//获得get方法  
        return getMethod.invoke(obj);//执行get方法返回一个Object 
    }


	public static  <T> T toJavaObject(String body, Class<T> clazz){
		try {
			return JSON.toJavaObject(JSON.parseObject(body), clazz);
		} catch (Exception e) {
		}
		return null;
	}

	public static JSONObject toJSONObject(String body) {
		return JSON.parseObject(body);
	}
    
    /**
     * 
     * isFieldName(指定对象，判断该对象的属性是否存在，包含检测其父类) 
     * @param clazz clazz
     * @param filedname 对象的属性名
     * @return
     * @throws Exception  
     * Boolean 
     * @exception
     */
    public static Boolean isFieldName(Class<?> clazz ,String filedname) throws Exception{
        Field[] fields=clazz.getDeclaredFields();
        /**
         * 循环遍历所有的元素，检测有没有这个名字
         */
        boolean b=false;
        for (int i = 0; i < fields.length; i++) {
            if(fields[i].getName().equals(filedname)){
               return true;
            }
        }
        if ( clazz.getSuperclass ( ) != null){
            return ClassUtil.isFieldName(clazz.getSuperclass(), filedname);
        }
        return b;
    }
    
    /**
     * 获取所有字段
     * @param clazz
     * @return
     */
    public static List<Field> getAllFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<Field>();
		for (;clazz != Object.class ;clazz = clazz.getSuperclass()) {
			fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
		}
        return fields;
    }
    
    /**
     * 根据属性名称获取属性对象
     * @param clazz
     * @param name
     * @return
     * @throws Exception
     */
    public static Field getFieldByName(Class<?> clazz, String name){
    	Field field = null;
    	if (StringUtils.isEmpty(name))
            return field;
    	
    	for (;clazz != Object.class ;clazz = clazz.getSuperclass()) {
    		 try {
    			 field = clazz.getDeclaredField(name);
    		 } catch (Exception e) {
    		 }
    	}
        return field;
    }

    /**
     * 根据属性,给对象赋值
     * @param bean
     * @param filedType
     * @param name
     * @param value
     * @return
     */
    public static Object setObjValueByField(Object bean,Class<?> filedType ,String name, Object value) {
        Object result = null;
        try {
            String stringLetter = name.substring(0, 1).toUpperCase();
            String setName = "set" + stringLetter + name.substring(1);
            Method method = bean.getClass().getMethod(setName, filedType);
            String filedTypeName = filedType.getName();
            if (method != null) {
                if (value.getClass().getName().equals("org.json.JSONObject$Null")) {
                } else if (filedTypeName.equals(String.class.getName())) {
                	 method.invoke(bean, value.toString());
                } else if (filedTypeName.equals(int.class.getName()) || filedTypeName.equals(Integer.class.getName())) {
                    method.invoke(bean, Integer.parseInt(StringUtils.isEmpty(value.toString()) ? "0" : value.toString()));
                } else if (filedTypeName.equals(float.class.getName()) || filedTypeName.equals(Float.class.getName())) {
                    method.invoke(bean, Float.parseFloat(StringUtils.isEmpty(value.toString()) ? "0" : value.toString()));
                } else if (filedTypeName.equals(double.class.getName()) || filedTypeName.equals(Double.class.getName())) {
                    method.invoke(bean, Double.parseDouble(StringUtils.isEmpty(value.toString()) ? "0" : value.toString()));
                } else if (filedTypeName.equals(long.class.getName()) || filedTypeName.equals(Long.class.getName())) {
                    method.invoke(bean, Long.parseLong(StringUtils.isEmpty(value.toString()) ? "0" : value.toString()));
                } else if (filedTypeName.equals(Date.class.getName())) {
                    if(value != null && value instanceof Long) {
                        method.invoke(bean, new Date(((Long) value).longValue()));
                    }else if(value instanceof Date){
                        method.invoke(bean, value);
                    }
                } else if (filedTypeName.equals(BigDecimal.class.getName())) {
                    method.invoke(bean, StringUtils.isEmpty(value.toString()) ? null : BigDecimal.valueOf(Double.parseDouble(value.toString())));
                } else {
                    method.invoke(bean, value);
                }
            }
            return bean;
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return result;
    }
    
    /**
	 * 对象转map
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> objectToMap(Object obj){
		if (obj == null) {
			return null;
		}
	
		Map<String, Object> map = new HashMap<String, Object>();
		Class<?> clazz = obj.getClass();
		for (;clazz != Object.class ;clazz = clazz.getSuperclass()) {
			Field[] declaredFields = clazz.getDeclaredFields();
			for (Field field : declaredFields) {
				field.setAccessible(true);
				Object value = "";
				try {
					value = field.get(obj);
				} catch (Exception e) {
					e.printStackTrace();
				}
				map.put(field.getName(), value);
			}
		}
		return map;
	}

	
	 private static boolean isPrimitive(Class<?> type) {
        return type.isPrimitive()
             || type == String.class 
             || type == Character.class
             || type == Boolean.class
             || type == Byte.class
             || type == Short.class
             || type == Integer.class 
             || type == Long.class
             || type == Float.class 
             || type == Double.class
             || type == Object.class;
    }
	 
	 public static <T> boolean isNullObject(T t) {
	   if (null ==  t) {
	     return true;
	   }
	   return false;
	 }
	 
	 public static <T> boolean isNotNullObject(T t) {
	   if (null == t) {
	     return false;
	   }
	   return true;
	 }
	 
	 public static <T> boolean isNotEmptyArrays(T[] t) {
	   if (null != t && t.length > 0) {
	     return true;
	   }
	   return false;
	 }
	 
	 public static <T> boolean isEmptyArrays(T[] t) {
	   if (null != t && t.length > 0) {
       return false;
     }
     return true;
	 }
	 
	


    final static String isSerialVersionUID  = "serialVersionUID";

	/**
	 * 
	 * 将某一个对象复制到另一个对象相同的字段上
	 * @author luolong
	 * Date:2016年7月26日上午11:06:48
	 * @param sourceObj
	 * @param destObj
	 * @param alias 为true时，获取sourceObj中的字段的别名注解用于和destObj中的字段名对应
	 */
	public static <T> void clone(T sourceObj,T destObj,boolean alias){
		if(null== sourceObj || null == destObj){
			return;
		}
		List<Field> fields = getAllFields(destObj.getClass());
		Object value = null;
		for (Field field : fields) {
			try {

				Request request = field.getAnnotation(Request.class);
				if (request != null && !request.need()) continue;

				String filedName = field.getName();
                if(StringUtils.equals(filedName, isSerialVersionUID)) continue;

				Class<?> filedType = field.getType();

				if(alias){
					Alias al = field.getAnnotation(Alias.class);
					if(null != al){
						filedName = al.value();
                        if(StringUtils.isNotBlank(al.type())) {
                            filedType = Class.forName(al.type());
                        }
					}
				}

				if(!isFieldName(destObj.getClass(), filedName)){
					continue;
				}
				value = getFieldValue(sourceObj, field.getName());
				if(null == value){
					continue;
				}

				Type type = field.getAnnotation(Type.class);
				if (null != type) {
					String format = type.format();
					if(StringUtils.isEmpty(format)) format = "yyyy-MM-dd HH:mm:ss";
					Date date = new Date(Long.parseLong(value.toString()));
					value = new SimpleDateFormat(format).format(date);
				}
				setObjValueByField(destObj, filedType, filedName, value);
			} catch (Exception e) {
				logger.warn("字段{}不存在,e:{}",field.getName(),e);
			}
		}
	}

}
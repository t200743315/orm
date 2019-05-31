package sorm.utils;

import java.lang.reflect.Method;

/**
 * 反射
 * @author pinkman
 *
 */
@SuppressWarnings("all")
public class ReflectUtils {
	/**
	 * 调用obj对象对应属性fieldName的get方法
	 * @param fieldName 字段名
	 * @param obj 传入要操作的对象
	 * @return 得到的方法名
	 */
	public static Object invokeGet(String fieldName,Object obj) {
		try {
			Class clazz = obj.getClass();
			Method method = clazz.getMethod("get"+StringUtils.firstCharToUpperCase(fieldName), null);
			return method.invoke(obj, null);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} 
	}
	
	public static void invokeSet(Object obj,String columnName,Object columnValue) {
		try {
			Method method = obj.getClass().getDeclaredMethod("set"+
					StringUtils.firstCharToUpperCase(columnName), columnValue.getClass());//根据columnValue的类型 获取相应的对象
			method.invoke(obj, columnValue);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
}

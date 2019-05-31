package sorm.core;
/**
 * 
 * @author pinkman
 *
 */
public interface TypeConvertor {

	/**
	 * 将数据库类型转换为java的数据类型
	 * @param columType 数据库字段的类型
	 * @return Java的数据类型
	 */
	public String databaseToJavaType(String columType);
	
	/**
	 * 将java数据类型转换为数据库类型
	 * @param javaDataType
	 * @return 数据库的数据类型
	 */
	public String javaToDatabaseType(String javaDataType);
}

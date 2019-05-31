package sorm.core;


/**
 * mysql数据库类型和java数据类型的转换
 * @author pinkman
 *
 */
public class MysqlTypeConvertor implements TypeConvertor {

	@Override
	public String databaseToJavaType(String columType) {
		
		if ("varchar".equalsIgnoreCase(columType) || "char".equalsIgnoreCase(columType)) {
			return "String";
		}else if ("int".equalsIgnoreCase(columType)
				|| "tinyint".equalsIgnoreCase(columType)
				|| "integer".equalsIgnoreCase(columType)
				|| "smallint".equalsIgnoreCase(columType)) {
			return "Integer";
		}else if ("bigint".equalsIgnoreCase(columType)) {
			return "Long";
		}else if ("double".equalsIgnoreCase(columType)
				|| "float".equalsIgnoreCase(columType)) {
			return "Double";
		}else if ("clob".equalsIgnoreCase(columType)) {
			return "java.sql.Clob";
		}else if ("blob".equalsIgnoreCase(columType)) {
			return "java.sql.Blob";
		}else if ("date".equalsIgnoreCase(columType)) {
			return "java.sql.Date";
		}else if ("time".equalsIgnoreCase(columType)) {
			return "java.sql.Time";
		}else if ("timestamp".equalsIgnoreCase(columType)) {
			return "java.sql.TimeStamp";
		}
		return null;
	}

	@Override
	public String javaToDatabaseType(String javaDataType) {
		return null;
	}

}

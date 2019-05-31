package sorm.core;
/**
 * 创建Query对象的工厂类
 * @author pinkman
 *
 */

@SuppressWarnings("all")
public class QueryFactory {
	
	private static QueryFactory factory = new QueryFactory();
	
	//private static Class clazz;//配置文件配置的谁 就返回谁的实例
	private static Query prototypeObj;//原型对象
	static {
		String queryClass= DBManager.getConf().getQueryClass();
		try {
			Class clazz = Class.forName(queryClass); 
			prototypeObj = (Query) clazz.newInstance();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private QueryFactory() {//私有构造器
		
	}
	
	public static Query creatQuery() {
/*		try {
			return (Query)clazz.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} */
		try {
			return (Query)prototypeObj.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
	}
}

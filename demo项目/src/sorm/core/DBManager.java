package sorm.core;
/**
 * 根据配置信息,维持连接对象的管理(增加连接池功能)
 * @author pinkman
 *
 */

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import sorm.bean.Config;
import sorm.pool.DBConnPool;

public class DBManager {
	
	/**
	 * 配置信息
	 */
	private static Config config;
	
	/**
	 * 连接池对象
	 */
	private static DBConnPool pool = null;//再这里new对象会引起DBManager 和DBConnPool初始化的冲突
	
	static {//加载指定的资源文件
		Properties pros = new Properties();
		try {
			pros.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		config = new Config();
		config.setDriver(pros.getProperty("driver"));
		config.setUrl(pros.getProperty("url"));
		config.setUser(pros.getProperty("user"));
		config.setPwd(pros.getProperty("pwd"));
		config.setUsingDB(pros.getProperty("usingDB"));
		config.setSrcPath(pros.getProperty("srcPath"));
		config.setPoPackage(pros.getProperty("poPackage"));
		config.setQueryClass(pros.getProperty("queryClass"));
		config.setPoolMinSize(Integer.parseInt(pros.getProperty("poolMinSize")));
		config.setPoolMaxSize(Integer.parseInt(pros.getProperty("poolMaxSize")));
		System.out.println(TableContext.class);
	}
	
	public static Connection getConn() {
		if (pool == null) {
			pool = new DBConnPool();
		}
		return pool.getConnection();
	}
	
	public static Connection createConn() {
		try {
			Class.forName(config.getDriver());
			return DriverManager.getConnection(config.getUrl(),
					config.getUser(), config.getPwd());//测试阶段直接连接,后期增加连接池
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	//可变参数
	public static void close(OutputStream os, InputStream is, ResultSet rSet, PreparedStatement ps,Connection conn) {
		try {
			if (null != os) {
			os.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			if (null != is) {
			is.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			if (null != rSet) {
			rSet.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			if (null != ps) {
			ps.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	/*	try {
			if (null != connection) {
				connection.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}*/
		pool.setConnection(conn);
	}
	
	public static void close( InputStream is, ResultSet rSet, PreparedStatement ps,Connection conn) {
		try {
			if (null != is) {
			is.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			if (null != rSet) {
			rSet.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			if (null != ps) {
			ps.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		pool.setConnection(conn);
/*		try {
			if (null != connection) {
				connection.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}*/
	}
	
	public static void close(ResultSet rSet, PreparedStatement ps,Connection conn) {
		try {
			if (null != rSet) {
			rSet.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			if (null != ps) {
			ps.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		/*try {
			if (null != conn) {
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}*/
		pool.setConnection(conn);
	}
	
	public static void close(PreparedStatement ps,Connection conn) {
		try {
			if (null != ps) {
			ps.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		/*try {
			if (null != conn) {
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}*/
		pool.setConnection(conn);
	}
	
	/**
	 * @return Configuration对象
	 */
	public static Config getConf(){
		return config;
	}
}

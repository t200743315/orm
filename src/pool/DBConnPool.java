package sorm.pool;
/**
 * 数据库连接池
 * @author pinkman
 *
 */

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import sorm.core.DBManager;

public class DBConnPool {
	/**
	 * 连接池对象
	 */
	private List<Connection> pool;
	
	/**
	 * 最大连接数
	 */
	private static final int POOL_MIN_SIZE = DBManager.getConf().getPoolMinSize();
	
	/**
	 * 最小连接数
	 */
	private static final int POOL_MAX_SIZE = DBManager.getConf().getPoolMinSize();
	
	public DBConnPool() {
		initPool();
	}
	
	/**
	 * 初始化连接池,使池中的连接数达到最小值
	 */
	public void initPool() {
		if (pool==null) {
			pool = new ArrayList<>();
		}
		
		while(pool.size() <= POOL_MIN_SIZE) {
			pool.add(DBManager.createConn());
		}
	}
	
	/**
	 * 从池中取一个连接对象
	 * @return 池中最后一个对象
	 */
	public synchronized Connection getConnection() {
		int index = pool.size() - 1;
		Connection conn = pool.get(index);
		pool.remove(index);
		return conn;
	}
	
	/**
	 * 从池中放一个连接对象
	 * @param conn 需要放入的连接对象
	 */
	public synchronized void setConnection(Connection conn) {
		if (pool.size() >= POOL_MAX_SIZE) {
			try {
				if (null != conn) {
				}conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}else {
			pool.add(conn);
		}
	}
}

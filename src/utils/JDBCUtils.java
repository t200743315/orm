package sorm.utils;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 封装了JDBC查询常用的操作
 * @author pinkman
 *
 */
public class JDBCUtils {
	public static void handleParams(PreparedStatement ps,Object params[]) {
		if (params != null) {
			for(int i = 0; i<params.length; i++) {
				try {
					ps.setObject(i+1, params[i]);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
}

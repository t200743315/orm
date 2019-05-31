package sorm.core;
/**
 * 负责查询(对外提供服务的核心类)
 * @author pinkman
 *
 */

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import sorm.bean.ColumnInfo;
import sorm.bean.TableInfo;
import sorm.utils.JDBCUtils;
import sorm.utils.ReflectUtils;

@SuppressWarnings("all")
public abstract class Query implements Cloneable{
	/**
	 * 采用模板方法模式将JDBC操作封装成模板,便于重用
	 * @param sql sql语句
	 * @param params 给sql语句赋值
	 * @param clazz 操作类对应的Class对象
	 * @param back CallBack的实现类 回调函数
	 * @return 数据库查询返回的对象
	 */
	public Object executeQueryTemplate(String sql,Object[] params,Class clazz,CallBack back) {
		Connection conn = DBManager.getConn();
		List list = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql);
			//给sql传参
			JDBCUtils.handleParams(ps, params);
			System.out.println(ps);
			rs = ps.executeQuery();
			
			return back.doExecute(conn, ps, rs);
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			DBManager.close(rs,ps,conn);
		}
		
		return list;
	}
	
	/**
	 * 直接执行一个DML语句
	 * @param sql sql语句
	 * @param params sql参数
	 * @return 执行sql语句后 影响记录的行数
	 */
	public int executeDML(String sql, Object[] params) {
		Connection conn = DBManager.getConn();
		int count = 0;
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			//给sql传参
			JDBCUtils.handleParams(ps, params);
			System.out.println(ps);
			count = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			DBManager.close(ps, conn);
		}
		return count;//返回更新的行数
	}
	
	/**
	 * 将一个对象插入到数据库中
	 * 把对象中不为null的属性存入到数据库中,如果是数字则存 0
	 * @param obj 需要存储的对象
	 */
	public void insert(Object obj) {
		//insert into emp (uname,pwd,age) values(?,?,?)
		Class clazz = obj.getClass();
		List<Object> params = new ArrayList<Object>();//存储sql的参数对象
		
		TableInfo tableInfo = TableContext.poClassTableMap.get(clazz);
		StringBuilder sql = new StringBuilder("insert into "+tableInfo.getTname()+" (");//insert into emp (
		int countNotNull = 0; //计算不为null属性的数量
		
		Field[] fs = clazz.getDeclaredFields();
		for(Field f:fs) {
			String fieldName = f.getName();
			Object fieldValue = ReflectUtils.invokeGet(fieldName, obj);
			
			if (null != fieldValue) {
				countNotNull++;
				sql.append(fieldName+",");//uname,pwd,age,
				params.add(fieldValue);
			}
		}
		sql.setCharAt(sql.length()-1, ')');//insert into emp (uname,pwd,age) values (
		
		sql.append(" values (");//
		
		for(int i = 0; i<countNotNull; i++) {
			sql.append("?,");
		}
		sql.setCharAt(sql.length()-1, ')');
		
		executeDML(sql.toString(), params.toArray());
	}
	
	/**
	 * 删除clazz表示类对应的表中的记录(指定主键值id的记录)
	 * @param clazz 与表对应类的Class对象
	 * @param id 对应表主键的值
	 */
	public void delete(Class clazz,Object id) {
		// delete from emp where id=?
		//通过Map'poClassTableMap'得到表信息
		TableInfo tableInfo = TableContext.poClassTableMap.get(clazz);
		//获得主键
		ColumnInfo onlyPriKey = tableInfo.getOnlyPriKey();
		
		String sql = "delete from "+tableInfo.getTname()+" where "+onlyPriKey.getName()+"=?";
		
		executeDML(sql, new Object[] {id});
	}

	/**
	 * 删除对象在数据库中对应的记录(对象所在的类对应到表,对象主键的值对应到记录)
	 * @param obj 指定对象
	 */
	public void delete(Object obj) {
		Class clazz = obj.getClass();
		TableInfo tableInfo = TableContext.poClassTableMap.get(clazz);
		ColumnInfo onlyPriKey = tableInfo.getOnlyPriKey();
		
		//通过反射机制,调用对应属性的get方法和set方法
		Object priKeyValue = ReflectUtils.invokeGet(onlyPriKey.getName(), obj);
		delete(clazz, priKeyValue);
	}
	
	/**
	 * 更新对象对应的记录,并且只更新指定字段的值
	 * @param obj 所要更新的对象
	 * @param fieldNames 更新的属性列表
	 * @return 执行sql语句后 影响记录的行数
	 */
	public int update(Object obj,String[] fieldNames) {//update user set uname=?,pwd=?
		//update emp set uname=?,age=? where id=?
		Class clazz = obj.getClass();
		List<Object> params = new ArrayList<Object>();//存储sql的参数对象
		TableInfo tableInfo = TableContext.poClassTableMap.get(clazz);
		ColumnInfo priKey = tableInfo.getOnlyPriKey();
		StringBuilder sql = new StringBuilder("update "+tableInfo.getTname()+" set ");//insert into emp (
		
		for(String fname:fieldNames) {
			Object fvalue = ReflectUtils.invokeGet(fname, obj);
			params.add(fvalue);
			sql.append(fname+"=?,");//update emp set uname=?,age=?,
		}
		sql.setCharAt(sql.length()-1, ' ');//update emp set uname=?,age=?
		sql.append(" where "+priKey.getName()+"=?");//update emp set uname=?,age=? where id=?
		params.add(ReflectUtils.invokeGet(priKey.getName(), obj));// 获得主键的值
		
		return executeDML(sql.toString(), params.toArray());
	}
	/**
	 * 模板方法模式&回调;
	 * 查询返回多条记录,并将记录封装到clazz指定类的对象中
	 * @param sql 查询语句
	 * @param clazz 封装数据的JavaBean类的Class对象
	 * @param params sql的参数
	 * @return 查询到的结果
	 */
	public List queryRows(final String sql, Class clazz, Object[] params) {
		return (List) executeQueryTemplate(sql, params, clazz, new CallBack() {
			@Override
			public Object doExecute(Connection conn, PreparedStatement ps, ResultSet rs) {
				List list = null;
				try {
					ResultSetMetaData metaDate = rs.getMetaData();//获得表源信息
					//多行 
					while(rs.next()) {
						if (list == null) {
							list = new ArrayList<>();
						}
						Object rowObj = clazz.newInstance();//调用javabean的无参构造器
						
						//多列
						for(int i=0; i<metaDate.getColumnCount(); i++) {
							String columnName = metaDate.getColumnLabel(i+1);
							Object columnValue = rs.getObject(i+1);
							
							//通过反射获得对象的set属性 方法,每次循环将值设置进去
						ReflectUtils.invokeSet(rowObj, columnName, columnValue);
						}
						list.add(rowObj);
					}
				} catch (Exception e) {
					e.printStackTrace();
				} 
				return list;
			}
		});
	}
	
	/**
	 * 查询返回一条记录,并将记录封装到clazz指定类的对象中
	 * @param sql 查询语句
	 * @param clazz 封装数据的JavaBean类的Class对象
	 * @param params sql的参数
	 * @return 查询到的结果
	 */
	public Object queryUniqueRow(String sql, Class clazz, Object[] params) {
		List list = queryRows(sql, clazz, params);
		return (list!=null && list.size()>0)? list.get(0):null;
	}
	
	/**
	 * 模板方法模式&回调;
	 * 查询返回一个值(一行一列) 并将该值返回
	 * @param sql sql查询语句
	 * @param parmas sql的参数
	 * @return 查询到的结果
	 */
	public Object queryValue(String sql,Object[] params) {
		
		return executeQueryTemplate(sql, params, null, new CallBack() {
			
			@Override
			public Object doExecute(Connection conn, PreparedStatement ps, ResultSet rs) {
				Object value = null;
				try {
					while(rs.next()) {
					value = rs.getObject(1);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return value;
			}
		});
	}
	
	/**
	 * 根据id直接查询对象
	 * @param clazz 表对应的对象
	 * @param id 记录的id
	 * @return id对应的记录
	 */
	public Object queryById(Class clazz,Object id) {
		TableInfo tableInfo = TableContext.poClassTableMap.get(clazz);
		
		ColumnInfo onlyPriKey = tableInfo.getOnlyPriKey();
		String sql = "select * from " + tableInfo.getTname()+" where "+onlyPriKey.getName()+"=?";
		
		return queryUniqueRow(sql, clazz, new Object[] {id});
	}
	
	/**
	 * 查询返回一个数字(一行一列),并将该值返回
	 * @param sql 查询语句
	 * @param params sql参数
	 * @return 查询到的数字
	 */
	public Number queryNumber(String sql,Object[] params) {
		return (Number)queryValue(sql, params);
	}
	
	/**
	 * 分页查询
	 * @param pageNum 页数
	 * @param size 每页显示多少记录
	 * @return 查询到的对象信息
	 */
	public abstract Object queryPagenate(int pageNum,int size);
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}

package sorm.utils;
/**
 * 封装生成java文件(源代码) 常用的操作
 * @author pinkman
 *
 */

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import sorm.bean.ColumnInfo;
import sorm.bean.JavaFieldGetSet;
import sorm.bean.TableInfo;
import sorm.core.DBManager;
import sorm.core.MysqlTypeConvertor;
import sorm.core.TableContext;
import sorm.core.TypeConvertor;

@SuppressWarnings("all")
public class JavaFileUtils {
	
	/**
	 * 根据字段信息生成java属性:如 varchar username--private String username;以及相应的get和set方法源码
	 * @param column 字段信息
	 * @param convertor 类型转换器
	 * @return set或者get方法
	 */
	public static JavaFieldGetSet createFieldGetSetSRC(ColumnInfo column,TypeConvertor convertor) {
		JavaFieldGetSet jfgs = new JavaFieldGetSet();
		
		String javaFieldType = convertor.databaseToJavaType(column.getDataType());
		
		//varchar username-->private String username;
		jfgs.setFieldInfo("\tprivate "+javaFieldType+" "+column.getName()+";\n");
		
		//public int getUsername(){return username;}
		StringBuilder getSrc = new StringBuilder();
		getSrc.append("\tpublic "+javaFieldType+" get"+StringUtils.firstCharToUpperCase(column.getName())+"(){\n");
		getSrc.append("\t\treturn "+column.getName()+";\n");
		getSrc.append("\t}\n");
		jfgs.setGetInfo(getSrc.toString());
		
		//public void setUserId(int id){this.id = id;}
		StringBuilder setSrc = new StringBuilder();
		setSrc.append("\tpublic void set"+StringUtils.firstCharToUpperCase(column.getName())+"(");
		setSrc.append(javaFieldType+" "+column.getName()+"){\n");
		setSrc.append("\t\tthis."+column.getName()+" = "+column.getName()+";\n");
		setSrc.append("\t}\n");
		jfgs.setSetInfo(setSrc.toString());
		
		return jfgs;
	}
	
	/**
	 * 根据表信息生成对应的java源码
	 * @param tableInfo 数据库的表信息
	 * @param convertor 数据类型转换器
	 * @return java源代码
	 */
	public static String creatJavaSrc(TableInfo tableInfo,TypeConvertor convertor) {
		
		//获取所有字段
		Map<String, ColumnInfo> columns = tableInfo.getColumns();
		List<JavaFieldGetSet> javaFields = new ArrayList<>();
		
		for (ColumnInfo c : columns.values()) {
			javaFields.add(createFieldGetSetSRC(c, convertor));
		}
		
		StringBuilder src = new StringBuilder();
		
		//生成package代码
		src.append("package "+DBManager.getConf().getPoPackage()+";\n\n");

		//生成import代码
		src.append("import java.sql.*;\n");
		src.append("import java.util.*;\n\n\n");
		
		//生成类声明语句
		src.append("public class "+StringUtils.firstCharToUpperCase(tableInfo.getTname())+" {\n\n");
		
		//生成属性代码
		for(JavaFieldGetSet f:javaFields) {
			src.append(f.getFieldInfo());
		}
		src.append("\n\n");
		
		//生成set方法
		for(JavaFieldGetSet f:javaFields) {
			src.append(f.getSetInfo());
		}
		
		//生成get代码
		for(JavaFieldGetSet f:javaFields) {
			src.append(f.getGetInfo());
		}
		
		//类结束符
		src.append("}\n");
		
		return src.toString();
	}

	public static void creatJavaFile(TableInfo tableInfo,TypeConvertor convertor) {
		String src = creatJavaSrc(tableInfo, convertor);
		
		String srcPath = DBManager.getConf().getSrcPath()+"\\";
		//D\:\\workspace\\SORM\\src 需要连个\\ -->转义时要输入 \\\\
		//在正则表达式中，点号表示所有字符的意思，所以需要用\来转义 所以要先\\->java中的\ 在\.->正则中的.
		String poPath = DBManager.getConf().getPoPackage().replaceAll("\\.", "\\\\");
		
		//先创建文件夹 路径为 工程路径+包路径
		File file = new File(srcPath+poPath);
		
		if (!file.exists()) {
			file.mkdirs();
		}
		BufferedWriter bw = null;
		
		try {
			//在文件夹下面创建 文件java文件
			bw = new BufferedWriter(new FileWriter(file.getAbsoluteFile()+"\\"+StringUtils.firstCharToUpperCase(tableInfo.getTname())+".java"));
			bw.write(src);
			System.out.println("建立表"+tableInfo.getTname()+"对应的java类:"+StringUtils.firstCharToUpperCase(tableInfo.getTname()+".java"));
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				if (null != bw) {
					bw.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	//测试.
	public static void main(String[] args) {
//		ColumnInfo columnInfo = new ColumnInfo("id", "varchar", 0);
//		JavaFieldGetSet f = createFieldGetSetSRC(columnInfo,new MysqlTypeConvertor());
//		System.out.println(f);
		
		//移到TableContext下
//		Map<String, TableInfo> map = TableContext.tables;
//		//TableInfo tableInfo = map.get("emp");
//		for (TableInfo tableInfo : map.values()) {
//			creatJavaFile(tableInfo, new MysqlTypeConvertor());
//		}
		
	}
}

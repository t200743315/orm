package sorm.utils;

/**
 * 封装了String查询常用的操作
 * @author pinkman
 *
 */
public class StringUtils {
	/**
	 * 将目标字符串的首字母变为大写
	 * @param str 目标字符串
	 * @return 首字母是大写的字符串
	 */
	public static String firstCharToUpperCase(String str) {
		//abcd-->Abcd
		//abcd-->ABCD-->Abcd
		return str.toUpperCase().substring(0, 1) + str.substring(1);
	}
}

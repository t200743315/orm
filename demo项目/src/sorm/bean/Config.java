package sorm.bean;
/**
 * 管理配置信息
 * @author pinkman
 *
 */
public class Config {
	/**
	 * 启动类
	 */
	private String driver;
	/**
	 * jdbc的url
	 */
	private String	url;
	/**
	 * 数据库的user
	 */
	private String	user;
	/**
	 * 数据库的pwd
	 */
	private String	pwd;
	/**
	 * 正在使用的数据库
	 */
	private String	usingDB;
	/**
	 * 源码路径
	 */
	private String	srcPath;	
	/**
	 * 扫描生成java类的包(po的意思是:Persistence object持久化对象
	 */
	private String	poPackage;
	
	/**
	 * 查询的是哪一个类
	 */
	private String queryClass;
	
	/**
	 * 连接池最小大小
	 */
	private int poolMinSize;
	
	/**
	 * 连接池最大大小
	 */
	private int poolMaxSize;
	
	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getUsingDB() {
		return usingDB;
	}

	public void setUsingDB(String usingDB) {
		this.usingDB = usingDB;
	}

	public String getSrcPath() {
		return srcPath;
	}

	public void setSrcPath(String srcPath) {
		this.srcPath = srcPath;
	}

	public String getPoPackage() {
		return poPackage;
	}

	public void setPoPackage(String poPackage) {
		this.poPackage = poPackage;
	}
	
	
	public String getQueryClass() {
		return queryClass;
	}

	public void setQueryClass(String queryClass) {
		this.queryClass = queryClass;
	}

	

	public int getPoolMinSize() {
		return poolMinSize;
	}

	public void setPoolMinSize(int poolMinSize) {
		this.poolMinSize = poolMinSize;
	}

	public int getPoolMaxSize() {
		return poolMaxSize;
	}

	public void setPoolMaxSize(int poolMaxSize) {
		this.poolMaxSize = poolMaxSize;
	}

	

	public Config(String driver, String url, String user, String pwd, String usingDB, String srcPath, String poPackage,
			String queryClass, int poolMinSize, int poolMaxSize) {
		super();
		this.driver = driver;
		this.url = url;
		this.user = user;
		this.pwd = pwd;
		this.usingDB = usingDB;
		this.srcPath = srcPath;
		this.poPackage = poPackage;
		this.queryClass = queryClass;
		this.poolMinSize = poolMinSize;
		this.poolMaxSize = poolMaxSize;
	}

	public Config() {
	}
	
	
}

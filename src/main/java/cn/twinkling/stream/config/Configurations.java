package cn.twinkling.stream.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * read the configurations from file `config.properties`.
 */
public class Configurations {
	static final String CONFIG_FILE = "stream-config.properties";
	private static Properties properties = null;
	private static final String REPOSITORY = System.getProperty("java.io.tmpdir",
			File.separator + "tmp" + File.separator + "upload-repository");

	static {
		new Configurations();
	}

	private Configurations() {
		init();
		System.out.println("[NOTICE] File Repository Path ≥≥≥ " + getFileRepository());
	}

	void init() {
		try {
			ClassLoader loader = Configurations.class.getClassLoader();
			InputStream in = loader.getResourceAsStream(CONFIG_FILE);
			properties = new Properties();
			properties.load(in);
		} catch (IOException e) {
			System.err.println("reading `" + CONFIG_FILE + "` error!" + e);
		}
	}

	public static String getConfig(String key) {
		return getConfig(key, null);
	}

	public static String getConfig(String key, String defaultValue) {
		return properties.getProperty(key, defaultValue);
	}

	public static int getConfig(String key, int defaultValue) {
		String val = getConfig(key);
		int setting = 0;
		try {
			setting = Integer.parseInt(val);
		} catch (NumberFormatException e) {
			setting = defaultValue;
		}
		return setting;
	}

	public static String getFileRepository() {
		String val = getConfig("STREAM_FILE_REPOSITORY");
		if (val == null || val.isEmpty())
			val = REPOSITORY;
		isexitsPath(val);
		return val;
	}

	public static boolean isexitsPath(String path) {
		
		String[] paths = path.split(File.separator);
		StringBuffer fullPath = new StringBuffer();
		for (int i = 0; i < paths.length; i++) {
			fullPath.append(paths[i]).append(File.separator);
			File file = new File(fullPath.toString());
			if (!file.exists()) {
				file.mkdir();
				System.out.println("创建目录为：" + fullPath.toString());
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		File file = new File(fullPath.toString());// 目录全路径
		if (!file.exists()) {
			return true;
		} else {
			return false;
		}
	}

	public static String getUserFileRepository(String userId) {
		String val = getConfig("STREAM_FILE_REPOSITORY");
		if (val == null || val.isEmpty())
			val = REPOSITORY;
		val = val + File.separator + userId;
		isexitsPath(val);
		return val;
	}

	public static String getCrossServer() {
		return getConfig("STREAM_CROSS_SERVER");
	}

	public static String getCrossOrigins() {
		return getConfig("STREAM_CROSS_ORIGIN");
	}

	public static boolean getBoolean(String key) {
		return Boolean.parseBoolean(getConfig(key));
	}

	public static boolean isDeleteFinished() {
		return getBoolean("STREAM_DELETE_FINISH");
	}

	public static boolean isCrossed() {
		return getBoolean("STREAM_IS_CROSS");
	}
}

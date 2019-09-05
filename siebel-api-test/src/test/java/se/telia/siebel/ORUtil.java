package se.telia.siebel;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ORUtil {
	private final static Properties CONFIG_FILE = loadPropertiesFile("./src/test/resources/testrunner/Config.properties");

	public static String getConfigValue(String key) throws IOException {
		String configValue = CONFIG_FILE.getProperty(key);
		return configValue;
	}
	
	private static Properties loadPropertiesFile(String strFilePath) {
		
		Properties prop = new Properties();
		try {
			FileInputStream fis;
			fis = new FileInputStream(strFilePath);
				prop.load(fis);
				fis.close();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return prop;
	}
	
	}

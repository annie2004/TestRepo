package intelliment.util;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GettersConfigProperties {

	private static final Logger log = LoggerFactory
			.getLogger(GettersConfigProperties.class);

	public static String getPropertyConfig(String key) {
		ResourceBundle properties = ResourceBundle.getBundle("configIntelli");
		String property = null;
		try {
			property = (String) properties.getObject(key);
		} catch (MissingResourceException e) {
			log.error("CAN`T ACCESS TO FILE PROPERTIES"
					+ e.getCause());
			log.error(e.getMessage());
		}
		return property;
	}
	
	public static String getPropertyTest(String key) {
		ResourceBundle properties = ResourceBundle.getBundle("testConfigIntelli");
		String property = null;
		try {
			property = (String) properties.getObject(key);
		} catch (MissingResourceException e) {
			log.error("CAN`T ACCESS TO FILE PROPERTIES"
					+ e.getCause());
			log.error(e.getMessage());
		}
		return property;
	}

}

package tr.com.aliok.osmani.annotator.commons;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author Ali Ok (ali.ok@apache.org)
 */
public class AppProperties {
    private static Properties properties;

    public static String bookSourceFolder() {
        return getString("app.data.folder.book.source");
    }

    public static String bookPagesFolder() {
        return getString("app.data.folder.book.pages");
    }

    public static String bookAnnotationFolder() {
            return getString("app.data.folder.book.annotation");
        }

    private static String getString(String key) {
        return (String) getObject(key);
    }

    private static Object getObject(String key) {
        if (properties == null)
            loadProperties();

        final Object value = properties.get(key);
        if (value == null)
            throw new RuntimeException("Cannot find key in properties file! Key : " + key + " properties file: " + properties.toString());
        return value;
    }

    private static void loadProperties() {
        final ClassLoader classLoader = AppProperties.class.getClassLoader();
        final InputStream stream = classLoader.getResourceAsStream("osmani.annotator.apps.properties");
        final Properties props = new Properties();
        try {
            props.load(stream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        AppProperties.properties = props;
    }
}

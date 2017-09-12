package io.lance.web.module.common.upload.config;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;

/**
 * read the configurations from file `config.properties`.
 */
public class Configurations {
    private static final Logger logger = LogManager.getLogger(Configurations.class);
    static final String CONFIG_FILE = "stream-config.properties";
    private static Properties properties = null;
    private static final String REPOSITORY = System.getProperty("java.io.tmpdir");

    static {
        new Configurations();
    }

    private Configurations() {
        init();
        logger.info("[NOTICE] File Repository Path ≥≥≥ {}", getFileRepository());
    }

    void init() {
        try {
            ClassLoader loader = Configurations.class.getClassLoader();
            InputStream in = loader.getResourceAsStream(CONFIG_FILE);
            properties = new Properties();
            properties.load(in);
        } catch (IOException e) {
            logger.error("reading `" + CONFIG_FILE + "` error!" + e);
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
        StringBuilder path = new StringBuilder(200);
     /*   Date date = new Date(System.currentTimeMillis());
        String year = DateFormatUtils.format(date, "yyyy");
        String month = DateFormatUtils.format(date, "MM");
        String day = DateFormatUtils.format(date, "dd");*/

        path.append(REPOSITORY);
     /*   path.append(File.separator);
        path.append(year);
        path.append(File.separator);
        path.append(month);
        path.append(File.separator);
        path.append(day);*/

        return path.toString();
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

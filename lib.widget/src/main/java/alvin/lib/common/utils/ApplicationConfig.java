package alvin.lib.common.utils;

import android.content.Context;
import android.util.AndroidRuntimeException;

import com.google.common.base.Strings;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class ApplicationConfig {
    private static ApplicationConfig instance;

    private final Properties properties = new Properties();

    private ApplicationConfig(Context context) throws IOException {
        this(context, "application.properties");
    }

    private ApplicationConfig(Context context, String filename) throws IOException {
        try (InputStream in = context.getAssets().open(filename)) {
            properties.load(in);
        }
    }

    public int getAsInt(String key, int defaultValue) {
        String property = properties.getProperty(key);
        return Strings.isNullOrEmpty(property) ? defaultValue : Integer.parseInt(property);
    }

    public long getAsLong(String key, long defaultValue) {
        String property = properties.getProperty(key);
        return Strings.isNullOrEmpty(property) ? defaultValue : Long.parseLong(property);
    }

    public String get(String key, String defaultValue) {
        String property = properties.getProperty(key);
        return Strings.isNullOrEmpty(property) ? defaultValue : property;
    }

    public String get(String key) {
        return get(key, null);
    }

    public static void initialize(Context context) {
        try {
            instance = new ApplicationConfig(context);
        } catch (IOException e) {
            throw new AndroidRuntimeException(e);
        }
    }

    public static void initialize(Context context, String filename) throws IOException {
        instance = new ApplicationConfig(context, filename);
    }

    public static ApplicationConfig getInstance() {
        if (instance == null) {
            throw new NullPointerException("ApplicationConfig must be initialize first");
        }
        return instance;
    }
}

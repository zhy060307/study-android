package alvin.lib.common.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;

import com.facebook.stetho.Stetho;

public final class Applications {

    private Applications() {
    }

    public static boolean isDebugging(Context context) {
        final ApplicationInfo info = context.getApplicationInfo();
        return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
    }

    public static void startStethoIfDebugging(Context context) {
        if (isDebugging(context)) {
            final Stetho.Initializer initializer = Stetho.newInitializerBuilder(context)
                    .enableDumpapp(Stetho.defaultDumperPluginsProvider(context))
                    .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(context))
                    .build();
            Stetho.initialize(initializer);
        }
    }
}

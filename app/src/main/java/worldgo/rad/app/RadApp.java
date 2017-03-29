package worldgo.rad.app;

import android.content.Context;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import java.util.logging.Level;

import ricky.oknet.OkGo;
import worldgo.common.viewmodel.app.BaseApplication;

/**
 * @author ricky.yao on 2017/3/22.
 */

public class RadApp extends BaseApplication {
    private RefWatcher refWatcher;

    public static RefWatcher getRefWatcher(Context context) {
        RadApp application = (RadApp) context.getApplicationContext();
        return application.refWatcher;
    }

    @Override
    public void onMainProcessCreate() {
        refWatcher = LeakCanary.install(this);
        //net
        OkGo.getInstance()
                .baseUrl("http://gank.io/api/data/")
                .debug("rad", Level.INFO, isDebug());

        if (isDebug()) {
            //chrome debug [chrome://inspect/#devices]
            Stetho.initialize(
                    Stetho.newInitializerBuilder(this)
                            .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                            .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                            .build());
            OkGo.getInstance().getOkHttpClientBuilder().addNetworkInterceptor(new StethoInterceptor());
        }
    }

    @Override
    public boolean isDebug() {
        return true;
    }
}

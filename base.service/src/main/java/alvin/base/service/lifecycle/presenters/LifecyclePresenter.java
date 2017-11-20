package alvin.base.service.lifecycle.presenters;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.common.base.Strings;

import java.util.ArrayDeque;
import java.util.Deque;

import javax.inject.Inject;

import alvin.base.service.lifecycle.LifecycleContracts;
import alvin.base.service.lifecycle.broadcasts.LifecycleBroadcasts;
import alvin.base.service.lifecycle.services.LifecycleService;
import alvin.lib.mvp.PresenterAdapter;

public class LifecyclePresenter extends PresenterAdapter<LifecycleContracts.View>
        implements LifecycleContracts.Presenter {

    private static final String TAG = LifecyclePresenter.class.getSimpleName();

    private BroadcastReceiver receiver;

    private Deque<ServiceConnection> connStack = new ArrayDeque<>();
    private int startedCount;

    @Inject
    LifecyclePresenter(@NonNull LifecycleContracts.View view) {
        super(view);
    }

    @Override
    public void registerReceiver(Context context) {
        if (receiver == null) {
            receiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    final String action = intent.getAction();
                    if (!Strings.isNullOrEmpty(action)) {
                        switch (action) {
                        case LifecycleBroadcasts.ACTION_SERVICE_CREATED:
                            withView(LifecycleContracts.View::serviceCreated);
                            break;
                        case LifecycleBroadcasts.ACTION_SERVICE_DESTROYED:
                            withView(LifecycleContracts.View::serviceDestroyed);
                            break;
                        default:
                            break;
                        }
                    }
                }
            };

            IntentFilter filter = new IntentFilter();
            filter.addAction(LifecycleBroadcasts.ACTION_SERVICE_CREATED);
            filter.addAction(LifecycleBroadcasts.ACTION_SERVICE_DESTROYED);
            context.registerReceiver(receiver, filter);
        }
    }

    @Override
    public void unregisterReceiver(Context context) {
        if (receiver != null) {
            context.unregisterReceiver(receiver);
            receiver = null;
        }
    }

    @Override
    public void startService(Context context) {
        final Intent intent = new Intent(context, LifecycleService.class)
                .putExtra(LifecycleService.EXTRA_ARGUMENTS_MODE, Service.START_REDELIVER_INTENT);

        // Start the service and pass the arguments from intent object.
        // Service can be start many times, but Service#onCreate method only be called one time,
        // and Service#onStartCommand method should be called many times.
        // Started service can be stoped by Context#stopService.
        context.startService(intent);
        startedCount = 1;

        refreshStartCount();
    }

    @Override
    public void bindService(Context context) {
        ServiceConnection conn = new ServiceConnection() {

            /**
             * Callback when service is connected, the {@link IBinder} instance will be passed,
             *
             * @see LifecycleService#onBind(Intent)
             */
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder binder) {
                Log.i(TAG, "The service was connected");
            }

            /**
             * Callback only service will be killed unexpected.
             */
            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                Log.i(TAG, "The service was accidentally disconnected");
            }
        };

        final Intent intent = new Intent(context, LifecycleService.class);

        // Bind service, the Service#onBind method will be called one time, and Reference Counter
        // should be increase
        context.bindService(intent, conn, Context.BIND_AUTO_CREATE);
        connStack.push(conn);

        refreshStartCount();
    }

    @Override
    public void unbindService(Context context) {
        if (!connStack.isEmpty()) {
            ServiceConnection conn = connStack.pop();

            // Unbind service, the Service#onUnbind method will be called one time, and Reference Counter
            // should be decrease
            context.unbindService(conn);
        }

        refreshStartCount();
    }

    @Override
    public void stopService(Context context) {
        // Stop service, if Reference Counter is zero, the service should been destroyed.
        context.stopService(new Intent(context, LifecycleService.class));

        startedCount = 0;
        refreshStartCount();
    }

    private void refreshStartCount() {
        withView(view -> {
            int count = startedCount + connStack.size();
            view.showStartCount(count);
        });
    }
}

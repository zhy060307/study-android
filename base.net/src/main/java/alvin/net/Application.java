package alvin.net;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.os.Build;
import android.widget.Toast;

import alvin.net.status.network.NetworkCallback;
import alvin.net.status.receivers.NetStatusBroadcastReceiver;

public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();

        checkNetworkStatus();

        NetworkCallback callback = new NetworkCallback(this);
        callback.registerNetworkCallback(new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(Network network) {
                super.onAvailable(network);
                Toast.makeText(Application.this, R.string.string_network_available, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onLost(Network network) {
                super.onLost(network);
                Toast.makeText(Application.this, R.string.string_network_lost, Toast.LENGTH_LONG).show();
            }
        });
    }


    @SuppressLint({"NewApi", "ObsoleteSdkInt"})
    @TargetApi(Build.VERSION_CODES.N)
    public void checkNetworkStatus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
            this.registerReceiver(new NetStatusBroadcastReceiver(), filter);
        }
    }
}
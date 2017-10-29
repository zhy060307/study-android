package alvin.net;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import alvin.net.http.HttpMainActivity;
import alvin.net.socket.SocketMainActivity;
import alvin.net.status.StatusMainActivity;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
    }

    @OnClick({
            R.id.btn_network_status,
            R.id.btn_http,
            R.id.btn_socket
    })
    public void onButtonsClicked(Button b) {
        Intent intent = null;
        switch (b.getId()) {
        case R.id.btn_network_status:
            intent = new Intent(this, StatusMainActivity.class);
            break;
        case R.id.btn_http:
            intent = new Intent(this, HttpMainActivity.class);
            break;
        case R.id.btn_socket:
            intent = new Intent(this, SocketMainActivity.class);
            break;
        }
        if (intent != null) {
            startActivity(intent);
        }
    }
}

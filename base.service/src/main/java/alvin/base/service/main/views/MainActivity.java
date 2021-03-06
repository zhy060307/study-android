package alvin.base.service.main.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import alvin.base.service.R;
import alvin.base.service.bind.views.BindActivity;
import alvin.base.service.foreground.views.ForegroundActivity;
import alvin.base.service.intent.views.IntentActivity;
import alvin.base.service.lifecycle.views.LifecycleActivity;
import alvin.base.service.messenger.views.MessengerActivity;
import alvin.base.service.remote.views.RemoteActivity;
import alvin.base.service.working.views.WorkingActivity;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
    }

    @OnClick({
            R.id.btn_basic_service,
            R.id.btn_working_service,
            R.id.btn_bind_service,
            R.id.btn_intent_service,
            R.id.btn_remote_service,
            R.id.btn_foreground_service,
            R.id.btn_messenger_service
    })
    public void onButtonsClick(Button b) {
        Intent intent;

        switch (b.getId()) {
        case R.id.btn_basic_service:
            intent = new Intent(this, LifecycleActivity.class);
            break;
        case R.id.btn_working_service:
            intent = new Intent(this, WorkingActivity.class);
            break;
        case R.id.btn_bind_service:
            intent = new Intent(this, BindActivity.class);
            break;
        case R.id.btn_intent_service:
            intent = new Intent(this, IntentActivity.class);
            break;
        case R.id.btn_remote_service:
            intent = new Intent(this, RemoteActivity.class);
            break;
        case R.id.btn_foreground_service:
            intent = new Intent(this, ForegroundActivity.class);
            break;
        case R.id.btn_messenger_service:
            intent = new Intent(this, MessengerActivity.class);
            break;
        default:
            intent = null;
        }

        if (intent != null) {
            startActivity(intent);
        }
    }
}

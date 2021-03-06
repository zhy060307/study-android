package alvin.base.net.http.views;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import alvin.base.net.R;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HttpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http);

        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_use_sync_task, R.id.btn_use_rx})
    public void onButtonsClick(Button b) {
        Intent intent;

        switch (b.getId()) {
        case R.id.btn_use_sync_task:
            intent = new Intent(this, HttpWithTaskActivity.class);
            break;
        case R.id.btn_use_rx:
            intent = new Intent(this, HttpWithRxActivity.class);
            break;
        default:
            intent = null;
            break;
        }

        if (intent != null) {
            startActivity(intent);
        }
    }
}

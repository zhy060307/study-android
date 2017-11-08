package alvin.base.mvp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import alvin.base.mvp.basic.views.BasicMainActivity;
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
            R.id.btn_basic
    })
    public void onButtonsClick(Button b) {
        Intent intent = null;

        switch (b.getId()) {
        case R.id.btn_basic:
            intent = new Intent(this, BasicMainActivity.class);
            break;
        }

        if (intent != null) {
            startActivity(intent);
        }
    }
}

package alvin.ui.list.list.views

import alvin.ui.list.R
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import butterknife.ButterKnife
import butterknife.OnClick

class ListMainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_main)

        ButterKnife.bind(this)
    }

    @OnClick(R.id.btn_list_view, R.id.btn_recycler_view)
    fun onButtonsClick(b: Button) {

        val intent = when (b.id) {
            R.id.btn_list_view -> Intent(this, ListByListViewActivity::class.java)
            R.id.btn_recycler_view -> Intent(this, ListByRecyclerViewActivity::class.java)
            else -> null
        }

        if (intent != null) {
            startActivity(intent)
        }
    }
}
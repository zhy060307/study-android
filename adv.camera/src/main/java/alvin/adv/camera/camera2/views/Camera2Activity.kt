package alvin.adv.camera.camera2.views

import alvin.adv.camera.R
import alvin.adv.camera.camera2.Camera2Contracts
import alvin.lib.mvp.views.AppCompatActivityView
import android.os.Bundle

class Camera2Activity :
        AppCompatActivityView<Camera2Contracts.Presenter>(), Camera2Contracts.View {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.camera2_activity)
    }
}

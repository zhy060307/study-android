package alvin.lib.mvp.contracts.adapters;


import javax.inject.Inject;

import alvin.lib.mvp.contracts.IPresenter;
import alvin.lib.mvp.contracts.IView;
import androidx.annotation.CallSuper;
import dagger.android.support.DaggerAppCompatDialogFragment;

public abstract class DialogFragmentAdapter<Presenter extends IPresenter>
        extends DaggerAppCompatDialogFragment implements IView {

    @Inject protected Presenter presenter;

    @CallSuper
    @Override
    public void onDestroyView() {
        presenter.onDestroy();
        super.onDestroyView();
    }
}

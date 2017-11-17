package alvin.base.mvp.scope.subcomponent.views;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import javax.inject.Inject;

import alvin.base.mvp.R;
import alvin.base.mvp.scope.subcomponent.DaggerSubcomponentComponent;
import alvin.base.mvp.scope.subcomponent.SubcomponentComponent;
import alvin.base.mvp.scope.subcomponent.SubcomponentContracts;
import alvin.base.mvp.scope.subcomponent.SubcomponentModule;
import alvin.base.mvp.scope.subcomponent.fragment.views.SubcomponentFragment;
import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SubcomponentActivity extends AppCompatActivity implements SubcomponentContracts.View {

    @BindView(R.id.tv_activity_scope)
    TextView tvSingletonService;

    @BindColor(R.color.color_blue)
    int colorBlue;

    @BindColor(R.color.color_green)
    int colorGreen;

    @Inject
    SubcomponentContracts.Presenter presenter;

    private SubcomponentComponent component;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        component = DaggerSubcomponentComponent.builder()
                .subcomponentModule(new SubcomponentModule(this))
                .build();
        component.inject(this);

        setContentView(R.layout.scope_activity_common);
        ButterKnife.bind(this);

        bindFragments();
    }

    private void bindFragments() {
        final FragmentManager fragmentManager = getFragmentManager();
        final FragmentTransaction transaction = fragmentManager.beginTransaction();

        SubcomponentFragment fragment = new SubcomponentFragment();
        fragment.setBackground(colorBlue);
        transaction.replace(R.id.frg_scope_1, fragment);

        fragment = new SubcomponentFragment();
        fragment.setBackground(colorGreen);
        transaction.replace(R.id.frg_scope_2, fragment);

        transaction.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.onStart();
    }

    @Override
    public Context context() {
        return this;
    }

    public SubcomponentComponent component() {
        return this.component;
    }

    @Override
    public void showActivityScopeService(String name) {
        tvSingletonService.setText(name);
    }
}

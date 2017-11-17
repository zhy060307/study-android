package alvin.base.mvp.scope.dependency.presenters;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import alvin.base.mvp.scope.dependency.DependencyContracts;
import alvin.base.mvp.scope.domain.service.ActivityScopeService;
import alvin.base.mvp.utils.ObjectNames;
import alvin.lib.mvp.PresenterAdapter;

public class DependencyPresenter extends PresenterAdapter<DependencyContracts.View>
        implements DependencyContracts.Presenter {

    private final ActivityScopeService activityScopeService;

    @Inject
    DependencyPresenter(@NonNull DependencyContracts.View view,
                        @NonNull ActivityScopeService activityScopeService) {
        super(view);
        this.activityScopeService = activityScopeService;
    }

    @Override
    public void onStart() {
        super.onStart();

        withView(view -> view.showActivityScopeService(ObjectNames.simpleName(activityScopeService)));
    }
}

package alvin.base.dagger.scope.dependency.presenters;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import alvin.base.dagger.scope.dependency.DependencyContracts;
import alvin.base.dagger.scope.domain.service.ActivityScopeService;
import alvin.base.dagger.utils.ObjectNames;
import alvin.lib.mvp.contracts.adapters.PresenterAdapter;

public class DependencyPresenter
        extends PresenterAdapter<DependencyContracts.View>
        implements DependencyContracts.Presenter {

    private final ActivityScopeService activityScopeService;

    @Inject
    DependencyPresenter(@NonNull DependencyContracts.View view,
                        @NonNull ActivityScopeService activityScopeService) {
        super(view);
        this.activityScopeService = activityScopeService;
    }

    @Override
    public void serviceName() {
        with(view -> view.showActivityScopeService(ObjectNames.simpleName(activityScopeService)));
    }
}

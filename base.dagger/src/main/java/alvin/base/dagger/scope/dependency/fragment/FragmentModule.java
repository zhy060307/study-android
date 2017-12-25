package alvin.base.dagger.scope.dependency.fragment;

import alvin.base.dagger.scope.Scopes;
import alvin.base.dagger.scope.dependency.fragment.presenters.FragmentPresenter;
import alvin.base.dagger.scope.dependency.fragment.views.DependencyFragment;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module(includes = {FragmentModule.BindModule.class})
public class FragmentModule {

    private final DependencyFragment fragment;

    public FragmentModule(DependencyFragment fragment) {
        this.fragment = fragment;
    }

    @Provides
    @Scopes.Fragment
    public FragmentContracts.View view() {
        return fragment;
    }

    @Module
    public interface BindModule {
        @Binds
        @Scopes.Fragment
        FragmentContracts.Presenter presenter(FragmentPresenter presenter);
    }
}

package alvin.base.mvp.basic;

import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.FlowManager;

import alvin.base.mvp.basic.presenters.BasicPresenter;
import alvin.base.mvp.common.Contract;
import alvin.base.mvp.common.db.MessageDatabase;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;


@Module(includes = {BasicModule.BindingModule.class})
public class BasicModule {
    private final Contract.View view;

    public BasicModule(Contract.View view) {
        this.view = view;
    }

    @Provides
    Contract.View view() {
        return view;
    }

    @Provides
    DatabaseDefinition databaseDefinition() {
        return FlowManager.getDatabase(MessageDatabase.class);
    }

    @Module
    public interface BindingModule {

        @Binds
        Contract.Presenter bindsPresenter(BasicPresenter presenter);
    }
}
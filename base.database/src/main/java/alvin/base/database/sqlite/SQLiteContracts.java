package alvin.base.database.sqlite;

import java.util.List;

import alvin.base.database.common.domain.models.Gender;
import alvin.base.database.common.domain.models.IPerson;
import alvin.lib.mvp.contracts.IPresenter;
import alvin.lib.mvp.contracts.IView;
import androidx.annotation.NonNull;

public interface SQLiteContracts {

    interface View extends IView {
        void onPersonGot(List<IPerson> persons);

        void onPersonCreate(IPerson person);

        void onPersonUpdate(IPerson person);

        void onPersonDelete(IPerson person);
    }

    interface Presenter extends IPresenter {
        void savePerson(@NonNull IPerson person);

        void getPersons(@NonNull Gender gender);

        void updatePerson(@NonNull IPerson person);

        void deletePerson(@NonNull IPerson person);
    }
}

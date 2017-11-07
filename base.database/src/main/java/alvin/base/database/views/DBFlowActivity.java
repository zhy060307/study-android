package alvin.base.database.views;

import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.FlowManager;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import alvin.base.database.R;
import alvin.base.database.dbflow.FlowDatabase;
import alvin.base.database.dbflow.models.Person;
import alvin.base.database.dbflow.repositories.PersonDBFlowRepository;
import alvin.base.database.models.Gender;
import alvin.base.database.models.IPerson;


public class DBFlowActivity extends FrameActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_dbflow;
    }

    @Override
    protected int getTitleId() {
        return R.string.title_dbflow;
    }

    @Override
    public void savePerson(String name, Gender gender, LocalDate birthday) {
        Person person = new Person(name, gender, birthday);
        DatabaseDefinition database = FlowManager.getDatabase(FlowDatabase.class);
        PersonDBFlowRepository repository = new PersonDBFlowRepository(database);
        repository.save(person);
    }

    @Override
    public List<IPerson> getPersons(Gender gender) {
        DatabaseDefinition database = FlowManager.getDatabase(FlowDatabase.class);
        PersonDBFlowRepository repository = new PersonDBFlowRepository(database);
        return Collections.unmodifiableList(repository.findByGender(gender));
    }

    @Override
    public void updatePerson(int id, String name, Gender gender, LocalDate birthday) {
        DatabaseDefinition database = FlowManager.getDatabase(FlowDatabase.class);
        PersonDBFlowRepository repository = new PersonDBFlowRepository(database);
        Optional<Person> mayPerson = repository.findById(id);
        mayPerson.ifPresent(person -> {
            person.setName(name);
            person.setGender(gender);
            person.setBirthday(birthday);
            repository.update(person);
        });
    }

    @Override
    public void deletePerson(int id) {
        DatabaseDefinition database = FlowManager.getDatabase(FlowDatabase.class);
        PersonDBFlowRepository repository = new PersonDBFlowRepository(database);
        Optional<Person> mayPerson = repository.findById(id);
        mayPerson.ifPresent(repository::delete);
    }
}
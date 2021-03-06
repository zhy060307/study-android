package alvin.base.kotlin.dagger.views

import alvin.base.kotlin.R
import alvin.base.kotlin.common.domain.models.Gender
import alvin.base.kotlin.common.domain.models.Person
import alvin.base.kotlin.common.views.PersonDialog
import alvin.base.kotlin.common.views.PersonListAdapter
import alvin.base.kotlin.dagger.DaggerContracts
import alvin.lib.mvp.contracts.adapters.ActivityAdapter
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_dbflow.*
import org.jetbrains.anko.sdk27.coroutines.onCheckedChange
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.toast

class DaggerActivity :
        ActivityAdapter<DaggerContracts.Presenter>(),
        DaggerContracts.View {

    companion object {
        val TAG = DaggerActivity::class.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dagger)

        initialize()
    }

    private fun initialize() {
        val adapter = PersonListAdapter(this, emptyList())
        rv_persons.adapter = adapter
        rv_persons.itemAnimator = DefaultItemAnimator()
        rv_persons.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        adapter.onItemEditListener = { person ->
            onPersonEdit(person)
        }
        adapter.onItemDeleteListener = { person ->
            onPersonDelete(person)
        }

        rg_gender.onCheckedChange(handler = { _, _ ->
            presenter.loadPersons(getGender())
        })

        fab.onClick { _ ->
            val dlg = PersonDialog.Builder(this@DaggerActivity).create(R.string.title_form_dialog)

            dlg.onConfirmClickListener = View.OnClickListener {
                presenter.savePerson(Person(dlg.name, dlg.gender, dlg.birthday))
                dlg.dismiss()
            }
            dlg.show()
        }
    }

    private fun onPersonDelete(person: Person) {
        presenter.deletePerson(person)
    }

    private fun onPersonEdit(person: Person) {
        val dlg = PersonDialog.Builder(this).create(R.string.title_form_dialog)
        dlg.name = person.name
        dlg.gender = person.gender
        dlg.birthday = person.birthday

        dlg.onConfirmClickListener = View.OnClickListener {
            person.name = dlg.name
            person.gender = dlg.gender
            person.birthday = dlg.birthday
            presenter.updatePerson(person)
            dlg.dismiss()
        }
        dlg.show()
    }

    override fun onResume() {
        super.onResume()
        presenter.loadPersons(getGender())
    }

    override fun personCreated(person: Person) {
        presenter.loadPersons(getGender())
        toast(R.string.msg_person_created).show()
    }

    override fun showPersons(persons: List<Person>?) {
        if (persons != null) {
            (rv_persons.adapter as PersonListAdapter).update(persons)
        }
    }

    private fun getGender(): Gender? {
        return when (rg_gender.checkedRadioButtonId) {
            R.id.rb_male -> Gender.MALE
            R.id.rb_female -> Gender.FEMALE
            else -> null
        }
    }

    override fun personUpdated(person: Person) {
        toast(R.string.msg_person_updated).show()
        presenter.loadPersons(getGender())
    }

    override fun personDeleted(person: Person) {
        toast(R.string.msg_person_deleted).show()
        presenter.loadPersons(getGender())
    }

    override fun showException(error: Throwable?) {
        if (error != null) {
            Log.e(TAG, "Exception cause", error)
        }
        toast(R.string.error_exception_caused).show()
    }
}

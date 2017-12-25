package alvin.base.kotlin

import alvin.base.kotlin.dagger.DaggerModule
import alvin.base.kotlin.dbflow.DBFlowModule
import alvin.base.kotlin.main.MainModule
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidSupportInjectionModule::class,
    ApplicationModule::class,
    MainModule::class,
    DaggerModule::class,
    DBFlowModule::class
])
interface ApplicationComponent : AndroidInjector<Application> {

    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<Application>()
}

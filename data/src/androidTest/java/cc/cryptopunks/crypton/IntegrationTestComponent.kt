package cc.cryptopunks.crypton

import android.app.Application
import androidx.room.Room
import cc.cryptopunks.crypton.component.DaoComponent
import cc.cryptopunks.crypton.data.Database
import cc.cryptopunks.crypton.module.DaoModule
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Singleton
@Component(modules = [TestModule::class])
internal interface IntegrationTestComponent : DaoComponent {

    val database: Database
}

@Module(includes = [DaoModule::class])
internal class TestModule(
    @get:Provides
    val context: Application
) {

    @Provides
    @Singleton
    fun appDatabase(context: Application) = Room
        .inMemoryDatabaseBuilder(context, Database::class.java)
        .build()
}



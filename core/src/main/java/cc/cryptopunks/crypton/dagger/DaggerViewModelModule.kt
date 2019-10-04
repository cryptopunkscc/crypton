package cc.cryptopunks.crypton.dagger

import cc.cryptopunks.crypton.component.ViewModelComponent
import cc.cryptopunks.crypton.util.Scopes
import dagger.Module
import dagger.Provides

@Module
class DaggerViewModelModule(
    private val viewModelComponent: ViewModelComponent
) : ViewModelComponent {

    @get:Provides
    override val viewModelScope: Scopes.ViewModel
        get() = viewModelComponent.viewModelScope
}
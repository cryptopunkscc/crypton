package cc.cryptopunks.crypton.component

import cc.cryptopunks.crypton.util.Scopes

interface ViewModelComponent : FeatureComponent {
    val viewModelScope: Scopes.ViewModel
}
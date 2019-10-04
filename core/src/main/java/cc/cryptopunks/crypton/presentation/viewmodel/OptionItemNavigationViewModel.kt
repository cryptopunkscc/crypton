package cc.cryptopunks.crypton.presentation.viewmodel

import cc.cryptopunks.crypton.dagger.ViewModelScope
import cc.cryptopunks.crypton.util.Navigate
import cc.cryptopunks.crypton.util.OptionItemSelected
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@ViewModelScope
class OptionItemNavigationViewModel @Inject constructor(
    private val navigate: Navigate,
    private val optionItemSelections: OptionItemSelected.Output
) {
    suspend operator fun invoke() = optionItemSelections.collect {
        navigate(it)
    }
}
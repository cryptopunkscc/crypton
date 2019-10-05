package cc.cryptopunks.crypton.presentation.viewmodel

import cc.cryptopunks.crypton.feature.Route
import cc.cryptopunks.crypton.util.Navigate
import cc.cryptopunks.crypton.util.OptionItemSelected
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class OptionItemNavigationViewModel @Inject constructor(
    private val navigate: Navigate,
    private val optionItemSelections: OptionItemSelected.Output
) {
    suspend operator fun invoke() = optionItemSelections.collect {
        navigate(Route.Raw(it))
    }
}
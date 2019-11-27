package cc.cryptopunks.crypton.navigation.service

import cc.cryptopunks.crypton.navigation.OptionItemSelected
import cc.cryptopunks.crypton.navigation.Navigate
import cc.cryptopunks.crypton.navigation.Route
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class OptionItemNavigationService @Inject constructor(
    private val navigate: Navigate,
    private val optionItemSelections: OptionItemSelected.Output
) {
    suspend operator fun invoke() = optionItemSelections.collect {
        navigate(Route.Raw(it))
    }
}
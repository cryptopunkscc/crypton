package cc.cryptopunks.crypton.navigation.service

import cc.cryptopunks.crypton.navigation.OptionItem
import cc.cryptopunks.crypton.navigation.Route
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class OptionItemNavigationService @Inject constructor(
    private val navigate: Route.Api.Navigate,
    private val optionItemSelections: OptionItem.Output
) {
    suspend operator fun invoke() = optionItemSelections.collect {
        navigate(Route.Raw(it))
    }
}
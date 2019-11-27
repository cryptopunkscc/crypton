package cc.cryptopunks.crypton.util

import cc.cryptopunks.crypton.context.OptionItem
import cc.cryptopunks.crypton.context.Route
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
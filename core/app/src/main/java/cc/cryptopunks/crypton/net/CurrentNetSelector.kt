package cc.cryptopunks.crypton.net

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNot
import javax.inject.Inject

class CurrentNetSelector @Inject constructor(
    private val currentNet: Net.Current
) : () -> Flow<Net> by {
    currentNet.filterNot { it.isEmpty }
}
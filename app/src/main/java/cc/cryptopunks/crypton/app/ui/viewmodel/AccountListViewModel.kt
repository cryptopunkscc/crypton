package cc.cryptopunks.crypton.app.ui.viewmodel

import cc.cryptopunks.crypton.core.query.ObserveAccountList
import cc.cryptopunks.crypton.core.util.Schedulers
import javax.inject.Inject

class AccountListViewModel @Inject constructor(
    private val observeAccounts: ObserveAccountList,
    private val schedulers: Schedulers
) {
    val accounts
        get() = observeAccounts()
            .subscribeOn(schedulers.io)
            .observeOn(schedulers.main)!!
}
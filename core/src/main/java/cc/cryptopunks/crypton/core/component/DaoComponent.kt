package cc.cryptopunks.crypton.core.component

import cc.cryptopunks.crypton.core.entity.Account

interface DaoComponent {
    val accountDao: Account.Dao
}
package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.util.migration

internal val migrations = arrayOf(
    migration(1, 2) {
        /*no-op*/
    },
    migration(2, 3) {
        execSQL("alter table chat add column isMultiUser integer not null default 0")
    }
)
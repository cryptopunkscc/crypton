package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.util.migration

internal val migrations = arrayOf(
    migration(1, 2) {
        /*no-op*/
    },
    migration(2, 3) {
        execSQL("alter table chat add column isMultiUser integer not null default 0")
    },
    migration(3, 4) {
        /*no-op*/
    },
    migration(4, 5) {
        execSQL("alter table message add column encrypted integer not null default 1")
    }
)

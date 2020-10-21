package cc.cryptopunks.crypton.util.ormlite

import com.j256.ormlite.support.ConnectionSource

interface ConnectionSourceFactory: (String) -> ConnectionSource

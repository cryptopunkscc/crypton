package cc.cryptopunks.crypton.util.ormlite

import com.j256.ormlite.support.ConnectionSource

typealias ConnectionSourceFactory = (String) -> ConnectionSource

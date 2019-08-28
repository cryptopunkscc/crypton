package cc.cryptopunks.crypton.core.util

import android.view.MenuItem
import cc.cryptopunks.crypton.common.Broadcast
import cc.cryptopunks.crypton.core.module.FeatureScope
import javax.inject.Inject

@FeatureScope
class OptionItemSelectedBroadcast @Inject constructor() : Broadcast<MenuItem>()
package cc.cryptopunks.crypton.activity

import android.os.Bundle
import cc.cryptopunks.crypton.fragment.MainFragment
import cc.cryptopunks.crypton.initDebugDrawer
import cc.cryptopunks.crypton.main.R
import cc.cryptopunks.crypton.util.ext.fragment

class MainActivity : FeatureActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)
        initDebugDrawer()
        fragment<MainFragment>()
    }
}
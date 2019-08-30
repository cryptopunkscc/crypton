package cc.cryptopunks.crypton.app.presentation.activity

import android.os.Bundle
import cc.cryptopunks.crypton.app.R
import cc.cryptopunks.crypton.app.presentation.fragment.MainFragment
import cc.cryptopunks.crypton.util.BaseActivity
import cc.cryptopunks.crypton.util.ext.fragment

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)
        fragment<MainFragment>()
    }
}
package com.github.shadowsocks.k

import android.os.Bundle
import android.os.Handler
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import com.github.shadowsocks.k.db.ProfileManager
import com.github.shadowsocks.k.widgets.CircularLoader
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {
    val handler = Handler()
    var circularLoader: CircularLoader by Delegates.notNull()
    var statusTv: TextView by Delegates.notNull()
    var rxTv: TextView by Delegates.notNull()
    var txTv: TextView by Delegates.notNull()
    var txRateTv: TextView by Delegates.notNull()
    var rxRateTv: TextView by Delegates.notNull()

    var statusBar: View by Delegates.notNull()

    var profileManager: ProfileManager by Delegates.notNull()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        circularLoader = findViewById(R.id.circular_loader) as CircularLoader
        statusTv = findViewById(R.id.tv_status) as TextView
        rxTv = findViewById(R.id.tv_rx) as TextView
        txTv = findViewById(R.id.tv_tx) as TextView
        txRateTv = findViewById(R.id.tv_tx_rate) as TextView
        rxRateTv = findViewById(R.id.tv_rx_rate) as TextView
        statusBar = findViewById(R.id.status_bar)

        profileManager = ProfileManager(ShadowsocksApplication.app)

        supportFragmentManager.beginTransaction()
                .add(R.id.fragment_holder, ProfilesFragment())
                .commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_settings -> {
                return true
            }
            R.id.action_import -> {

                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}

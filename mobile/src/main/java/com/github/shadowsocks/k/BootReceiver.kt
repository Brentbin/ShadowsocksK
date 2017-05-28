/**
 * Copyright (C) 2017 lhyz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.github.shadowsocks.k

import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import com.github.shadowsocks.k.utils.startShadowsocksService

/**
 * hello,kotlin
 * Created by lhyz on 2017/5/28.
 *
 * Boot up Receiver
 */
class BootReceiver : BroadcastReceiver() {
    fun isEnabled(context: Context): Boolean = PackageManager.COMPONENT_ENABLED_STATE_ENABLED ==
            context.packageManager.getComponentEnabledSetting(ComponentName(context, BootReceiver::class.java))

    fun setEnabled(context: Context, enabled: Boolean) {
        context.packageManager.setComponentEnabledSetting(
                ComponentName(context, BootReceiver::class.java),
                if (enabled) PackageManager.COMPONENT_ENABLED_STATE_ENABLED else PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP)
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let {
            startShadowsocksService(context)
        }
    }
}
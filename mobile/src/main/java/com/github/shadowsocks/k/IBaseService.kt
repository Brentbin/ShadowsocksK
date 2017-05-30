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

import com.github.shadowsocks.k.db.Profile

/**
 * hello,kotlin
 * Created by lhyz on 2017/5/29.
 */
interface IBaseService {
    fun checkProfile(profile: Profile): Boolean
    fun connect()
    fun startRunner(profile: Profile)
    fun stopRunner(stopService: Boolean, msg: String? = null)
    fun updateTrafficTotal(tx: Long, rx: Long)
    fun updateTrafficRate()

}
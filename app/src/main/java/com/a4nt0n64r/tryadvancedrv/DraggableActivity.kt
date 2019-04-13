/*
 *    Copyright (C) 2015 Haruki Hasegawa
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.a4nt0n64r.tryadvancedrv

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class DraggableActivity : AppCompatActivity() {

    val dataProvider: ListActions?
        get() {
            val fragment = supportFragmentManager.findFragmentByTag(FRAGMENT_TAG_DATA_PROVIDER)
            return (fragment as DataProviderFragment).listActions
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(DataProviderFragment(), FRAGMENT_TAG_DATA_PROVIDER)
                .commit()
            supportFragmentManager.beginTransaction()
                .add(R.id.container, DraggableFragment(), FRAGMENT_LIST_VIEW)
                .commit()
        }
    }

    companion object {
        private val FRAGMENT_TAG_DATA_PROVIDER = "data provider"
        private val FRAGMENT_LIST_VIEW = "list view"
    }
}

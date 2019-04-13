package com.a4nt0n64r.tryadvancedrv

import android.os.Bundle
import androidx.fragment.app.Fragment

//фрагмент, который хранит источник данных?
class DataProviderFragment : Fragment() {
    var listActions: ListActions? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        retainInstance = true  // keep the listActions instance //а зачем он его держит?
        listActions = DataSource()
    }
}


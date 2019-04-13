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

import android.graphics.drawable.NinePatchDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.h6ah4i.android.widget.advrecyclerview.animator.DraggableItemAnimator
import com.h6ah4i.android.widget.advrecyclerview.decoration.ItemShadowDecorator
import com.h6ah4i.android.widget.advrecyclerview.decoration.SimpleListDividerDecorator
import com.h6ah4i.android.widget.advrecyclerview.draggable.RecyclerViewDragDropManager
import com.h6ah4i.android.widget.advrecyclerview.utils.WrapperAdapterUtils

//это надо писать в контроллере?
class DraggableFragment : Fragment() {
    private var recyclerView: RecyclerView? = null
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<*>? = null
    private var wrappedAdapter: RecyclerView.Adapter<*>? = null
    private var recyclerViewDragDropManager: RecyclerViewDragDropManager? = null

    val listActions: ListActions
        get() = (activity as DraggableActivity).dataProvider!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_recycler_list_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        recyclerView = getView()!!.findViewById(R.id.recycler_view)
        layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)

        // drag & drop manager
        recyclerViewDragDropManager = RecyclerViewDragDropManager()
        recyclerViewDragDropManager!!.setDraggingItemShadowDrawable(
            ContextCompat.getDrawable(requireContext(), R.drawable.material_shadow) as NinePatchDrawable?
        )

        //adapter
        val myItemAdapter = DraggableAdapter(listActions)
        adapter = myItemAdapter

        wrappedAdapter = recyclerViewDragDropManager!!.createWrappedAdapter(myItemAdapter)      // wrap for dragging

        val animator = DraggableItemAnimator()

        recyclerView!!.layoutManager = layoutManager
        recyclerView!!.adapter = wrappedAdapter  // requires *wrapped* adapter
        recyclerView!!.itemAnimator = animator

        // если телефон не поддерживает элевэйшн, добавим тени
        if (supportsViewElevation()) {
            // Lollipop or later has native drop shadow feature. ItemShadowDecorator is not required.
        } else {
            recyclerView!!.addItemDecoration(
                ItemShadowDecorator(
                    (ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.material_shadow_z1
                    ) as NinePatchDrawable?)!!
                )
            )
        }

        //ещё одна кроасотв
        //         красота
        recyclerView!!.addItemDecoration(
            SimpleListDividerDecorator(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.list_divider_h
                ), true
            )
        )

        recyclerViewDragDropManager!!.attachRecyclerView(recyclerView!!)

        // for debugging
        //        animator.setDebug(true);
        //        animator.setMoveDuration(2000);
    }

    //дальше пока в проект не вставлял (может и не надо)
    override fun onPause() {
        recyclerViewDragDropManager!!.cancelDrag()
        super.onPause()
    }

    override fun onDestroyView() {
        if (recyclerViewDragDropManager != null) {
            recyclerViewDragDropManager!!.release()
            recyclerViewDragDropManager = null
        }

        if (recyclerView != null) {
            recyclerView!!.itemAnimator = null
            recyclerView!!.adapter = null
            recyclerView = null
        }

        if (wrappedAdapter != null) {
            WrapperAdapterUtils.releaseAll(wrappedAdapter)
            wrappedAdapter = null
        }
        adapter = null
        layoutManager = null

        super.onDestroyView()
    }

    private fun supportsViewElevation(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
    }
}



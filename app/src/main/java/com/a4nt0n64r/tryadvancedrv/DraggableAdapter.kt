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

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.h6ah4i.android.widget.advrecyclerview.draggable.DraggableItemAdapter
import com.h6ah4i.android.widget.advrecyclerview.draggable.ItemDraggableRange
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractDraggableItemViewHolder

internal class DraggableAdapter(private val actions: ListActions) :
    RecyclerView.Adapter<DraggableAdapter.MyViewHolder>(),
    DraggableItemAdapter<DraggableAdapter.MyViewHolder> {

    class MyViewHolder(v: View) : AbstractDraggableItemViewHolder(v) {
        var container: FrameLayout = v.findViewById(R.id.container)
        var dragHandle: View = v.findViewById(R.id.drag_handle)
        var textView: TextView = v.findViewById(android.R.id.text1)
    }

    init {
        // DraggableItemAdapter requires stable ID, and also
        // have to implement the getItemId() method appropriately.
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return actions.getItem(position).id
    }

    override fun getItemViewType(position: Int): Int {
        return actions.getItem(position).viewType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(
            if (viewType == 0) R.layout.list_item_draggable else R.layout.list_item2_draggable,
            parent,
            false
        )
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = actions.getItem(position)

        // set text
        holder.textView.text = item.text

        // set background resource (target view ID: container)
        val dragState = holder.dragState

        if (dragState.isUpdated) {
            val bgResId: Int

            if (dragState.isActive) {
                bgResId = R.drawable.bg_item_dragging_active_state

                // need to clear drawable state here to get correct appearance of the dragging item.
                DrawableUtils.clearState(holder.container.foreground)
            } else if (dragState.isDragging) {
                bgResId = R.drawable.bg_item_dragging_state
            } else {
                bgResId = R.drawable.bg_item_normal_state
            }

            holder.container.setBackgroundResource(bgResId)
        }
    }

    override fun getItemCount(): Int {
        return actions.count
    }

    override fun onMoveItem(fromPosition: Int, toPosition: Int) {
        Log.d(TAG, "onMoveItem(fromPosition = $fromPosition, toPosition = $toPosition)")

        actions.changeItemPosition(fromPosition, toPosition)
    }

    override fun onCheckCanStartDrag(holder: MyViewHolder, position: Int, x: Int, y: Int): Boolean {
        // x, y --- relative from the itemView's top-left
        val containerView = holder.container
        val dragHandleView = holder.dragHandle

        val offsetX = containerView.left + (containerView.translationX + 0.5f).toInt()
        val offsetY = containerView.top + (containerView.translationY + 0.5f).toInt()

        return ViewUtils.hitTest(dragHandleView, x - offsetX, y - offsetY)
    }

    override fun onGetItemDraggableRange(holder: MyViewHolder, position: Int): ItemDraggableRange? {
        // no drag-sortable range specified
        return null
    }

    override fun onCheckCanDrop(draggingPosition: Int, dropPosition: Int): Boolean {
        return true
    }

    override fun onItemDragStarted(position: Int) {
        notifyDataSetChanged()
    }

    override fun onItemDragFinished(fromPosition: Int, toPosition: Int, result: Boolean) {
        notifyDataSetChanged()
    }

    companion object {
        private val TAG = "MyDraggableItemAdapter"
    }
}

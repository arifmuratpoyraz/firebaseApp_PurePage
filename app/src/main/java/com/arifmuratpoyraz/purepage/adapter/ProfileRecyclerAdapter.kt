package com.arifmuratpoyraz.purepage.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.arifmuratpoyraz.purepage.model.Post
import com.arifmuratpoyraz.purepage.R

class ProfileRecyclerAdapter (val itemList : ArrayList<Post>, val itemClickListener: ItemVH.ItemClickListener) : RecyclerView.Adapter<ProfileRecyclerAdapter.ItemVH>() {
    class ItemVH (itemview : View) : RecyclerView.ViewHolder(itemview){

        val profileRecyclerEmailText : TextView = itemView.findViewById(R.id.profileRecyclerEmailText)
        val profileRecyclerPostText : TextView = itemView.findViewById(R.id.profileRecyclerPostText)
        val profileEditButton : ImageButton = itemView.findViewById(R.id.profileEditButton)

        interface ItemClickListener {
            fun onMenuButtonClick(view: View, itm: Post)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemVH {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.profile_recycler_row,parent,false)
        return ItemVH(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: ItemVH, position: Int) {
        holder.profileRecyclerEmailText.text = itemList[position].nick
        holder.profileRecyclerPostText.text = itemList[position].paylasim
        holder.profileEditButton.setOnClickListener {
            itemClickListener.onMenuButtonClick(holder.profileEditButton, itemList[position])
        }
    }

    fun removeItem(postId: String) {
        val position = itemList.indexOfFirst { it.documentId == postId }
        if (position != -1) {
            itemList.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, itemCount)
        }
    }
}
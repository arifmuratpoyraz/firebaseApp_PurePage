package com.arifmuratpoyraz.purepage.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.arifmuratpoyraz.purepage.model.Post
import com.arifmuratpoyraz.purepage.R

class CommentRecyclerAdapter (val itemList : ArrayList<Post>) : RecyclerView.Adapter<CommentRecyclerAdapter.ItemVH>() {
    class ItemVH(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val commentRecyclerEmailText : TextView = itemView.findViewById(R.id.commentRecyclerEmailText)
        val commentRecyclerCommentText : TextView = itemView.findViewById(R.id.commentRecyclerCommentText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemVH {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.comment_recycler_row,parent,false)
        return ItemVH(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: ItemVH, position: Int) {
        holder.commentRecyclerEmailText.text = itemList[position].nick
        holder.commentRecyclerCommentText.text = itemList[position].paylasim
    }
}
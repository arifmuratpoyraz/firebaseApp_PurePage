package com.arifmuratpoyraz.purepage.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.arifmuratpoyraz.purepage.model.Post
import com.arifmuratpoyraz.purepage.R
import com.arifmuratpoyraz.purepage.view.CommentActivity

class PostRecyclerAdapter (val itemList : ArrayList<Post>): RecyclerView.Adapter<PostRecyclerAdapter.ItemVH>() {
    class ItemVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val postRecyclerEmailText : TextView = itemView.findViewById(R.id.postRecyclerEmailText)
        val postRecyclerPostText : TextView = itemView.findViewById(R.id.postRecyclerPostText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemVH {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.post_recycler_row,parent,false)
        return ItemVH(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: ItemVH, position: Int) {
        holder.postRecyclerEmailText.text = itemList[position].nick
        holder.postRecyclerPostText.text = itemList[position].paylasim
        holder.itemView.setOnClickListener{
            val intent = Intent(holder.itemView.context, CommentActivity::class.java)
            val documentId = itemList[position].documentId
            intent.putExtra("Postid", documentId)
            holder.itemView.context.startActivity(intent)
        }
    }
}


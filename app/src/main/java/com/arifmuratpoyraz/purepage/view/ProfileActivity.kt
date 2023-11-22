package com.arifmuratpoyraz.purepage.view

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arifmuratpoyraz.purepage.model.Post
import com.arifmuratpoyraz.purepage.adapter.ProfileRecyclerAdapter
import com.arifmuratpoyraz.purepage.R
import com.arifmuratpoyraz.purepage.model.Singleton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class ProfileActivity : AppCompatActivity(), ProfileRecyclerAdapter.ItemVH.ItemClickListener {

    private lateinit var auth: FirebaseAuth
    private lateinit var recyclerAdapter: ProfileRecyclerAdapter
    private lateinit var database : FirebaseFirestore
    private lateinit var userEmailText: TextView
    lateinit var kullaniciEmail : String
    var postListesi = ArrayList<Post>()
    var nick = Singleton.nick

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val profileRecyclerView : RecyclerView = findViewById(R.id.profileRecyclerView)
        auth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()
        userEmailText = findViewById(R.id.userEmailText)
        kullaniciEmail = auth.currentUser!!.email.toString()
        verileriAl()
        userEmailText.text = kullaniciEmail
        var layoutManager = LinearLayoutManager(this)
        profileRecyclerView.layoutManager = layoutManager
        recyclerAdapter = ProfileRecyclerAdapter(postListesi,this)
        profileRecyclerView.adapter = recyclerAdapter
    }

    @SuppressLint("NotifyDataSetChanged")
    fun verileriAl(){
        database.collection("Post").orderBy("tarih",Query.Direction.DESCENDING)
            .whereEqualTo("kullaniciemail",kullaniciEmail).addSnapshotListener { value, error ->
            if (error!= null){
                println("hata")
            }else{
                if (value != null){
                    if (!value.isEmpty){
                        val documents = value.documents
                        postListesi.clear()
                        for (document in documents){
                            val kullaniciEmail = document.get("kullaniciemail") as String
                            val paylasim = document.get("paylasim") as String
                            val documentId = document.id
                            val nick = document.get("nick") as String
                            val indirilenPost = Post(kullaniciEmail,paylasim,documentId,nick)
                            postListesi.add(indirilenPost)
                        }
                        recyclerAdapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }
    override fun onMenuButtonClick(view: View, itm: Post) {
        val popupMenu = PopupMenu(this, view)
        popupMenu.inflate(R.menu.menu)
        popupMenu.setOnMenuItemClickListener { item: MenuItem ->

            val postId =itm.documentId
            val postRef = database.collection("Post").document(postId)
            val commentsCollection = postRef.collection("Comments")

            when (item.itemId) {
                R.id.update -> {
                    val intent = Intent(this, UpdatePostActivity::class.java)
                    intent.putExtra("postId", postId)
                    intent.putExtra("paylasim", itm.paylasim)
                    startActivity(intent)

                    return@setOnMenuItemClickListener true
                }
                R.id.delete -> {
                    postRef.get().addOnSuccessListener { postDocument ->
                        val postOwnerEmail = postDocument.getString("kullaniciemail")
                        if (kullaniciEmail == postOwnerEmail) {
                            commentsCollection.get().addOnSuccessListener { querySnapshot ->
                                for (document in querySnapshot.documents) {
                                    document.reference.delete()
                                }
                                postRef.delete().addOnSuccessListener {
                                    println("Post silindi.")
                                    recyclerAdapter.removeItem(postId)
                                }.addOnFailureListener { e ->
                                    println("Post silme hatası: $e")
                                }
                            }.addOnFailureListener { e ->
                                println("Yorumları alma hatası: $e")
                            }
                        } else {
                            println("Bu post'u silme izniniz yok.")
                        }
                    }.addOnFailureListener { e ->
                        println("Post verilerini alma hatası: $e")
                    }
                    return@setOnMenuItemClickListener true
                }
                else -> return@setOnMenuItemClickListener false
            }
        }
        popupMenu.show()
    }

    override fun onResume() {
        verileriAl()
        super.onResume()
    }
}
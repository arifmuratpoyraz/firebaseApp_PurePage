package com.arifmuratpoyraz.purepage.view

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arifmuratpoyraz.purepage.adapter.CommentRecyclerAdapter
import com.arifmuratpoyraz.purepage.model.Post
import com.arifmuratpoyraz.purepage.R
import com.arifmuratpoyraz.purepage.model.Singleton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.sql.Timestamp
import java.time.Instant

class CommentActivity : AppCompatActivity() {

     private  var postId : String? = null
    private lateinit var postRef : DocumentReference
    private lateinit var database : FirebaseFirestore
    private lateinit var auth : FirebaseAuth
    private lateinit var commentsCollection: CollectionReference
    private lateinit var commentPostTextView: TextView
    private lateinit var commentEmailTextView: TextView
    private lateinit var commentButton : Button
    private lateinit var commentEditText : TextView
    private lateinit var commentRecyclerView : RecyclerView
    private lateinit var recyclerAdapter: CommentRecyclerAdapter
    var postListesi = ArrayList<Post>()
    var nick = Singleton.nick

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment)
        database = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        commentEmailTextView = findViewById(R.id.postRecyclerEmailText)
        commentPostTextView = findViewById(R.id.postRecyclerPostText)
        commentButton = findViewById(R.id.commentButton)
        commentEditText = findViewById(R.id.commentEditText)
        commentRecyclerView = findViewById(R.id.commentRecyclerView)
        var nick = Singleton.nick


        val intent = intent
        postId = intent.getStringExtra("Postid",)
        postRef = postId?.let { database.collection("Post").document(it) }!!
        val commentsCollection = postRef.collection("Comments")

        paylasimiAl()
        verileriAl()

        var layoutManager = LinearLayoutManager(this)
        commentRecyclerView.layoutManager = layoutManager
        recyclerAdapter = CommentRecyclerAdapter(postListesi)
        commentRecyclerView.adapter = recyclerAdapter

        commentButton.setOnClickListener {
            val kullaniciEmail = auth.currentUser!!.email.toString()
            val yorumText = commentEditText.text.toString()
            val tarih = Timestamp.from(Instant.now())
            val yeniYorum = hashMapOf<String,Any>()
            yeniYorum.put("kullaniciEmail",kullaniciEmail)
            yeniYorum.put("nick",nick)
            yeniYorum.put("yorum",yorumText)
            yeniYorum.put("tarih",tarih)
            commentsCollection.add(yeniYorum)
                .addOnSuccessListener { documentReference ->
                    println("Yeni yorum eklendi: ${documentReference.id}")
                }
                .addOnFailureListener { e ->
                    println("Yorum ekleme hatası: $e")
                }
            commentEditText.text = ""
        }
    }

    fun paylasimiAl(){
        postRef.get().addOnSuccessListener { document ->
            if (document != null && document.exists()) {
                val nick = document.getString("nick")
                val paylasim = document.getString("paylasim")
                commentEmailTextView.text = nick
                commentPostTextView.text = paylasim
            } else {
                println("Belge bulunamadı veya boş.")
            }
        }.addOnFailureListener { exception ->
            println("Belge çekme hatası: ${exception.localizedMessage}")
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun verileriAl() {
        commentsCollection = postRef.collection("Comments")
        commentsCollection
            .orderBy("tarih", Query.Direction.ASCENDING)
            .addSnapshotListener { querySnapshot, exception ->
                if (exception != null) {
                    println("Yorumları alma hatası: $exception")
                }
                querySnapshot?.let {
                    postListesi.clear()
                    for (document in it.documents) {
                        val kullaniciEmail = document.getString("kullaniciEmail") as String
                        val yorum = document.getString("yorum") as String
                        val documentId = document.id
                        val nick = document.getString("nick") as String
                        val indirilenPost = Post(kullaniciEmail,yorum, documentId, nick)
                        postListesi.add(indirilenPost)
                    }
                    recyclerAdapter.notifyDataSetChanged()
                }
            }
    }
}
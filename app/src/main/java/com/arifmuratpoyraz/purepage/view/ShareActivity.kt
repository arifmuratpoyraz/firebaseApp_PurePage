package com.arifmuratpoyraz.purepage.view

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.arifmuratpoyraz.purepage.R
import com.arifmuratpoyraz.purepage.model.Singleton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.sql.Timestamp
import java.time.Instant

class ShareActivity : AppCompatActivity() {

    private lateinit var shareButton: Button
    private lateinit var shareEditText: TextView
    private lateinit var auth : FirebaseAuth
    private lateinit var database : FirebaseFirestore

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share)

        auth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()
        shareButton = findViewById(R.id.shareButton)
        shareEditText = findViewById(R.id.shareEditText)

        shareButton.setOnClickListener {

            val kullaniciEmail = auth.currentUser!!.email.toString()
            val sharedText = shareEditText.text.toString()
            val tarih = Timestamp.from(Instant.now())
            val nick = Singleton.nick

            val postHashMap = hashMapOf<String,Any>()
            postHashMap.put("kullaniciemail",kullaniciEmail)
            postHashMap.put("paylasim",sharedText)
            postHashMap.put("tarih",tarih)
            postHashMap.put("nick",nick!!)

            database.collection("Post").add(postHashMap).addOnCompleteListener{
                if (it.isSuccessful){
                    finish()
                }
            }.addOnFailureListener {
                Toast.makeText(applicationContext,it.localizedMessage,Toast.LENGTH_LONG).show()
            }
        }
    }
}
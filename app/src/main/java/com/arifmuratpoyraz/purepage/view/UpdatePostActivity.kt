package com.arifmuratpoyraz.purepage.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.arifmuratpoyraz.purepage.R
import com.google.firebase.firestore.FirebaseFirestore

class UpdatePostActivity : AppCompatActivity() {

    private lateinit var updateEditText : EditText
    private lateinit var updateButton: Button
    private lateinit var database : FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_post)

        updateButton = findViewById(R.id.updateButton)
        updateEditText = findViewById(R.id.updateEditText)
        database = FirebaseFirestore.getInstance()

        val intent = intent
        val postId = intent.getStringExtra("postId")
        val paylasim = intent.getStringExtra("paylasim")
        val postRef = database.collection("Post").document(postId!!)

        updateEditText.setText(paylasim)

        updateButton.setOnClickListener {
            val sharedText = updateEditText.text.toString()
            val yeniVeriler = hashMapOf<String, Any>()
            yeniVeriler.put("paylasim", sharedText)
            postRef.update(yeniVeriler)
                .addOnSuccessListener {
                    println("Post başarıyla güncellendi.")
                    finish()
                }
                .addOnFailureListener { e ->
                    println("Post güncelleme hatası: $e")
                }
        }
    }
}
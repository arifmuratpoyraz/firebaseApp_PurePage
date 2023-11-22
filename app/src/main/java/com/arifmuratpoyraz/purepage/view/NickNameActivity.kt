package com.arifmuratpoyraz.purepage.view

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.firebase.firestore.FirebaseFirestore
import android.widget.EditText
import android.widget.Toast
import com.arifmuratpoyraz.purepage.R
import com.arifmuratpoyraz.purepage.model.Singleton
import com.google.firebase.auth.FirebaseAuth

class NickNameActivity : AppCompatActivity() {

    private lateinit var nickNameEditText: EditText
    private lateinit var nickNameSaveButton: Button
    private lateinit var auth : FirebaseAuth
    private lateinit var database : FirebaseFirestore

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nick_name)

        nickNameSaveButton = findViewById(R.id.nickNameSaveButton)
        nickNameEditText = findViewById(R.id.nickNameEditText)
        auth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()
        val db = FirebaseFirestore.getInstance()

        nickNameSaveButton.setOnClickListener {
            val selectedNickname = nickNameEditText.text.toString()
            if (selectedNickname.isEmpty()) {
                Toast.makeText(this, "Lütfen Nick Seçin", Toast.LENGTH_LONG).show()
            } else {
                db.collection("Nick")
                    .whereEqualTo("nickname", selectedNickname)
                    .get()
                    .addOnSuccessListener { querySnapshot ->
                        if (querySnapshot != null && !querySnapshot.isEmpty) {
                            Toast.makeText(
                                applicationContext,
                                "Lütfen Başka Bir Nick Seçin",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            val kullaniciEmail = auth.currentUser!!.email.toString()
                            val userHashMap = hashMapOf<String, Any>()
                            userHashMap.put("kullaniciemail", kullaniciEmail)
                            userHashMap.put("nickname", selectedNickname)
                            database.collection("Nick").add(userHashMap)
                                .addOnCompleteListener {
                                    if (it.isSuccessful) {
                                        Singleton.nick = selectedNickname
                                        val intent = Intent(this, HomeActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                    }
                                }.addOnFailureListener {
                                    Toast.makeText(
                                        applicationContext,
                                        it.localizedMessage,
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                        }
                    }
                    .addOnFailureListener { exception ->
                        Toast.makeText(this, exception.localizedMessage, Toast.LENGTH_LONG).show()
                    }
            }
        }
    }
}
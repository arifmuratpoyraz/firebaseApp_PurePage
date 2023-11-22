package com.arifmuratpoyraz.purepage.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.arifmuratpoyraz.purepage.R
import com.arifmuratpoyraz.purepage.model.Singleton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {

    private lateinit var loginButton: Button
    private lateinit var loginEmailEditText: EditText
    private lateinit var loginPasswordEditText: EditText
    private lateinit var registerTextView: TextView
    private lateinit var database : FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginButton = findViewById(R.id.loginButton)
        loginEmailEditText = findViewById(R.id.loginEmailEditText)
        loginPasswordEditText = findViewById(R.id.loginPasswordEditText)
        registerTextView = findViewById(R.id.registerTextView)
        database = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        val kullanici = auth.currentUser

        if (kullanici != null){
           val kullaniciEmail = auth.currentUser!!.email.toString()
           database.collection("Nick").whereEqualTo("kullaniciemail", kullaniciEmail)
               .get().addOnSuccessListener {querySnapshot ->
                   if (querySnapshot != null && !querySnapshot.isEmpty) {
                       val documents = querySnapshot.documents
                       for (document in documents){
                           Singleton.nick = document.get("nickname") as String
                       }
                       val intent = Intent(this, HomeActivity::class.java)
                       startActivity(intent)
                       finish()
                   }else   {
                       val intent = Intent(this, NickNameActivity::class.java)
                       startActivity(intent)
                       finish()
                   }
               }
        }
        loginButton.setOnClickListener{
            val email = loginEmailEditText.text.toString()
            val sifre = loginPasswordEditText.text.toString()
            if(email.isEmpty()||sifre.isEmpty()){
                Toast.makeText(this,"Lütfen E-mail ve şifre bilgilerinizi Eksiksiz Doldurun",Toast.LENGTH_LONG).show()
            }else {
                auth.signInWithEmailAndPassword(email, sifre).addOnCompleteListener {
                    if (it.isSuccessful) {
                        val kullaniciEmail = auth.currentUser!!.email.toString()
                        database.collection("Nick").whereEqualTo("kullaniciemail", kullaniciEmail)
                            .get().addOnSuccessListener {querySnapshot ->
                                if (querySnapshot != null && !querySnapshot.isEmpty) {
                                    val documents = querySnapshot.documents
                                    for (document in documents){
                                        Singleton.nick = document.get("nickname") as String
                                    }
                                    val intent = Intent(this, HomeActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }else   {
                                    val intent = Intent(this, NickNameActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                            }
                    }
                }.addOnFailureListener {
                    Toast.makeText(this, it.localizedMessage, Toast.LENGTH_LONG).show()
                }
            }
        }
        registerTextView.setOnClickListener{
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}


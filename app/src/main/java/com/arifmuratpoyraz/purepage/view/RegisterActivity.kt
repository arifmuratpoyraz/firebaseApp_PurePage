package com.arifmuratpoyraz.purepage.view

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.arifmuratpoyraz.purepage.R
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {

    private lateinit var registerButton: Button
    private lateinit var registerEmailEditText: EditText
    private lateinit var registerPasswordEditText: EditText
    private lateinit var auth: FirebaseAuth

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        registerButton = findViewById(R.id.registerButton)
        registerEmailEditText = findViewById(R.id.registerEmailEditText)
        registerPasswordEditText = findViewById(R.id.registerPasswordEditText)
        auth = FirebaseAuth.getInstance()

        registerButton.setOnClickListener {
            val email = registerEmailEditText.text.toString()
            val sifre = registerPasswordEditText.text.toString()

            if(email.isEmpty()||sifre.isEmpty()){
                Toast.makeText(this,"Lütfen E-mail ve şifre bilgilerinizi Eksiksiz Doldurun",Toast.LENGTH_LONG).show()
            }else{
                auth.createUserWithEmailAndPassword(email,sifre).addOnCompleteListener {
                    if (it.isSuccessful){
                        val intent = Intent(this, NickNameActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }.addOnFailureListener {
                    Toast.makeText(applicationContext,it.localizedMessage,Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
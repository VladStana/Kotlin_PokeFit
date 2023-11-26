package com.example.pokefit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.pokefit.LoginActivity
import com.example.pokefit.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var auth: FirebaseAuth
    private lateinit var refUsers: DatabaseReference
    private var firebaseUserID: String = ""

    lateinit var username_reg: EditText
    lateinit var email_reg: EditText
    lateinit var password_reg: EditText
    lateinit var password_reg_verif: EditText
    lateinit var registerButton: Button



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        binding = ActivityMainBinding.inflate(layoutInflater)

        username_reg = findViewById(R.id.username_reg)
        email_reg = findViewById(R.id.email_reg)
        password_reg = findViewById(R.id.password_reg)
        password_reg_verif = findViewById(R.id.password_reg_verif)
        registerButton = findViewById(R.id.registerButton)

        // add other things??

        auth = FirebaseAuth.getInstance()

        registerButton.setOnClickListener {
            registerUser()
        }
    }

    private fun registerUser() {
        val username: String = username_reg.text.toString()
        val email: String = email_reg.text.toString()
        val password: String = password_reg.text.toString()
        val password_v: String = password_reg_verif.text.toString()

        if (username == ""){
            Toast.makeText(this@RegisterActivity,"Please insert username!", Toast.LENGTH_LONG).show()
        }
        else if (email == ""){
            Toast.makeText(this@RegisterActivity,"Please insert e-mail!", Toast.LENGTH_LONG).show()
        }
        else if (password == ""){
            Toast.makeText(this@RegisterActivity,"Please insert password!", Toast.LENGTH_LONG).show()
        }
        else if (password_v == ""){
            Toast.makeText(this@RegisterActivity,"Please verify your password!", Toast.LENGTH_LONG).show()
        }
        else if (password != password_v) {
            Toast.makeText(this@RegisterActivity, "Passwords must match!", Toast.LENGTH_LONG).show()
        }
        else{
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener{ task ->
                    if (task.isSuccessful) {
                        firebaseUserID = auth.currentUser!!.uid
                        refUsers = FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUserID)

                        val userHashMap = HashMap<String, Any>()
                        userHashMap["uid"] = firebaseUserID
                        userHashMap["username"] = username

                        refUsers.updateChildren(userHashMap)

                        val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                        startActivity(intent)

                    }
                    else{
                        Toast.makeText(this@RegisterActivity, "Error:"+ task.exception!!.message.toString(), Toast.LENGTH_LONG).show()
                    }
                }
        }
    }
}
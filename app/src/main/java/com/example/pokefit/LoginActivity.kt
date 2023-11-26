package com.example.pokefit

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.pokefit.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    //Firebase
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient : GoogleSignInClient

    lateinit var email_log : EditText
    lateinit var password_log : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        email_log = findViewById(R.id.email_log)
        password_log = findViewById(R.id.password_log)

        // Normal login
        binding.loginButton.setOnClickListener{
            loginUser()
        }

        // Google login
        auth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("765154408178-3oefdj9ocl84gavf5mbb1l0tsc4gfh4k.apps.googleusercontent.com")
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this , gso)

        binding.googleButton.setOnClickListener{
            signInGoogle()
            functie()
        }

        binding.registerButton.setOnClickListener{
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun functie() {
        val intent = Intent(this@LoginActivity, MapsActivity::class.java)
        startActivity(intent)
    }

    private fun loginUser() {
        val email: String = email_log.text.toString()
        val password: String = password_log.text.toString()

        if (email == ""){
            Toast.makeText(this@LoginActivity,"Please insert email!", Toast.LENGTH_LONG).show()
        }
        else if (password == ""){
            Toast.makeText(this@LoginActivity,"Please insert password!", Toast.LENGTH_LONG).show()
        }
        else{
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener{ task ->
                    if(task.isSuccessful){
                        val intent = Intent(this@LoginActivity, MapsActivity::class.java)
                        startActivity(intent)
                    }
                    else{
                        Toast.makeText(this@LoginActivity, "Error:"+ task.exception!!.message.toString(), Toast.LENGTH_LONG).show()
                    }
                }
        }
    }

    private fun signInGoogle(){
        val signInIntent = googleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result ->
        if (result.resultCode == Activity.RESULT_OK){

            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            handleResults(task)
        }
    }

    private fun handleResults(task: Task<GoogleSignInAccount>) {
        if (task.isSuccessful){
            val account : GoogleSignInAccount? = task.result
            if (account != null){
                updateUI(account)
            }
        }else{
            Toast.makeText(this, task.exception.toString() , Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                val intent = Intent(this, MapsActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()

            }
        }
    }
}
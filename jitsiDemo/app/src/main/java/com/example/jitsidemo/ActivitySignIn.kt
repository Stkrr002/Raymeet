package com.example.jitsidemo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class ActivitySignIn : AppCompatActivity() {
    companion object{
        private const val RC_SIGN_IN = 120
    }

    private lateinit var mAuth : FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var signin : Button
    private lateinit var  dashBoardIntent : Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
        val user = mAuth.currentUser
          dashBoardIntent = Intent(this,MainActivity::class.java)
        installSplashScreen().apply {
           if(user!=null)
           {
               finish()
               startActivity(dashBoardIntent)

           }
        }
        setContentView(R.layout.activity_sign_in)
        signin = findViewById(R.id.signin)
                    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.web_client_id))
                        .requestEmail()
                        .build()
                    googleSignInClient = GoogleSignIn.getClient(this,gso)
                    signin.setOnClickListener {
                        signIn()
                    }

    }

    private fun signIn() {
        val signInIntent =  googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
           val task = GoogleSignIn.getSignedInAccountFromIntent(data)
          val exception = task.exception
            if(task.isSuccessful){
                try{
                    val account = task.getResult(ApiException::class.java)!!
                    firebaseAuthWithGoogle(account.idToken.toString())
                }catch (e: ApiException){
                }
            }
            else{
                Toast.makeText(this, "sigin falied", Toast.LENGTH_SHORT).show()
            }

        }
    }
    private fun firebaseAuthWithGoogle(idToken: String) {
     val credential = GoogleAuthProvider.getCredential(idToken,null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this){
                task->
                if(task.isSuccessful){
                    finish()
                    startActivity(dashBoardIntent)
                }else{

                }
            }
    }

}
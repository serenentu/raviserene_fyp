package com.raviserene.fyp.ui.auth

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.raviserene.fyp.R
import com.raviserene.fyp.viewmodel.AuthState
import com.raviserene.fyp.viewmodel.AuthViewModel

class LoginActivity : AppCompatActivity() {
    private val vm: AuthViewModel by viewModels()
    private lateinit var googleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        val emailField = findViewById<EditText>(R.id.input_email)
        val passField = findViewById<EditText>(R.id.input_password)
        val btnLogin = findViewById<Button>(R.id.btn_login)
        val btnSignup = findViewById<Button>(R.id.btn_signup)
        val btnGoogle = findViewById<Button>(R.id.btn_google)

        btnLogin.setOnClickListener {
            val email = emailField.text.toString().trim()
            val pass = passField.text.toString()
            if (email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Enter email and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            vm.signInWithEmail(email, pass)
        }

        btnSignup.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        btnGoogle.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }

        vm.state.observe(this) { state ->
            when (state) {
                is AuthState.Loading -> {
                }
                is AuthState.Authenticated -> {
                    Toast.makeText(this, "Signed in: ${'$'}{state.uid}", Toast.LENGTH_SHORT).show()
                }
                is AuthState.Unauthenticated -> {
                    state.reason?.let { Toast.makeText(this, it, Toast.LENGTH_SHORT).show() }
                }
                else -> {}
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                val idToken = account?.idToken
                if (idToken != null) {
                    vm.signInWithGoogle(idToken) { state ->
                        runOnUiThread {
                            if (state is AuthState.Authenticated) {
                                Toast.makeText(this, "Google sign-in success", Toast.LENGTH_SHORT).show()
                            } else if (state is AuthState.Unauthenticated) {
                                Toast.makeText(this, "Google sign-in failed: ${'$'}{state.reason}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                } else {
                    Toast.makeText(this, "Google sign-in failed: no id token", Toast.LENGTH_SHORT).show()
                }
            } catch (e: ApiException) {
                Toast.makeText(this, "Google sign-in failed: ${'$'}{e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
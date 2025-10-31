package com.raviserene.fyp.ui.auth

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.raviserene.fyp.data.UserProfile
import com.raviserene.fyp.viewmodel.AuthViewModel
import com.raviserene.fyp.viewmodel.AuthState
import com.raviserene.fyp.R

class SignUpActivity : AppCompatActivity() {
    private val vm: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val nameField = findViewById<EditText>(R.id.input_name)
        val emailField = findViewById<EditText>(R.id.input_email)
        val passField = findViewById<EditText>(R.id.input_password)
        val roleGroup = findViewById<RadioGroup>(R.id.role_group)
        val btnCreate = findViewById<Button>(R.id.btn_create)

        btnCreate.setOnClickListener {
            val name = nameField.text.toString().trim()
            val email = emailField.text.toString().trim()
            val pass = passField.text.toString()
            val role = when (roleGroup.checkedRadioButtonId) {
                R.id.role_driver -> "driver"
                else -> "rider"
            }
            if (name.isEmpty() || email.isEmpty() || pass.length < 6) {
                Toast.makeText(this, "Please enter valid details (password >=6)", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val profile = UserProfile(displayName = name, email = email, role = role)
            vm.signUpWithEmail(email, pass, profile)
            vm.state.observe(this) { state ->
                when (state) {
                    is AuthState.Authenticated -> {
                        Toast.makeText(this, "Account created", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    is AuthState.Unauthenticated -> {
                        state.reason?.let { Toast.makeText(this, it, Toast.LENGTH_SHORT).show() }
                    }
                    else -> {}
                }
            }
        }
    }
}
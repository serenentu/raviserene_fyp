package com.raviserene.fyp.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.raviserene.fyp.viewmodel.AuthState
import com.raviserene.fyp.viewmodel.AuthViewModel

class SplashActivity : AppCompatActivity() {
    private val vm: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(android.R.layout.simple_list_item_1)

        vm.state.observe(this) { state ->
            when(state) {
                is AuthState.Authenticated -> {
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
                is AuthState.Unauthenticated, is AuthState.Uninitialized -> {
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
                else -> { }
            }
        }
    }
}
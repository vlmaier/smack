package org.vmaier.smack.controller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_login.*
import org.vmaier.smack.R
import org.vmaier.smack.service.AuthService


class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    fun loginLoginButtonClicked(view: View) {

        val email = loginEmailText.text.toString()
        val password = loginPasswordText.text.toString()

        AuthService.loginUser(this, email, password) { loginSuccessful ->
            if (loginSuccessful) {
                AuthService.findUserByEmail(this) { findUserSuccessful ->
                    if (findUserSuccessful) {
                        finish()
                    }
                }
            }
        }
    }

    fun loginCreateUserButtonClicked(view: View) {

        val createUserIntent = Intent(this, CreateUserActivity::class.java)
        startActivity(createUserIntent)
        finish()
    }
}

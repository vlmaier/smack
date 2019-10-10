package org.vmaier.smack.controller

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import kotlinx.android.synthetic.main.activity_create_user.*
import org.vmaier.smack.R
import org.vmaier.smack.service.AuthService
import org.vmaier.smack.util.BROADCAST_USER_DATA_CHANGE
import java.util.*


class CreateUserActivity : AppCompatActivity() {

    var userAvatar = "profileDefault"
    var avatarColor = "[0.5, 0.5, 0.5, 1]"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_user)
        createSpinner.visibility = View.INVISIBLE
    }

    fun generateUserAvatar(view: View) {

        val random = Random()
        val color = random.nextInt(2)
        val avatar = random.nextInt(28)

        if (color == 0) {
            userAvatar = "light$avatar"
        } else {
            userAvatar = "dark$avatar"
        }
        val resourceId = resources.getIdentifier(userAvatar, "drawable", packageName)
        createAvatarImageView.setImageResource(resourceId)
    }

    fun generateBackgroundColorButtonClicked(view: View) {

        val random = Random()
        val r = random.nextInt(256)
        val g = random.nextInt(256)
        val b = random.nextInt(256)

        createAvatarImageView.setBackgroundColor(Color.rgb(r, g, b))

        val savedR = r.toDouble() / 255
        val savedG = g.toDouble() / 255
        val savedB = b.toDouble() / 255

        avatarColor = "[$savedR, $savedG, $savedB, 1]"
    }

    fun createUserButtonClicked(view: View) {

        enableSpinner(true)

        val userName = createUserNameText.text.toString()
        val email = createEmailText.text.toString()
        val password = createPasswordText.text.toString()

        if (userName.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
            AuthService.registerUser(this, email, password) { registrationSuccessful ->
                if (registrationSuccessful) {
                    AuthService.loginUser(this, email, password) { loginSuccessful ->
                        if (loginSuccessful) {
                            AuthService.createUser(this, userName, email, userAvatar, avatarColor) { userCreationSuccessful ->
                                if (userCreationSuccessful) {
                                    val userDataChange = Intent(BROADCAST_USER_DATA_CHANGE)
                                    LocalBroadcastManager.getInstance(this).sendBroadcast(userDataChange)
                                    enableSpinner(false)
                                    finish()
                                } else {
                                    showToast()
                                }
                            }
                        } else {
                            showToast()
                        }
                    }
                } else {
                    showToast()
                }
            }
        } else {
            showToast("Make sure user name, email and password are filled in.")
        }
    }

    fun showToast(message: String = "Something went wrong. Please try again.") {

        Toast.makeText(this, "Something went wrong. Please try again.", Toast.LENGTH_LONG).show()
        enableSpinner(false)
    }

    fun enableSpinner(enable: Boolean) {

        if (enable) {
            createSpinner.visibility = View.VISIBLE
        } else {
            createSpinner.visibility = View.INVISIBLE
        }
        createUserButton.isEnabled = !enable
        createAvatarImageView.isEnabled = !enable
        generateBackgroundColorButton.isEnabled = !enable
    }
}

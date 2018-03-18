package com.example.linkedinsignin

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast

import com.linkedin.platform.LISessionManager
import com.linkedin.platform.errors.LIAuthError
import com.linkedin.platform.listeners.AuthListener
import com.linkedin.platform.utils.Scope

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        // Handle responses from the LinkedIn mobile app
        LISessionManager.getInstance(applicationContext).onActivityResult(this, requestCode, resultCode, data)
    }

    fun linkedinLogin(view: View) {
        LISessionManager.getInstance(applicationContext).init(this, buildScope(), object : AuthListener {
            override fun onAuthSuccess() {
                val token = LISessionManager.getInstance(applicationContext).session.accessToken.toString()
                Toast.makeText(applicationContext, "Auth Success, Token: " + token, Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                finish()
            }

            override fun onAuthError(error: LIAuthError) {
                Toast.makeText(applicationContext, "Auth Failed, Token: " + error.toString(), Toast.LENGTH_SHORT).show()
            }
        }, true)
    }

    // Build the list of member permissions our LinkedIn session requires
    private fun buildScope(): Scope {
        return Scope.build(Scope.R_BASICPROFILE, Scope.W_SHARE)
    }
}

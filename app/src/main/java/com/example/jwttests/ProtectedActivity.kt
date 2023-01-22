package com.example.jwttests

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.interfaces.Claim
import com.auth0.jwt.interfaces.DecodedJWT

class ProtectedActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.protected_view)

        var authIndicator: TextView = findViewById(R.id.text_view_indicator)
        getAuthStatus {
            when (it) {
                true -> authIndicator.setText("Authenticated")
                false -> authIndicator.setText("No dice")
            }
        }

        var btnLogout: Button = findViewById(R.id.button_logout)
        btnLogout.setOnClickListener {
            authIndicator.setText("Logging out in 3 seconds. Bye!")
            val timer = object : CountDownTimer(3000, 1000) {
                override fun onTick(millisUntilFinished: Long) {}

                override fun onFinish() {
                    finish()
                }
            }.start()
        }
    }

    // Checks if `id` of a particular user is 1.
    private fun getAuthStatus(callback: (Boolean) -> Unit) {
        val sharedPrefs: SharedPreferences = csiGetSharedPrefs()
        val authToken: String = sharedPrefs.getString("auth_token", null) ?: return callback(false)

        val decodedJWT: DecodedJWT = decodeJWT(authToken)
        val roleClaim: Claim = decodedJWT.getClaim("role")
        val role: CSIRole = tokenRoleToCsiRole(roleClaim.asString())

        if (role.isAdmin()) {
            return callback(true)
        }
        return callback(false)
    }

    private fun decodeJWT(authToken: String): DecodedJWT {
        val algorithm: Algorithm = Algorithm.HMAC256(R.string.JWT_SECRET_KEY.toString())
        val verifier: JWTVerifier = JWT.require(algorithm)
            .build()
        return verifier.verify(authToken)
    }

    fun csiGetSharedPrefs(): SharedPreferences {
        return getSharedPreferences("application_prefs", Context.MODE_PRIVATE)
    }
}
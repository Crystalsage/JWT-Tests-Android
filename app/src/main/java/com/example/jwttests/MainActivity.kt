package com.example.jwttests

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm

class MainActivity : AppCompatActivity() {
    private lateinit var btnLogin: Button
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dashboard)

        etPassword  = findViewById(R.id.edit_text_password)
        etEmail     = findViewById(R.id.edit_text_email)

        btnLogin    = findViewById(R.id.button_login)
        btnLogin.setOnClickListener {
            if (etPassword.text.toString() == "admin" && etEmail.text.toString() == "admin@gmail.com") {
                setAuthToken()
                val intent = Intent(this, ProtectedActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun setAuthToken() {
//         JWT structure:
//         Header: HS256
//         Body:
//          {
//            "student_id": "2019FHCO106"
//            "role": "admin"
//          }
//         Signed with: JWT_SECRET_KEY
        val algorithm: Algorithm = Algorithm.HMAC256(R.string.JWT_SECRET_KEY.toString())
        val jwt: String = JWT.create()
            .withClaim("student_id", "2019FHCO106")
            .withClaim("role", CSIRole.ADMIN.role)
            .sign(algorithm)

        val sharedPrefs = csiGetSharedPrefs()
        sharedPrefs.edit {
            Log.d("TAG", jwt)
            this.putString("auth_token", jwt)
        }
    }

    fun csiGetSharedPrefs(): SharedPreferences {
        return getSharedPreferences("application_prefs", Context.MODE_PRIVATE)
    }
}
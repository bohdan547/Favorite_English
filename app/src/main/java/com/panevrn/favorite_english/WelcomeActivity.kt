package com.panevrn.favorite_english

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.panevrn.favorite_english.databinding.ActivityRegistrationBinding
import com.panevrn.favorite_english.databinding.ActivityWelcomeBinding

class WelcomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeBinding
    private lateinit var userPreferences: UserPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_welcome)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.register)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        userPreferences = UserPreferences(this)

        if (userPreferences.isUserLoggedIn()) {
            // Якщо користувач авторизований, переходимо на головний екран
            val intent = Intent(this@WelcomeActivity, MainActivity::class.java)
            startActivity(intent)
            finish()  // Закриваємо екран авторизації, щоб не повертатись назад
        } else {
            // Якщо не авторизований, показуємо екран реєстрації/входу
            binding = ActivityWelcomeBinding.inflate(layoutInflater)
            setContentView(binding.root)


            val registerButton = findViewById<Button>(R.id.main_activity_register_button)
            registerButton.setOnClickListener {
                val intent = Intent(this, RegistrationActivity::class.java)
                startActivity(intent)
            }

            val loginButton = findViewById<Button>(R.id.main_activity_login_button)
            loginButton.setOnClickListener {
                val intent = Intent(this, AuthorizationActivity::class.java)
                startActivity(intent)
            }
        }
    }
}
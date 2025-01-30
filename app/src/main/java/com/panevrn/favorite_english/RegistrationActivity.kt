package com.panevrn.favorite_english

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.panevrn.favorite_english.databinding.ActivityRegistrationBinding
import com.panevrn.favorite_english.retrofit.AuthRequest
import com.panevrn.favorite_english.retrofit.MainApi
import com.panevrn.favorite_english.retrofit.RegisterRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import org.json.JSONObject

class RegistrationActivity : AppCompatActivity() {
    private lateinit var mainApi: MainApi
    private lateinit var userPreferences: UserPreferences
    lateinit var binding: ActivityRegistrationBinding
    @SuppressLint("MissingInflatedId", "SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userPreferences = UserPreferences(this)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.register)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.register)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.buttonRegister.setOnClickListener {
            register(
                RegisterRequest(
                    name = binding.nameEditText.text.toString(),
                    email = binding.editTextTextEmailAddress.text.toString(),
                    password = binding.passwordEditText.text.toString(),
                    password_confirmation = binding.confirmPasswordEditText.text.toString()
                )
            )
        }

        binding.haveAccount.setOnClickListener {
            val intent = Intent(this@RegistrationActivity, AuthorizationActivity::class.java)
            startActivity(intent)
        }

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY


        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://favorite-english.com:8876/api/").client(client)
            .addConverterFactory(GsonConverterFactory.create()).build()
        mainApi = retrofit.create(MainApi::class.java)

        }

    private fun register(registrationRequest: RegisterRequest) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = mainApi.register(registrationRequest)
                val message = if (!response.isSuccessful) {
                    response.errorBody()?.string()?.let {
                        JSONObject(it).optJSONObject("data")?.let { data ->
                            data.keys().asSequence().joinToString("\n") { key ->
                                val errors = data.getJSONArray(key)
                                val errorMessages = (0 until errors.length()).joinToString(", ") { i ->
                                    errors.getString(i)
                                }
                                "$key: $errorMessages"
                            }
                        } ?: "Unknown error occurred."
                    }
                } else {
                    null // Якщо відповідь успішна, помилок немає
                }

                this@RegistrationActivity.runOnUiThread {
                    if (message != null) {
                        // Виведення помилки, якщо вона є
                        binding.errorRegister.text = message
                    } else {
                        // Успішна реєстрація: перехід на основний екран
                        val user = response.body()
                        if (user != null) {
                            val intent = Intent(this@RegistrationActivity, MainActivity::class.java)
                            startActivity(intent)
                            userPreferences.saveUserData(true)
                            finish() // Закриваємо екран реєстрації
                        }
                    }
                }
            } catch (e: Exception) {
                this@RegistrationActivity.runOnUiThread {
                    binding.errorRegister.text = "An unexpected error occurred: ${e.message}"
                }
            }
        }
    }

}

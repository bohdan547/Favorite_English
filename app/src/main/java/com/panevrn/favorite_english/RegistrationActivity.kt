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

class RegistrationActivity : AppCompatActivity() {
    lateinit var binding: ActivityRegistrationBinding
    @SuppressLint("MissingInflatedId", "SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.register)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.register)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY


        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://favorite-english.com:8876/api/").client(client)
            .addConverterFactory(GsonConverterFactory.create()).build()
        val mainApi = retrofit.create(MainApi::class.java)


            binding.buttonRegister.setOnClickListener {
                CoroutineScope(Dispatchers.IO).launch {
                    val user = mainApi.register(
                        RegisterRequest(
                            binding.nameEditText.text.toString(),
                            binding.editTextTextEmailAddress.text.toString(),
                            binding.passwordEditText.text.toString(),
                            binding.confirmPasswordEditText.text.toString()
                        )
                    )
                    withContext(Dispatchers.Main) {
                        val intents = Intent(this@RegistrationActivity, MainActivity::class.java)
                        startActivity(intents)
                        //перехід на основний екран
                    }
                }
            }
        }
}

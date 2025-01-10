package com.panevrn.favorite_english

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.panevrn.favorite_english.databinding.ActivityAuthorizationBinding
import com.panevrn.favorite_english.databinding.ActivityRegistrationBinding
import com.panevrn.favorite_english.retrofit.AuthRequest
import com.panevrn.favorite_english.retrofit.MainApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AuthorizationActivity : AppCompatActivity() {
    private lateinit var mainApi: MainApi
    lateinit var binding: ActivityAuthorizationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthorizationBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.register)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.register)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.bAuthorization.setOnClickListener {
            auth(
                AuthRequest(
                    email = binding.emailEditTextEmail.text.toString(),
                    password = binding.passwordEditText.text.toString()
                )
            )
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

    private fun auth(authRequest: AuthRequest) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = mainApi.auth(authRequest)
            val message = response.errorBody()?.string()?.let {
                JSONObject(it).getString("message")
            }
            this@AuthorizationActivity.runOnUiThread {
                binding.error.text = message
                val user = response.body()
                if (user != null) {
                    //перехід на основний екран
                    val intents = Intent(this@AuthorizationActivity, MainActivity::class.java)
                    startActivity(intents)
                }
            }

        }
    }
}
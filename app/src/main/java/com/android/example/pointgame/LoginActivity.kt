package com.android.example.pointgame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.android.example.pointgame.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    private val userList: List<User> = listOf(
        User("AABA1234", "1234", "東日本 太郎", "sample@sample.com"),
        User("AABA1235", "1234", "東日本 次郎", "sample2@sample.com")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // バインディング
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ログインボタン押下の挙動
        binding.loginButton.setOnClickListener {
            val id: String = binding.loginId.text.toString()
            val pass: String = binding.loginPassword.text.toString()

            // 繰り返して確認
            userList.forEach {
                // 一致すれば
                if (id == it.id && pass == it.pass) {
                    // MainActivityへ遷移
                    val intent = Intent(applicationContext, MainActivity::class.java)
                    intent.putExtra("NAME", it.name)
                    intent.putExtra("EMAIL", it.email)
                    startActivity(intent)
                }
            }
        }
    }
}

class User(id: String, pass: String, name: String, email: String) {
    val id: String = id
    val pass: String = pass
    val name: String = name
    val email: String = email
}
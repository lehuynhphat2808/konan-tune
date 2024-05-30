package com.example.myapplication.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.View.OnClickListener
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.myapplication.R
import com.example.myapplication.api.ApiClient
import com.example.myapplication.api.response.ApiResponse
import com.example.myapplication.api.service.UserService
import com.example.myapplication.dto.LoginResponse
import com.example.myapplication.dto.RegisterRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.regex.Pattern

class RegisterActivity : AppCompatActivity() {
    private lateinit var btnLogin: Button
    private lateinit var btnSignUp: Button
    private lateinit var edtUsername: EditText
    private lateinit var edtPassword: EditText
    private lateinit var edtNickname: EditText
    private lateinit var edtPhone: EditText
    private lateinit var edtEmail: EditText
    private lateinit var tvError: TextView
    val PHONE_REGEX = Pattern.compile("^(\\+84|0[35789])\\d{8}$").toRegex()
    val EMAIL_REGEX = Pattern.compile("^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$").toRegex()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup_test)

        btnLogin = findViewById(R.id.btn_login)
        btnSignUp = findViewById(R.id.btn_send_password)
        edtUsername = findViewById(R.id.edt_username)
        edtPassword = findViewById(R.id.edt_password)
        edtNickname = findViewById(R.id.edt_nickname)
        edtPhone = findViewById(R.id.edt_phone_number)
        edtEmail = findViewById(R.id.edt_email)
        tvError = findViewById(R.id.tv_error2)
        btnLogin.setOnClickListener {
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        btnSignUp.setOnClickListener(object : OnClickListener {
            override fun onClick(v: View?) {
                val username = edtUsername.text.toString()
                val password = edtPassword.text.toString()
                val nickname = edtNickname.text.toString()
                val phone = edtPhone.text.toString()
                val email = edtEmail.text.toString()
                // Kiểm tra dữ liệu nhập vào
                val errorMsg = validateRegistrationData(
                    username,
                    password,
                    nickname,
                    phone,
                    email
                )
                if(errorMsg.isNotEmpty()) {
                    // Hiển thị thông báo lỗi
                    tvError.visibility = VISIBLE
                    tvError.text = errorMsg
                    return
                }
                val registerRequest = RegisterRequest(username, password, nickname, phone, email)
                val userService = ApiClient.retrofit.create(UserService::class.java)
                userService.register(registerRequest)
                    .enqueue(object : Callback<ApiResponse<LoginResponse>> {
                        override fun onResponse(
                            call: Call<ApiResponse<LoginResponse>>,
                            response: Response<ApiResponse<LoginResponse>>
                        ) {
                            val loginResponse = response.body()?.data
                            Toast.makeText(this@RegisterActivity, "Please Check Your Email", Toast.LENGTH_SHORT).show()
                            val handler = Handler()
                            handler.postDelayed({
                                val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                                startActivity(intent)
                                finish()
                            }, 5000) // 2000ms = 2s
                        }

                        override fun onFailure(
                            call: Call<ApiResponse<LoginResponse>>,
                            t: Throwable
                        ) {
                            // error
                            Toast.makeText(
                                this@RegisterActivity,
                                "Error Register",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    })
            }


        })
    }


    fun validateRegistrationData(
        username: String,
        password: String,
        nickname: String,
        phone: String,
        email: String
    ): String {
        // Kiểm tra tên đăng nhập
        if (username.isEmpty()) {
            return "Username is required\n"
        } else if (username.length < 6) {
            return "Username must be at least 6 characters\n"
        }

        // Kiểm tra mật khẩu
        if (password.isEmpty()) {
            return "Password is required\n"
        } else if (password.length < 8) {
            return "Password must be at least 8 characters\n"
        }

        // Kiểm tra họ tên
        if (nickname.isEmpty()) {
            return "Full name is required\n"
        }

        // Kiểm tra số điện thoại
        if (!phone.matches(PHONE_REGEX)) {
            return "Invalid phone number\n"
        }

        // Kiểm tra email
        if (email.isEmpty()) {
            return "Email is required\n"
        } else if (!email.matches(EMAIL_REGEX)) {
            return "Invalid email\n"
        }
        return ""
    }
}
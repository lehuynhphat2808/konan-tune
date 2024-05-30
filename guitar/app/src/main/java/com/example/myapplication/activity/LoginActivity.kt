package com.example.myapplication.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.myapplication.R
import com.example.myapplication.api.ApiClient
import com.example.myapplication.api.response.ApiResponse
import com.example.myapplication.api.service.UserService
import com.example.myapplication.dto.LoginRequest
import com.example.myapplication.dto.LoginResponse
import com.example.myapplication.model.Role
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var btnLogin: Button
    private lateinit var btnSignUp: Button
    private lateinit var edtUsername: EditText
    private lateinit var edtPassword: EditText
    private lateinit var tvError: TextView
    private lateinit var tvForgotPassword: TextView

    private val TAG = "LoginActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_test)

        val sharedPreferences = getSharedPreferences("userInfo", MODE_PRIVATE)
        val role = sharedPreferences.getString("role", null)
        if (role != null) {
            if (role.equals("ADMIN")) {
                val intent = Intent(this@LoginActivity, AdminActivity::class.java)
                startActivity(intent)
                finish()
            } else if (role.equals("USER")) {
                val intent = Intent(this@LoginActivity, ProductActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        btnLogin = findViewById(R.id.btn_login)
        btnSignUp = findViewById(R.id.btn_send_password)
        tvError = findViewById(R.id.tv_error)
        btnSignUp.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }
        edtUsername = findViewById(R.id.edt_username)
        edtPassword = findViewById(R.id.edt_password)
        btnLogin.setOnClickListener(object : OnClickListener {
            override fun onClick(v: View?) {
                val username = edtUsername.text.toString()
                val password = edtPassword.text.toString()
                if (username.isNotEmpty() && password.isNotEmpty()) {
                    val loginRequest = LoginRequest(username, password)
                    val userService = ApiClient.retrofit.create(UserService::class.java)
                    userService.login(loginRequest)
                        .enqueue(object : Callback<ApiResponse<LoginResponse>> {
                            override fun onResponse(
                                call: Call<ApiResponse<LoginResponse>>,
                                response: Response<ApiResponse<LoginResponse>>
                            ) {
                                if (response.isSuccessful) {
                                    val loginResponse = response.body()?.data
                                    handleLoginResponse(loginResponse!!)
                                    if (loginResponse.user.role == Role.USER) {
                                        val intent =
                                            Intent(this@LoginActivity, ProductActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                    } else if (loginResponse.user.role == Role.ADMIN) {
                                        val intent =
                                            Intent(this@LoginActivity, AdminActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                    }
                                } else if (response.code() == 406) {
                                    tvError.visibility = View.VISIBLE
                                    tvError.text = "The account has not been verified, please check your email"
                                }
                                else {
                                    tvError.visibility = View.VISIBLE
                                    tvError.text = "Wrong Username Or Password"
                                }

                            }

                            override fun onFailure(
                                call: Call<ApiResponse<LoginResponse>>,
                                t: Throwable
                            ) {
                                // error
                                Toast.makeText(
                                    this@LoginActivity,
                                    "Error Login",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                        })
                } else {
                    tvError.visibility = View.VISIBLE
                    tvError.text = "Username And Password Cannot Be Empty"
                }

            }
        })
        tvForgotPassword = findViewById(R.id.tv_forgot_password)
        tvForgotPassword.setOnClickListener {
            val intent = Intent(this@LoginActivity, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }
    }

    fun handleLoginResponse(loginResponse: LoginResponse) {

        val preferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE)
        val editor = preferences.edit()

        editor.putString("token", loginResponse.token)
        editor.putString("userId", loginResponse.user.id.toString())
        editor.putString("cartId", loginResponse.user.cartId.toString())
        editor.putString("nickname", loginResponse.user.nickname)
        editor.putString("role", loginResponse.user.role.toString())


        // lưu các trường khác

        editor.apply()

    }
}
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

class ForgotPasswordActivity : AppCompatActivity() {
    private lateinit var btnSendEmail: Button
    private lateinit var edtEmail: EditText
    private lateinit var tvError: TextView
    val EMAIL_REGEX = Pattern.compile("^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$").toRegex()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        btnSendEmail = findViewById(R.id.btn_send_password)
        edtEmail = findViewById(R.id.edt_email)
        tvError = findViewById(R.id.tv_error2)
        btnSendEmail.setOnClickListener(object : OnClickListener {
            override fun onClick(v: View?) {
                val email = edtEmail.text.toString()
                // Kiểm tra dữ liệu nhập vào
                val errorMsg = validateRegistrationData(
                    email
                )
                if(errorMsg.isNotEmpty()) {
                    // Hiển thị thông báo lỗi
                    tvError.visibility = VISIBLE
                    tvError.text = errorMsg
                    return
                }
                val userService = ApiClient.retrofit.create(UserService::class.java)
                userService.forgotPassword(email)
                    .enqueue(object : Callback<ApiResponse<Boolean>> {
                        override fun onResponse(
                            call: Call<ApiResponse<Boolean>>,
                            response: Response<ApiResponse<Boolean>>
                        ) {
                            val loginResponse = response.body()?.data
                            Toast.makeText(this@ForgotPasswordActivity, "Please Check Your Email", Toast.LENGTH_SHORT).show()
                            val handler = Handler()
                            handler.postDelayed({
                                val intent = Intent(this@ForgotPasswordActivity, LoginActivity::class.java)
                                startActivity(intent)
                                finish()
                            }, 5000) // 2000ms = 2s
                        }

                        override fun onFailure(
                            call: Call<ApiResponse<Boolean>>,
                            t: Throwable
                        ) {
                            // error
                            Toast.makeText(
                                this@ForgotPasswordActivity,
                                "Error Send New Password",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    })
            }


        })
    }


    fun validateRegistrationData(
        email: String
    ): String {
        // Kiểm tra email
        if (email.isEmpty()) {
            return "Email is required\n"
        } else if (!email.matches(EMAIL_REGEX)) {
            return "Invalid email\n"
        }
        return ""
    }
}
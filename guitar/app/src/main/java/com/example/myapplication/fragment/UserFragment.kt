package com.example.myapplication.fragment

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Nickname
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import coil.load
import com.example.myapplication.R
import com.example.myapplication.activity.LoginActivity
import com.example.myapplication.activity.MySellingActivity
import com.example.myapplication.api.ApiClient
import com.example.myapplication.api.response.ApiResponse
import com.example.myapplication.api.service.FileUploadService
import com.example.myapplication.api.service.ProductService
import com.example.myapplication.api.service.UserService
import com.example.myapplication.dto.RegisterRequest
import com.example.myapplication.dto.UserDetail
import com.example.myapplication.model.SellingProduct
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create
import java.io.File
import java.lang.Exception
import java.util.UUID
import java.util.regex.Pattern

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [UserFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UserFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var btnMySelling: Button
    private lateinit var btnSignOut: Button
    private lateinit var btnMyAccount: Button
    private lateinit var btnPassword: Button
    private lateinit var fragment: View
    private lateinit var tvNickname: TextView
    private lateinit var dialog: Dialog
    private var USER_ID: UUID? = null
    private var imageUri: Uri? = null
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private lateinit var ivUserImage: ImageView
    private var linkImage: String? = null
    private lateinit var edtPhone: EditText
    private lateinit var edtNickname: EditText
    private lateinit var edtEmail: EditText
    private lateinit var ivUser: ImageView
    private lateinit var ivUser2: ImageView
    private val TAG = "UserFragment"
    private lateinit var userDetail: UserDetail
    val PHONE_REGEX = Pattern.compile("^(\\+84|0[35789])\\d{8}$").toRegex()
    val EMAIL_REGEX = Pattern.compile("^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$").toRegex()
    private lateinit var tvCoin: TextView




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val sharedPreferences: SharedPreferences = requireActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE)
        val userIdString = sharedPreferences.getString("userId",null)
        if(userIdString != null) {
            USER_ID = UUID.fromString(userIdString)
        }
        fragment = inflater.inflate(R.layout.fragment_user, container, false)
        getUserDetail()

        ivUser = fragment.findViewById(R.id.iv_user)
        ivUser2 = fragment.findViewById(R.id.iv_user2)
        tvNickname = fragment.findViewById(R.id.tv_nickname)
        btnMySelling = fragment.findViewById(R.id.btn_my_selling)
        btnMySelling.setOnClickListener(object : OnClickListener {
            override fun onClick(v: View?) {
                val intent: Intent = Intent(requireActivity(), MySellingActivity::class.java)
                startActivity(intent)
            }

        })
        btnSignOut = fragment.findViewById(R.id.btn_sign_out)
        btnSignOut.setOnClickListener{
            sharedPreferences.edit().clear().apply()
            val intent = Intent(context, LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
        btnMyAccount = fragment.findViewById(R.id.btn_my_account)
        btnMyAccount.setOnClickListener{showDialog()}
        btnPassword = fragment.findViewById(R.id.btn_password)
        btnPassword.setOnClickListener{showDialogPassword()}
        tvCoin = fragment.findViewById(R.id.tv_coin)
        return fragment
    }

    override fun onResume() {
        super.onResume()
        getUserDetail()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Tab3Fragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            UserFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun showDialog() {
        dialog = Dialog(requireContext())
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.dialog_user_info)
        dialog.setCancelable(true)
        val btnCancel: Button = dialog.findViewById(R.id.btn_user_cancel)
        val btnSubmit: Button = dialog.findViewById(R.id.btn_user_submit)
        edtNickname = dialog.findViewById(R.id.edt_user_nickname)
        edtPhone = dialog.findViewById(R.id.edt_user_phone)
        edtEmail = dialog.findViewById(R.id.edt_user_email)
        ivUserImage = dialog.findViewById(R.id.iv_user_image)
        val ivChangeImage: ImageView = dialog.findViewById(R.id.iv_change_user_image)
        btnCancel.setOnClickListener{
            dialog.dismiss()
        }
        if (userDetail != null) {
            edtNickname.setText(userDetail.nickname)
            edtPhone.setText(userDetail.phone)
            edtEmail.setText(userDetail.email)
            linkImage = userDetail.linkImage
            if(userDetail.linkImage?.isNotEmpty() == true) {
                ivUserImage.load(userDetail.linkImage!!.replace("localhost", "10.0.2.2")) {
                    placeholder(R.drawable.default_image)
                    error(R.drawable.default_image)
                }
            }
        }

        ivChangeImage.setOnClickListener{
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false)
            resultLauncher.launch(intent)
        }

        btnSubmit.setOnClickListener(object : OnClickListener {
            override fun onClick(v: View?) {
                // Kiểm tra dữ liệu nhập vào
                val errorMsg = validateRegistrationData(
                    edtNickname.text.toString(),
                    edtPhone.text.toString(),
                    edtEmail.text.toString()
                )
                if(errorMsg.isNotEmpty()) {
                    // Hiển thị thông báo lỗi
                    Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show()
                    return
                }

                if(imageUri != null) {
                    uploadImages()
                }
                else {
                    updateUser()
                }
                dialog.dismiss()
            }

        })

        dialog.show()
    }

    private fun showDialogPassword() {
        val dialog = Dialog(requireContext())
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.dialog_password)
        dialog.setCancelable(true)
        val btnCancel: Button = dialog.findViewById(R.id.btn_user_cancel)
        val btnSubmit: Button = dialog.findViewById(R.id.btn_user_submit)
        val edtNewPassword: EditText = dialog.findViewById(R.id.edt_newpassword)
        val edtReNewPassword: EditText = dialog.findViewById(R.id.edt_re_new_password)
        btnCancel.setOnClickListener{
            dialog.dismiss()
        }
        btnSubmit.setOnClickListener(object : OnClickListener {
            override fun onClick(v: View?) {
                // Kiểm tra dữ liệu nhập vào
                val errorMsg = validatePassword(
                    edtNewPassword.text.toString(),
                    edtReNewPassword.text.toString(),
                )
                if(errorMsg.isNotEmpty()) {
                    // Hiển thị thông báo lỗi
                    Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show()
                    return
                }
                val userService = ApiClient.retrofit.create(UserService::class.java)
                userService.changePassword(USER_ID!!, UserDetail(password = edtNewPassword.text.toString())).enqueue(object: Callback<ApiResponse<Boolean>> {
                    override fun onResponse(
                        call: Call<ApiResponse<Boolean>>,
                        response: Response<ApiResponse<Boolean>>
                    ) {
                        if (response.isSuccessful) {
                            val apiResponse = response.body()
                            val data = apiResponse?.data
                            // Sử dụng thông tin của sản phẩm ở đây
                            data?.let {
                                Log.e(TAG, it.toString())
                                getUserDetail()

                            }
                            Log.e(TAG, "Yêu cầu API changePassword thanh cong : ${response.code()}")

                        } else {
                            // Xử lý lỗi ở đây
                            Log.e(TAG, "Yêu cầu API changePassword thất bại: ${response.toString()}")
                        }
                    }

                    override fun onFailure(call: Call<ApiResponse<Boolean>>, t: Throwable) {
                        Log.e(TAG, "Yêu cầu API updateUser onFailure: ${t.message}")
                    }

                })
                dialog.dismiss()
            }

        })

        dialog.show()
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)

        // Create launcher
        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if(result.resultCode == Activity.RESULT_OK){
                imageUri = result.data?.data!!
                // handle image uri
                ivUserImage.load(imageUri)
            }
        }
    }


    fun uploadImages() {
        val file = saveImageToFile(imageUri!!)
        val contentType = "image/*"
        val requestBody = RequestBody.create(
            contentType.toMediaTypeOrNull(),
            file
        )
        val part = MultipartBody.Part.createFormData(
            "file",
            file.name,
            requestBody
        )
        val fileUploadService = ApiClient.retrofit.create(FileUploadService::class.java)
        fileUploadService.uploadFiles(part).enqueue(object: Callback<ApiResponse<String>> {
            override fun onResponse(
                call: Call<ApiResponse<String>>,
                response: Response<ApiResponse<String>>
            ) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    val data = apiResponse?.data
                    // Sử dụng thông tin của sản phẩm ở đây
                    data?.let {
                        Log.e("api", it)
                        linkImage = (it)
                        updateUser()
                    }
                } else {
                    // Xử lý lỗi ở đây
                    Log.e("API_CALL", "Yêu cầu API thất bại: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<ApiResponse<String>>, t: Throwable) {
                Log.e("API_CALL", "Yêu cầu API onFailure: ${t.message}")
            }

        })
    }

    private fun saveImageToFile(uri: Uri): File {

        // Get input stream from uri
        val inputStream = requireActivity().contentResolver.openInputStream(uri)

        // Create a temp file
        val tempFile = File.createTempFile("image", ".jpg")

        // Write input stream content to temp file
        tempFile.outputStream().use { outputStream ->
            inputStream?.copyTo(outputStream)
        }

        return tempFile

    }


    private fun updateUser() {

        val nickname: String = edtNickname.text.toString()
        val email: String = edtEmail.text.toString()
        val phone: String = edtPhone.text.toString()
        val userDetail = UserDetail(nickname, phone, email, linkImage)
        val userService = ApiClient.retrofit.create(UserService::class.java)

        userService.updateUser(USER_ID!!, userDetail).enqueue(object: Callback<ApiResponse<Boolean>> {
            override fun onResponse(
                call: Call<ApiResponse<Boolean>>,
                response: Response<ApiResponse<Boolean>>
            ) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    val data = apiResponse?.data
                    // Sử dụng thông tin của sản phẩm ở đây
                    data?.let {
                        Log.e(TAG, it.toString())
                        getUserDetail()

                    }
                    Log.e(TAG, "Yêu cầu API updateUser thanh cong : ${response.code()}")

                } else {
                    // Xử lý lỗi ở đây
                    Log.e(TAG, "Yêu cầu API updateUser thất bại: ${response.toString()}")
                }
            }

            override fun onFailure(call: Call<ApiResponse<Boolean>>, t: Throwable) {
                Log.e(TAG, "Yêu cầu API updateUser onFailure: ${t.message}")
            }

        })
    }



    private fun getUserDetail() {
        val userService = ApiClient.retrofit.create(UserService::class.java)
        userService.getById(USER_ID!!).enqueue(object : Callback<ApiResponse<UserDetail>> {
            override fun onResponse(
                call: Call<ApiResponse<UserDetail>>,
                response: Response<ApiResponse<UserDetail>>
            ) {
                if(response.isSuccessful) {
                    val data = response.body()?.data
                    if (data != null) {
                        userDetail = data
                        tvNickname.text = userDetail.nickname
                        tvCoin.text = userDetail.coin.toString()
                        if(userDetail.linkImage?.isNotEmpty() == true) {
                            ivUser.load(userDetail.linkImage!!.replace("localhost", "10.0.2.2")) {
                                placeholder(R.drawable.default_image)
                                error(R.drawable.default_image)
                            }
                            ivUser2.load(userDetail.linkImage!!.replace("localhost", "10.0.2.2")) {
                                placeholder(R.drawable.default_image)
                                error(R.drawable.default_image)
                            }
                        }
                        Log.e(TAG, "Yêu cầu API getUserDetail thanh cong : ${response.code()}")

                    }
                }
            }

            override fun onFailure(call: Call<ApiResponse<UserDetail>>, t: Throwable) {
                Log.e(TAG, "Yêu cầu API getUserDetail onFailure: ${t.message}")
            }

        })

    }
    fun validateRegistrationData(
        nickname: String,
        phone: String,
        email: String
    ): String {
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

    fun validatePassword(
        password: String,
        repassword: String,
    ): String {
        if (password.isEmpty()) {
            return "Password is required\n"
        } else if (password.length < 8) {
            return "Password must be at least 8 characters\n"
        }
        if(password != repassword) {
            return "Repassword not correct"
        }
        return ""
    }


}
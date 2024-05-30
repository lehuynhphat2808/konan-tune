package com.example.myapplication.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.adapter.AddProductImageAdapter
import com.example.myapplication.api.ApiClient
import com.example.myapplication.api.response.ApiResponse
import com.example.myapplication.api.service.CategoryService
import com.example.myapplication.api.service.FileUploadService
import com.example.myapplication.api.service.ProductService
import com.example.myapplication.model.Category
import com.example.myapplication.model.SellingProduct
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.lang.Exception
import java.math.BigDecimal
import java.util.UUID

class AddProductActivity : AppCompatActivity() {
    private val TAG: String? = "AddProductActivity"
    private lateinit var USER_ID: UUID
    private lateinit var btnUploadImage: Button
    private lateinit var rcvImageProduct: RecyclerView
    private var imageUris = arrayListOf<Uri>()
    private lateinit var addProductImageAdapter: AddProductImageAdapter
    private lateinit var btnAdd: Button
    private lateinit var edtName: EditText
    private lateinit var edtPrice: EditText
    private lateinit var edtSumary: EditText
    private lateinit var edtContent: EditText
    private lateinit var edtQuantity: EditText
    private var linkImages: ArrayList<String> = ArrayList()
    private var price = BigDecimal.ZERO
    private lateinit var spnCategory: Spinner
    private lateinit var categoryId: UUID
    private lateinit var spnBrand: Spinner
    private lateinit var spnColor: Spinner
    private var brandNameSelected: String = ""
    private var brandName: String? = null
    private var colorNameSelected: String = ""
    private var colorName: String? = null
    private lateinit var brandNameList: MutableList<String>
    private lateinit var colorNameList: MutableList<String>
    private lateinit var categoryNameList: MutableList<String>
    private lateinit var categoryIdList: MutableList<UUID>

    var parts = arrayListOf<MultipartBody.Part>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)

        rcvImageProduct = findViewById(R.id.rcv_image_product)
        rcvImageProduct.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        btnUploadImage = findViewById(R.id.btn_upload_image);
        addProductImageAdapter = AddProductImageAdapter(ArrayList())
        rcvImageProduct.adapter = addProductImageAdapter

        edtName = findViewById(R.id.edt_name)
        edtPrice = findViewById(R.id.edt_price)
        edtSumary = findViewById(R.id.edt_sumary)
        edtContent = findViewById(R.id.edt_content)
        edtQuantity = findViewById(R.id.edt_quantity)
        spnCategory = findViewById(R.id.spn_category)
        setUpSpinnerCategory()
        spnBrand = findViewById(R.id.spn_brand_filter2)
        setUpSpinnerBrand()
        spnColor = findViewById(R.id.spn_color_filter2)
        setUpSpinnerColor()


        // Create launcher
        val pickImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {activityResult ->
            if(activityResult.resultCode == Activity.RESULT_OK) {
                if(activityResult.data?.clipData != null) {
                    val clipData = activityResult.data!!.clipData
                    for(i in 0 until clipData!!.itemCount) {
                        val uri = clipData.getItemAt(i).uri
                        imageUris.add(uri)
                    }
                } else {
                    // Xử lý 1 ảnh
                    val uri = activityResult.data!!.data
                    uri?.let { imageUris.add(it) }
                }

            }
            addProductImageAdapter.setData(imageUris)
        }


        btnUploadImage.setOnClickListener {
            imageUris = ArrayList()
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.type = "image/*"
            pickImageLauncher.launch(intent)
        }
        btnAdd = findViewById(R.id.btn_add)
        btnAdd.setOnClickListener(object : OnClickListener {
            override fun onClick(v: View?) {
                checkValid()
                uploadImages()
                Log.e("api", parts.toString())
            }

        })

        spnCategory = findViewById(R.id.spn_category)
        setUpSpinnerCategory()

    }

    private fun setUpSpinnerCategory() {
        val categoryNameList = mutableListOf<String>()
        val categoryIdList = mutableListOf<UUID>()

        val categoryService = ApiClient.retrofit.create(CategoryService::class.java)
        categoryService.getAllCategoty().enqueue(object : Callback<ApiResponse<List<Category>>> {
            override fun onResponse(
                call: Call<ApiResponse<List<Category>>>,
                response: Response<ApiResponse<List<Category>>>
            ) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    val data = apiResponse?.data
                    // Sử dụng thông tin của sản phẩm ở đây
                    data?.let {categoryList ->
                        Log.e(TAG, "categoryList: " + categoryList.toString())
                        for(category in categoryList) {
                            categoryNameList.add(category.title)
                            categoryIdList.add(category.id)
                        }
                    }
                    val spnCategoryAdapter = ArrayAdapter(this@AddProductActivity, android.R.layout.simple_spinner_item, categoryNameList)
                    spnCategory.adapter = spnCategoryAdapter
                } else {
                    // Xử lý lỗi ở đây
                    Log.e(TAG, "Yêu cầu API thất bại: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<ApiResponse<List<Category>>>, t: Throwable) {
                Log.e(TAG, "get all category onFailure")
            }

        })
        spnCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{

            override fun onItemSelected(parent: AdapterView<*>?,
                                        view: View?,
                                        position: Int,
                                        id: Long) {

                //Lấy giá trị item được chọn
                categoryId = categoryIdList[position]
                Log.e(TAG, "categoryId: " + categoryId)

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}

        }
    }

    private fun checkValid() {
        val priceString: String = edtPrice.text.toString()
        try {
            price = priceString.toBigDecimal()
        } catch (e: Exception) {
            Toast.makeText(this, "Please enter valid price", Toast.LENGTH_SHORT).show()
        }
    }

    private fun insertSellingProduct() {

        val name: String = edtName.text.toString()
        val sumary: String = edtSumary.text.toString()
        val content: String = edtContent.text.toString()
        val quantity: Int = edtQuantity.text.toString().toInt()
        val brand: String = spnBrand.selectedItem.toString()
        val color: String = spnCategory.selectedItem.toString()
        val sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE)
        val userIdString = sharedPref?.getString("userId", "")
        if(!userIdString.equals("")) {
            USER_ID = UUID.fromString(userIdString)
        }
        val sellingProduct: SellingProduct = SellingProduct(
            null, name, sumary, content, brand, color, price, linkImages, categoryId, USER_ID, true, quantity
        )
        val productService = ApiClient.retrofit.create(ProductService::class.java)

        productService.insertProduct(sellingProduct).enqueue(object: Callback<ApiResponse<String>> {
            override fun onResponse(
                call: Call<ApiResponse<String>>,
                response: Response<ApiResponse<String>>
            ) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    val data = apiResponse?.data
                    // Sử dụng thông tin của sản phẩm ở đây
                    data?.let {
                        Log.e(TAG, it)
                    }
                    Log.e(TAG, "Yêu cầu API productService.insertProduct thanh cong : ${response.code()}")
                    Toast.makeText(this@AddProductActivity, "Insert Product successfully", Toast.LENGTH_SHORT).show()
                    clear()
                } else {
                    // Xử lý lỗi ở đây
                    Log.e(TAG, "Yêu cầu API thất bại: ${response.code()}")
                    Log.e(TAG, "Yêu cầu API thất bại: ${response.toString()}")
                }
            }

            override fun onFailure(call: Call<ApiResponse<String>>, t: Throwable) {
                Log.e(TAG, "Yêu cầu API onFailure: ${t.message}")
            }

        })




    }

    // Upload images
    fun uploadImages() {
        linkImages = ArrayList()
        var i = imageUris.size
        imageUris.forEach { imageUri ->
            val file = saveImageToFile(imageUri)
            val contentType = "image/*"
            val requestBody = RequestBody.create(
//                MediaType.parse(contentType),
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
                            linkImages.add(it)
                            i--
                            if(i == 0) {
                                insertSellingProduct()
                            }

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
    }

    private fun saveImageToFile(uri: Uri): File {

        // Get input stream from uri
        val inputStream = this@AddProductActivity.contentResolver.openInputStream(uri)

        // Create a temp file
        val tempFile = File.createTempFile("image", ".jpg")

        // Write input stream content to temp file
        tempFile.outputStream().use { outputStream ->
            inputStream?.copyTo(outputStream)
        }

        return tempFile

    }

    private fun clear() {
        edtName.text.clear()
        edtSumary.text.clear()
        edtContent.text.clear()
        edtPrice.text.clear()
        imageUris = ArrayList()
        addProductImageAdapter.setData(imageUris)

    }


    private fun setUpSpinnerBrand() {
        brandNameList = mutableListOf("None", "Yamaha", "Fender", "Gibson", "Ibanez", "Taylor")
        val spnBrandAdapter = ArrayAdapter(this@AddProductActivity, android.R.layout.simple_spinner_item, brandNameList)
        spnBrand.adapter = spnBrandAdapter


        spnBrand.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{

            override fun onItemSelected(parent: AdapterView<*>?,
                                        view: View?,
                                        position: Int,
                                        id: Long) {

                //Lấy giá trị item được chọn
                brandNameSelected = brandNameList[position]

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}

        }
    }

    private fun setUpSpinnerColor() {
        colorNameList = mutableListOf("None", "Sunburst", "Black", "White", "Red", "Blue", "Natural", "Cherry", "Burst", "Green", "Yellow", "Purple", "Metallic", "Brown", "Orange")
        val spnColorAdapter = ArrayAdapter(this@AddProductActivity, android.R.layout.simple_spinner_item, colorNameList)
        spnColor.adapter = spnColorAdapter

        spnColor.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{

            override fun onItemSelected(parent: AdapterView<*>?,
                                        view: View?,
                                        position: Int,
                                        id: Long) {

                //Lấy giá trị item được chọn
                colorNameSelected = colorNameList[position]

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}

        }
    }

}


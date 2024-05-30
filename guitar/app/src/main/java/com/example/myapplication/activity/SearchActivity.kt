package com.example.myapplication.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.adapter.ProductAdapter
import com.example.myapplication.adapter.ProductSuggestionAdapter
import com.example.myapplication.api.ApiClient
import com.example.myapplication.api.response.ApiResponse
import com.example.myapplication.api.service.CategoryService
import com.example.myapplication.model.Category
import com.example.myapplication.model.Product
import com.example.myapplication.controller.ProductController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.math.BigDecimal
import java.util.UUID

class SearchActivity : AppCompatActivity() {
    private val TAG = "SearchActivity"
    private lateinit var searchView: SearchView
    private lateinit var ivBackButton: ImageView
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var rcvProductSuggestion: RecyclerView
    private lateinit var productSuggestionViewMolder : ProductController
    private lateinit var productSuggestionAdapter : ProductSuggestionAdapter
    private lateinit var rcvResultSearch : RecyclerView
    private lateinit var resultSearchAdapter: ProductAdapter
    private lateinit var groupFilter: ConstraintLayout
    private lateinit var spnCategory: Spinner
    private lateinit var spnBrand: Spinner
    private lateinit var spnColor: Spinner
    private lateinit var spnSort: Spinner
    private lateinit var btnFilter: Button
    private lateinit var edtMinPrice: EditText
    private lateinit var edtMaxPrice: EditText
    private lateinit var tvFilter: TextView



    private var categoryIdSelected: UUID? = null
    private var categoryId: UUID? = null
    private var brandNameSelected: String = ""
    private var brandName: String? = null
    private var colorNameSelected: String = ""
    private var colorName: String? = null
    private var sortField: String? = null
    private var sortSelected: String? = null
    private var minPrice: BigDecimal? = null
    private var maxPrice: BigDecimal? = null
    private var keyword: String? = ""
    private var productList: List<Product> = ArrayList()
    private var currentPageIndex: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        ivBackButton = findViewById(R.id.iv_back_button)
        groupFilter = findViewById(R.id.group_filter)
        ivBackButton.setOnClickListener(object : OnClickListener {
            override fun onClick(v: View?) {
                onBackPressedDispatcher.onBackPressed()
            }

        })
        searchView = findViewById(R.id.search_view)
        searchView.requestFocus()
        searchView.setIconifiedByDefault(false)


        rcvProductSuggestion = findViewById(R.id.rcv_product_suggestion)
        productSuggestionViewMolder = ViewModelProvider(this)[ProductController::class.java]
        productSuggestionAdapter = ProductSuggestionAdapter(listOf())
        rcvProductSuggestion.layoutManager = LinearLayoutManager(this)
        rcvProductSuggestion.adapter = productSuggestionAdapter

        rcvResultSearch = findViewById(R.id.rcv_result_search);
        resultSearchAdapter = ProductAdapter(ArrayList())
        rcvResultSearch.adapter = resultSearchAdapter
        rcvResultSearch.layoutManager = GridLayoutManager(this, 2)

        // Observe product view model
        productSuggestionViewMolder.products.observe(this) { products ->
            productList = products
            if(rcvProductSuggestion.visibility == View.VISIBLE) {
                Log.e(TAG, products.size.toString())
                productSuggestionAdapter.setData(products)
            } else {
                resultSearchAdapter.setData(products)
            }
        }



        searchView.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                showUIResult()
                keyword = query
                loadMoreProducts(keyword)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                    showUISugesstion()
                if(!newText.equals("")) {
                    keyword = newText
                    productSuggestionViewMolder.fetchProductsFilter(categoryId, keyword, brandName, colorName, minPrice, maxPrice, sortField)
                } else {
                    keyword = null
                    showUIResult()
                    productSuggestionAdapter.setData(ArrayList());
                }
                return true
            }

        })



        rcvResultSearch.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as GridLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0) {
                    // Đã cuộn đến cuối danh sách, hãy tải thêm sản phẩm ở đây
                    loadMoreProducts(keyword)
                }
            }
        })

        // Set up Filter
        spnCategory = findViewById(R.id.spn_category_filter)
        setUpSpinnerCategory()
        spnBrand = findViewById(R.id.spn_brand_filter)
        setUpSpinnerBrand()
        spnColor = findViewById(R.id.spn_color_filter)
        setUpSpinnerColor()
        spnSort = findViewById(R.id.spn_sort_filter)
        setUpSpinnerSort()
        edtMinPrice = findViewById(R.id.edt_min_price)
        edtMaxPrice = findViewById(R.id.edt_max_price)
        tvFilter = findViewById(R.id.tv_filter)
        tvFilter.setOnClickListener {
            if(groupFilter.visibility == View.GONE) {
                groupFilter.visibility = View.VISIBLE
            } else {
                groupFilter.visibility = View.GONE

            }
        }
        btnFilter = findViewById(R.id.btn_filter)
        btnFilter.setOnClickListener(object : OnClickListener {
            override fun onClick(v: View?) {
                handleFilterClick()
            }

        })

    }
    private fun loadMoreProducts(keyword: String? = "") {
        currentPageIndex++
        productSuggestionViewMolder.fetchMoreProductsFilter(categoryId, keyword, brandName, colorName, minPrice, maxPrice, sortField, false, currentPageIndex)

    }

    private fun showUISugesstion() {
        rcvProductSuggestion.visibility = View.VISIBLE
        tvFilter.visibility = View.GONE
        groupFilter.visibility = View.GONE
        rcvResultSearch.visibility = View.GONE
    }

    private fun showUIResult() {
        rcvProductSuggestion.visibility = View.GONE
//        groupFilter.visibility = View.VISIBLE
        tvFilter.visibility = View.VISIBLE
        rcvResultSearch.visibility = View.VISIBLE
    }

    private fun setUpSpinnerCategory() {
        val categoryNameList = mutableListOf<String>("None")
        val categoryIdList = mutableListOf<UUID?>(null)

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
                    val spnCategoryAdapter = ArrayAdapter(this@SearchActivity, android.R.layout.simple_spinner_item, categoryNameList)
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
                categoryIdSelected = categoryIdList[position]
                Log.e(TAG, "categoryId: " + categoryId)

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}

        }
    }

    private fun setUpSpinnerBrand() {
        val brandNameList = mutableListOf("None", "Yamaha", "Fender", "Gibson", "Ibanez", "Taylor")
        val spnBrandAdapter = ArrayAdapter(this@SearchActivity, android.R.layout.simple_spinner_item, brandNameList)
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
        val colorNameList = mutableListOf("None", "Sunburst", "Black", "White", "Red", "Blue", "Natural", "Cherry", "Burst", "Green", "Yellow", "Purple", "Metallic", "Brown", "Orange")
        val spnColorAdapter = ArrayAdapter(this@SearchActivity, android.R.layout.simple_spinner_item, colorNameList)
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

    private fun setUpSpinnerSort() {
        val sortNameList = mutableListOf("None", "A -> Z", "Z -> A", "Low Price -> High Price", "High Price -> Low Price")
        val sortNameField = mutableListOf(null, "name.asc", "name.desc", "price.asc", "price.desc")
        val spnSortAdapter = ArrayAdapter(this@SearchActivity, android.R.layout.simple_spinner_item, sortNameList)
        spnSort.adapter = spnSortAdapter

        spnSort.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{

            override fun onItemSelected(parent: AdapterView<*>?,
                                        view: View?,
                                        position: Int,
                                        id: Long) {

                //Lấy giá trị item được chọn
                sortSelected = sortNameField[position]

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}

        }
    }

    private fun handleFilterClick(){
        sortField = sortSelected
        if(edtMinPrice.text.toString() != "") {
            minPrice = BigDecimal(edtMinPrice.text.toString())
        } else {
            minPrice = null
        }
        if(edtMaxPrice.text.toString() != "") {
            maxPrice = BigDecimal(edtMaxPrice.text.toString())
        } else {
            maxPrice = null
        }
        categoryId = categoryIdSelected
        if(brandNameSelected.equals("None")) {
            brandName = null
        } else {
            brandName = brandNameSelected
        }
        if(colorNameSelected.equals("None")){
            colorName = null
        } else {
            colorName = colorNameSelected
        }
        productSuggestionViewMolder.fetchProductsFilter(categoryId, keyword, brandName, colorName, minPrice, maxPrice, sortField)
    }

}
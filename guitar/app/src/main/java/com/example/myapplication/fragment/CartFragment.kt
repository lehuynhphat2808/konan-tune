package com.example.myapplication.fragment

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.adapter.CartProductAdapter
import com.example.myapplication.api.ApiClient
import com.example.myapplication.api.response.ApiResponse
import com.example.myapplication.api.service.CartService
import com.example.myapplication.api.service.OrderService
import com.example.myapplication.api.service.VoucherService
import com.example.myapplication.model.CartProduct
import com.example.myapplication.dto.InsertOrderRequest
import com.example.myapplication.model.OrderStatus
import com.example.myapplication.model.Voucher
import com.example.myapplication.controller.CartProductController
import com.paypal.checkout.approve.OnApprove
import com.paypal.checkout.cancel.OnCancel
import com.paypal.checkout.createorder.CreateOrder
import com.paypal.checkout.createorder.CurrencyCode
import com.paypal.checkout.createorder.OrderIntent
import com.paypal.checkout.createorder.UserAction
import com.paypal.checkout.error.OnError
import com.paypal.checkout.order.Amount
import com.paypal.checkout.order.AppContext
import com.paypal.checkout.order.OrderRequest
import com.paypal.checkout.order.PurchaseUnit
import com.paypal.checkout.paymentbutton.PaymentButtonContainer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.math.BigDecimal
import java.util.UUID
import java.util.regex.Pattern

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CartFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CartFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private val TAG = "CartFragment"


    private lateinit var rcvCart: RecyclerView
    private lateinit var cartProductAdapter: CartProductAdapter
    private lateinit var cartProductController: CartProductController
    private lateinit var CART_ID: UUID;
    private lateinit var USER_ID: UUID;
    private lateinit var tvSubtotal: TextView

    private lateinit var btnCheckout: Button
    private lateinit var dialog: Dialog
    private lateinit var cartProducts: List<CartProduct>
    private var usedCode: MutableList<String> = mutableListOf()
    private var discount: BigDecimal = BigDecimal.ZERO
    private lateinit var tvDiscountPrice: TextView
    private lateinit var edtVoucherCode: TextView
    private lateinit var tvFinalPrice: TextView
    private lateinit var btnPaypal: PaymentButtonContainer
    val PHONE_REGEX = Pattern.compile("^(\\+84|0[35789])\\d{8}$").toRegex()
    private lateinit var error: String
    private var cartProductIdList = mutableListOf<UUID>()


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
        val fragment = inflater.inflate(R.layout.fragment_cart, container, false)
        // Get UserInfo in shareReference
        val sharedPref = context?.getSharedPreferences("userInfo", Context.MODE_PRIVATE)
        val cartIdString = sharedPref?.getString("cartId", "")
        if(!cartIdString.equals("")) {
            CART_ID = UUID.fromString(cartIdString)
        }
        val userIdString = sharedPref?.getString("userId", "")
        if(!userIdString.equals("")) {
            USER_ID = UUID.fromString(userIdString)
        }
        tvSubtotal = fragment.findViewById(R.id.tv_subTotal)
        // Set up recyclerView
        rcvCart = fragment.findViewById(R.id.rcv_cart)
        cartProductController = ViewModelProvider(this)[CartProductController::class.java]
        rcvCart.layoutManager = LinearLayoutManager(context)
        cartProductAdapter = CartProductAdapter(ArrayList(), CART_ID, tvSubtotal, context)
        rcvCart.adapter = cartProductAdapter
        // Observe product view model
        cartProductController.cartProducts.observe(viewLifecycleOwner) { cartProducts ->
            this.cartProducts = cartProducts
            cartProductAdapter.setData(cartProducts.toMutableList())
        }
        // Set up button checkout
        btnCheckout = fragment.findViewById(R.id.btn_checkout)
        btnCheckout.setOnClickListener { showDialog() }
        cartProductController.fetchCartProducts(CART_ID)


        return fragment
    }



    override fun onResume() {
        super.onResume()
        getSubTotalPrice(CART_ID)
        cartProductController.fetchCartProducts(CART_ID)

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CartFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CartFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun showDialog() {
        dialog = Dialog(requireContext())
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.dialog_order_info)
        dialog.setCancelable(true)
        val btnCancel = dialog.findViewById<Button>(R.id.btn_cancel)
        val edtAddress = dialog.findViewById<EditText>(R.id.edt_address)
        edtVoucherCode = dialog.findViewById<EditText>(R.id.edt_voucher_code)
        val edtContactPhoneNumber = dialog.findViewById<EditText>(R.id.edt_contact_phone_number)
        val btnApply = dialog.findViewById<Button>(R.id.btn_apply)
        val tvSubTotalPrice: TextView = dialog.findViewById(R.id.tv_sub_total_price)
        tvDiscountPrice = dialog.findViewById(R.id.tv_discount_price)
        tvFinalPrice = dialog.findViewById(R.id.tv_final_price)
        tvSubTotalPrice.text = tvSubtotal.text
        tvDiscountPrice.text = discount.toString()
        tvFinalPrice.text = tvSubTotalPrice.text
        btnApply.setOnClickListener{
            val code = edtVoucherCode.text.toString()
            applyVoucher(code)
        }

        btnPaypal = dialog.findViewById(R.id.payment_button_container)
        btnPaypal.setup(

            createOrder =
            CreateOrder { createOrderActions ->
                setUpCartProductIdList()
                val error = validateOrderData(edtContactPhoneNumber.text.toString(), edtAddress.text.toString())
                if(error != "") {
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                    return@CreateOrder
                }

                val order =
                    OrderRequest(
                        intent = OrderIntent.CAPTURE,
                        appContext = AppContext(userAction = UserAction.PAY_NOW),
                        purchaseUnitList =
                        listOf(
                            PurchaseUnit(
                                amount =
                                Amount(currencyCode = CurrencyCode.USD, value = tvFinalPrice.text.toString())
                            )
                        )
                    )
                createOrderActions.create(order)
            },
            onApprove =
            OnApprove { approval ->
                approval.orderActions.capture { captureOrderResult ->
                    Log.i(TAG, "CaptureOrderResult: $captureOrderResult")
                    Toast.makeText(context, "Payment approval", Toast.LENGTH_SHORT).show()
                    val address = edtAddress.text.toString()
                    val contactPhoneNUmber = edtContactPhoneNumber.text.toString()
                    createNewOrder(address, contactPhoneNUmber)
                    dialog.dismiss()
                    onResume()
                }
            },
            onCancel = OnCancel {
                Log.d(TAG, "Buyer canceled the PayPal experience.")
                Toast.makeText(context, "Payment canceled", Toast.LENGTH_SHORT).show()
            },
            onError = OnError { errorInfo ->
                Log.d(TAG, "Error: $errorInfo")
                Toast.makeText(context, "Payment Error", Toast.LENGTH_SHORT).show()
            }


        )

        btnCancel.setOnClickListener{
            dialog.dismiss()
        }
        dialog.show()
    }
    private fun createNewOrder(address: String, contactPhoneNUmber: String) {
        val cartService = ApiClient.retrofit.create(CartService::class.java)

        cartService.insert(cartProductIdList).enqueue(object : Callback<ApiResponse<UUID>> {
            override fun onResponse(
                call: Call<ApiResponse<UUID>>,
                response: Response<ApiResponse<UUID>>
            ) {
                Log.e(TAG, "cartService.insert onResponse")
                Log.e(TAG, response.body().toString())
                val newCartId = response.body()?.data
                newCartId?.let { insertOrder(address, USER_ID, it, contactPhoneNUmber) }
                Log.e(TAG, "newCartId: " + newCartId.toString())
                Log.e(TAG, "cartProductIdList: $cartProductIdList")
                Log.e(TAG, "cartProductIdListSize: ${cartProductIdList.size}")


            }

            override fun onFailure(call: Call<ApiResponse<UUID>>, t: Throwable) {
                Log.e(TAG, "cartService.insert onFailure")
            }
        })
    }

    private fun insertOrder(address: String, userId: UUID, cartId: UUID, contactPhoneNUmber: String) {
        val orderService = ApiClient.retrofit.create(OrderService::class.java)
        orderService.insertOrder(InsertOrderRequest(address, OrderStatus.PENDING, userId, cartId, contactPhoneNUmber)).enqueue(
            object : Callback<ApiResponse<Boolean>> {
                override fun onResponse(
                    call: Call<ApiResponse<Boolean>>,
                    response: Response<ApiResponse<Boolean>>
                ) {
                    Log.e(TAG, response.toString())
                    Log.e(TAG, userId.toString())
                    Log.e(TAG, USER_ID.toString())
                    Log.e(TAG, cartId.toString())


                    Toast.makeText(requireContext(), "Inser Order Successfully", Toast.LENGTH_SHORT).show()
                }

                override fun onFailure(call: Call<ApiResponse<Boolean>>, t: Throwable) {
                    Log.e(TAG, "orderService.insertOrder onFailure")
                }

            })
    }

    private fun applyVoucher(code:String) {
        val voucherService = ApiClient.retrofit.create(VoucherService::class.java)
        voucherService.applyVoucher(code, USER_ID).enqueue(
            object : Callback<ApiResponse<Voucher>> {
                override fun onResponse(
                    call: Call<ApiResponse<Voucher>>,
                    response: Response<ApiResponse<Voucher>>
                ) {
                    if(response.isSuccessful) {
                        val data = response.body()?.data
                        if(data != null && !usedCode.contains(code)) {
                            discount += data.discount
                            usedCode.add(code)
                            tvDiscountPrice.text = discount.toString()
                            Toast.makeText(context, "Apply Code Successfully", Toast.LENGTH_SHORT).show()
                            edtVoucherCode.text = ""
                            tvFinalPrice.text = (BigDecimal(tvSubtotal.text.toString()) - discount).toString()
                        } else {
                            Toast.makeText(context, "This Code Is Wrong Or Has Been Used", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                override fun onFailure(call: Call<ApiResponse<Voucher>>, t: Throwable) {
                    Log.e(TAG, "orderService.insertOrder onFailure")
                }

            })
    }

    fun getSubTotalPrice(cartId: UUID) {
        val cartService = ApiClient.retrofit.create(CartService::class.java)
        cartService.getSubTotalPrice(cartId).enqueue(object :
            Callback<ApiResponse<BigDecimal>> {
            override fun onResponse(
                call: Call<ApiResponse<BigDecimal>>,
                response: Response<ApiResponse<BigDecimal>>
            ) {
                if (response.isSuccessful) {
                    tvSubtotal.text = response.body()?.data.toString()
                }
                else {
                    // Xử lý lỗi ở đây
                    Log.e(TAG, "Yêu cầu API getSubTotalPrice thất bại: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<ApiResponse<BigDecimal>>, t: Throwable) {
                Log.e(TAG, "Yêu cầu API getSubTotalPrice onFailure: ${t.message}")
            }
        })
    }

    fun validateOrderData(
        phone: String,
        address: String?
    ): String {
        // Kiểm tra số điện thoại
        if (!phone.matches(PHONE_REGEX)) {
            return "Invalid phone number\n"
        }
        if(address == null || address.equals("")) {
            return "Address cannot be empty"
        }
        if(cartProductIdList.size == 0) {
            return "Please select product to pay"
        }
        return ""
    }

    private fun setUpCartProductIdList() {
        cartProductIdList = mutableListOf<UUID>()
        for(cartProduct in cartProducts) {
            if(cartProduct.selected) {
                cartProductIdList.add(cartProduct.id)
            }
        }
    }

}
package com.example.myapplication.fragment

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.adapter.VoucherAdapter
import com.example.myapplication.api.ApiClient
import com.example.myapplication.api.response.ApiResponse
import com.example.myapplication.api.service.VoucherService
import com.example.myapplication.model.Voucher
import com.example.myapplication.controller.VoucherController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.format.DateTimeFormatter
import java.util.Calendar


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [VoucherFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class VoucherFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var rcvVoucher: RecyclerView
    private lateinit var voucherAdapter: VoucherAdapter
    private lateinit var voucherController: VoucherController
    private lateinit var dialog: Dialog
    private lateinit var btnAddVoucher: Button
    private lateinit var edtStartAt: EditText
    private lateinit var edtEndAt: EditText
    private lateinit var edtCode: EditText
    private lateinit var edtDiscount: EditText
    private lateinit var btnSubmit: Button
    private lateinit var btnCancel: Button
    private val TAG = "VoucherFragment"


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
        val fragment = inflater.inflate(R.layout.fragment_voucher, container, false)
        rcvVoucher = fragment.findViewById(R.id.rcv_voucher)
        rcvVoucher.layoutManager = LinearLayoutManager(context)
        voucherAdapter = VoucherAdapter(mutableListOf())
        rcvVoucher.adapter = voucherAdapter
        voucherController = ViewModelProvider(this)[VoucherController::class.java]
        voucherController.vouchers.observe(viewLifecycleOwner){ vouchers->
            voucherAdapter.setData(vouchers.toMutableList())
        }
        voucherController.fetchVoucher()
        setUpDialog()
        btnAddVoucher = fragment.findViewById(R.id.btn_add_voucher)
        btnAddVoucher.setOnClickListener{
            dialog.show()
        }
        return fragment
    }

    override fun onResume() {
        super.onResume()
        voucherController.fetchVoucher()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment VoucherFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            VoucherFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun setUpDialog() {
        dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_voucher)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(true)
        edtCode = dialog.findViewById(R.id.edt_code)
        edtStartAt = dialog.findViewById(R.id.edt_start_at)
        edtEndAt = dialog.findViewById(R.id.edt_end_at)
        edtDiscount = dialog.findViewById(R.id.edt_discount)
        btnSubmit = dialog.findViewById(R.id.btn_voucher_submit)
        btnCancel = dialog.findViewById(R.id.btn_voucher_cancel)
        setUpEdtDate(edtStartAt)
        setUpEdtDate(edtEndAt)
        btnSubmit.setOnClickListener{
            val code = edtCode.text.toString()
            val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            val startAt = edtStartAt.text.toString()
            val endAt = edtEndAt.text.toString()
            val discount = edtDiscount.text.toString()
            insertVoucher(Voucher(null, code, startAt, endAt, discount.toBigDecimal()))
        }
        btnCancel.setOnClickListener {
            dialog.dismiss()
        }
    }


    private fun setUpEdtDate(edtDate: EditText) {
        var current: String = ""
        var ddmmyyyy = "DDMMYYYY"
        var cal = Calendar.getInstance()
        edtDate.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.toString().equals(current)) {
                    var clean: String = s.toString().replace(Regex("[^\\d.]"), "")
                    val cleanC: String = current.replace(Regex("[^\\d.]"), "")
                    val cl = clean.length
                    var sel = cl
                    var i = 2
                    while (i <= cl && i < 6) {
                        sel++
                        i += 2
                    }
                    //Fix for pressing delete next to a forward slash
                    if (clean == cleanC) sel--
                    if (clean.length < 8) {
                        clean = clean + ddmmyyyy.substring(clean.length)
                    } else {
                        //This part makes sure that when we finish entering numbers
                        //the date is correct, fixing it otherwise
                        var day = clean.substring(0, 2).toInt()
                        var mon = clean.substring(2, 4).toInt()
                        var year = clean.substring(4, 8).toInt()
                        if (mon > 12) mon = 12
                        cal.set(Calendar.MONTH, mon - 1)
                        year = if (year < 1900) 1900 else if (year > 2100) 2100 else year
                        cal.set(Calendar.YEAR, year)
                        // ^ first set year for the line below to work correctly
                        //with leap years - otherwise, date e.g. 29/02/2012
                        //would be automatically corrected to 28/02/2012
                        day = if (day > cal.getActualMaximum(Calendar.DATE)) cal.getActualMaximum(
                            Calendar.DATE
                        ) else day
                        clean = String.format("%02d%02d%02d", day, mon, year)
                    }
                    clean = String.format(
                        "%s/%s/%s", clean.substring(0, 2),
                        clean.substring(2, 4),
                        clean.substring(4, 8)
                    )
                    sel = if (sel < 0) 0 else sel
                    current = clean
                    edtDate.setText(current)
                    edtDate.setSelection(if (sel < current.length) sel else current.length)
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })
    }

    private fun insertVoucher(voucher: Voucher) {
        val voucherService = ApiClient.retrofit.create(VoucherService::class.java)
        voucherService.insert(voucher)
            .enqueue(object : Callback<ApiResponse<Boolean>> {
                override fun onResponse(
                    call: Call<ApiResponse<Boolean>>,
                    response: Response<ApiResponse<Boolean>>
                ) {
                    if (response.isSuccessful) {
                        Log.d(TAG, "Yêu cầu API insertVoucher thành công ${response.code()}")
                        dialog.dismiss()
                        voucherController.fetchVoucher()
                    } else {
                        // Xử lý lỗi ở đây
                        Log.e(TAG, "Yêu cầu API insertVoucher thất bại: ${response.code()}")
                    }
                }

                override fun onFailure(
                    call: Call<ApiResponse<Boolean>>,
                    t: Throwable
                ) {
                    Log.e(TAG, "Yêu cầu API insertVoucher onFailure: ${t.message}")
                }
            })

    }

}
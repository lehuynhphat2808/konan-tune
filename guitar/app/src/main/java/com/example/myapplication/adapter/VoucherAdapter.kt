package com.example.myapplication.adapter


import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.api.ApiClient
import com.example.myapplication.api.response.ApiResponse
import com.example.myapplication.api.service.VoucherService
import com.example.myapplication.model.Voucher
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.format.DateTimeFormatter
import java.util.Calendar

import java.util.UUID

//The Adapter Pattern allows to separate the data processing logic (model) and the way data is displayed on the interface (view).

class VoucherAdapter(private var vouchers: MutableList<Voucher>) :
    RecyclerView.Adapter<VoucherAdapter.ViewHolder>() {
    private val TAG = "VoucherAdapter"

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val TAG = "VoucherAdapter ViewHolder"
        lateinit var voucherId: UUID
        val tvCode: TextView = itemView.findViewById(R.id.tv_code)
        val tvStartAt: TextView = itemView.findViewById(R.id.tv_start_at)
        val tvEndAt: TextView = itemView.findViewById(R.id.tv_end_at)
        val tvDiscount: TextView = itemView.findViewById(R.id.tv_discount)
        private lateinit var dialog: Dialog
        private lateinit var edtStartAt: EditText
        private lateinit var edtEndAt: EditText
        private lateinit var edtCode: EditText
        private lateinit var edtDiscount: EditText
        private lateinit var btnSubmit: Button
        private lateinit var btnCancel: Button
        init {
            itemView.setOnClickListener {
                dialog.show()
            }
        }
        fun setUpDialogUpdate() {
            dialog = Dialog(itemView.context)
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
            val voucherService = ApiClient.retrofit.create(VoucherService::class.java)
            voucherService.getById(voucherId)
                .enqueue(object : Callback<ApiResponse<Voucher>> {
                    override fun onResponse(
                        call: Call<ApiResponse<Voucher>>,
                        response: Response<ApiResponse<Voucher>>
                    ) {
                        if (response.isSuccessful) {
                            Log.d(TAG, "Yêu cầu API getById thành công ${response.code()}")
                            val voucher = response.body()?.data
                            if(voucher != null) {
                                edtCode.setText(voucher.code);
                                edtStartAt.setText(voucher.startAt);
                                edtEndAt.setText(voucher.endAt);
                                edtDiscount.setText(voucher.discount.toString());
                            }
                        } else {
                            // Xử lý lỗi ở đây
                            Log.e(TAG, "Yêu cầu API getById thất bại: ${response.code()}")
                        }
                    }

                    override fun onFailure(
                        call: Call<ApiResponse<Voucher>>,
                        t: Throwable
                    ) {
                        Log.e(TAG, "Yêu cầu API insertVoucher onFailure: ${t.message}")
                    }
                })
            btnSubmit.setOnClickListener{
                val code = edtCode.text.toString()
                val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                val startAt = edtStartAt.text.toString()
                val endAt = edtEndAt.text.toString()
                val discount = edtDiscount.text.toString()
                updateVoucher(voucherId, Voucher(null, code, startAt, endAt, discount.toBigDecimal()))
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

        private fun updateVoucher(voucherId: UUID, voucher: Voucher) {
            val voucherService = ApiClient.retrofit.create(VoucherService::class.java)
            voucherService.update(voucherId, voucher)
                .enqueue(object : Callback<ApiResponse<Boolean>> {
                    override fun onResponse(
                        call: Call<ApiResponse<Boolean>>,
                        response: Response<ApiResponse<Boolean>>
                    ) {
                        if (response.isSuccessful) {
                            Log.d(TAG, "Yêu cầu API updateVoucher thành công ${response.code()}")
                            dialog.dismiss()
//                            voucherViewModel.fetchVoucher()
                            for(i in 0 until vouchers.size) {
                                if (vouchers[i].id == voucherId) {
                                    voucher.id = voucherId
                                    vouchers[i] = voucher
                                    break
                                }
                            }
                            notifyDataSetChanged()
                        } else {
                            // Xử lý lỗi ở đây
                            Log.e(TAG, "Yêu cầu API updateVoucher thất bại: ${response.code()}")
                        }
                    }

                    override fun onFailure(
                        call: Call<ApiResponse<Boolean>>,
                        t: Throwable
                    ) {
                        Log.e(TAG, "Yêu cầu API updateVoucher onFailure: ${t.message}")
                    }
                })

        }



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_voucher, parent, false)
        return ViewHolder(view)

    }

    override fun getItemCount(): Int {
        return vouchers.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val voucher = vouchers[position]
        holder.voucherId = voucher.id!!
        holder.tvCode.text = voucher.code
        holder.tvStartAt.text = voucher.startAt.toString()
        holder.tvEndAt.text = voucher.startAt.toString()
        holder.tvDiscount.text = voucher.discount.toString()
        holder.setUpDialogUpdate()
    }

    fun setData(newVouchers: MutableList<Voucher>) {
        vouchers = newVouchers
        notifyDataSetChanged()
    }




}




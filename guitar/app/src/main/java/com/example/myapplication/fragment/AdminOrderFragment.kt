package com.example.myapplication.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.activity.OrderDetailActivity
import com.example.myapplication.adapter.AdminOrderAdapter
import com.example.myapplication.model.OrderStatus
import com.example.myapplication.controller.OrderController

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [OrderFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AdminOrderFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var rcvOrder: RecyclerView
    private lateinit var orderController: OrderController
    private lateinit var orderAdapter: AdminOrderAdapter
    private var currentPageIndex: Int = 0
    private lateinit var btnFind: Button
    private lateinit var edtOrderId: EditText
    private val TAG = "AdminOrderFragment"
    private var orderStatus: OrderStatus = OrderStatus.PENDING
    private lateinit var btnPending: Button
    private lateinit var btnProcessing: Button
    private lateinit var btnComplete: Button
    private lateinit var btnCancel: Button



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val fragment = inflater.inflate(R.layout.fragment_admin_order, container, false)
        btnFind = fragment.findViewById(R.id.btn_find)
        edtOrderId = fragment.findViewById(R.id.edt_order_id)
        btnFind.setOnClickListener{
            var intent: Intent = Intent(context, OrderDetailActivity::class.java)
            intent.putExtra("orderId", edtOrderId.text.toString())
            requireContext().startActivity(intent)
        }
        rcvOrder = fragment.findViewById(R.id.rcv_order)
        rcvOrder.layoutManager = LinearLayoutManager(requireContext())
        orderAdapter = AdminOrderAdapter(mutableListOf())
        rcvOrder.adapter = orderAdapter
        orderController = ViewModelProvider(this)[OrderController::class.java]
        orderController.orders.observe(viewLifecycleOwner){ orders->
            orderAdapter.setData(orders.toMutableList())
        }

        rcvOrder.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0) {
                    // Đã cuộn đến cuối danh sách, hãy tải thêm sản phẩm ở đây
                    loadMoreOrders()
                }
            }
        })
        btnPending = fragment.findViewById(R.id.btn_pending)
        btnPending.setOnClickListener{
            changeStatus(OrderStatus.PENDING)
        }
        btnProcessing = fragment.findViewById(R.id.btn_processing)
        btnProcessing.setOnClickListener{changeStatus(OrderStatus.PROCESSING)}
        btnComplete = fragment.findViewById(R.id.btn_complete)
        btnComplete.setOnClickListener{changeStatus(OrderStatus.COMPLETED)}
        btnCancel = fragment.findViewById(R.id.btn_cancel_status)
        btnCancel.setOnClickListener{changeStatus(OrderStatus.CANCELED)}

        return fragment
    }

    override fun onResume() {
        super.onResume()
        orderController.fetchOrdersAdmin(orderStatus)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Tab2Fragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic fun newInstance(param1: String, param2: String) =
            OrderFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
    private fun loadMoreOrders() {
        currentPageIndex++
        orderController.fetchMoreOrders(orderStatus, currentPageIndex)

    }

    private fun changeStatus(newOrderStatus: OrderStatus) {
        orderStatus = newOrderStatus
        orderController.fetchOrdersAdmin(orderStatus)
    }
}
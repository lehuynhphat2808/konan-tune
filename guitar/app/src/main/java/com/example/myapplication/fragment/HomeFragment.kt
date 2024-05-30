package com.example.myapplication.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.myapplication.R
import com.example.myapplication.activity.SearchActivity
import com.example.myapplication.activity.ViewMoreActivity
import com.example.myapplication.adapter.ProductAdapter
import com.example.myapplication.controller.BestSellingController
import com.example.myapplication.controller.DrumController
import com.example.myapplication.controller.PianoController
import com.example.myapplication.controller.GuitarController
import com.example.myapplication.controller.ViolinController
import java.util.UUID

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {

    private lateinit var rcvGuitar: RecyclerView
    private lateinit var rcvPiano: RecyclerView
    private lateinit var rcvViolin: RecyclerView
    private lateinit var rcvDrum: RecyclerView
    private lateinit var rcvBestSelling: RecyclerView
    private lateinit var guitarController: GuitarController
    private lateinit var pianoController: PianoController
    private lateinit var violinViewModel: ViolinController
    private lateinit var drumController: DrumController
    private lateinit var bestSellingController: BestSellingController
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var guitarAdapter : ProductAdapter
    private lateinit var pianoAdapter : ProductAdapter
    private lateinit var violinAdapter : ProductAdapter
    private lateinit var drumAdapter : ProductAdapter
    private lateinit var bestSellingAdapter: ProductAdapter
    private lateinit var tvViewMoreGuitar : TextView
    private lateinit var tvViewMorePiano : TextView
    private lateinit var tvViewMoreViolin : TextView
    private lateinit var tvViewMoreDrum : TextView
    private lateinit var tvViewMoreBestSelling: TextView

    private lateinit var viewPager: ViewPager2
    private val CATEGOTY_ID_VIOLIN = "bcc93103-8af6-43d3-bdfc-5624982bf7b6"
    private val CATEGOTY_ID_PIANO: String = "3bd73648-95c8-45e5-a6fc-810ea6fa64ba"
    private val CATEGOTY_ID_ASCOUTIC_GUITAR: String = "8cc488cf-078c-4df2-a6da-bdd98b14fddb"
    private val CATEGOTY_ID_DRUM: String = "6c604268-aa47-4f07-ba0d-355bc5666ddd"

    private lateinit var Guitar: ConstraintLayout
    private lateinit var Piano: ConstraintLayout
    private lateinit var Violin: ConstraintLayout
    private lateinit var Drum: ConstraintLayout




    private lateinit var fragment: View

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

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
        fragment = inflater.inflate(R.layout.fragment_home, container, false)
        val viewModelProvider = ViewModelProvider(this)

        // Toolbar and menu
        toolbar = fragment.findViewById(R.id.toolbar)
        (requireActivity() as AppCompatActivity).apply {
            setSupportActionBar(toolbar)
            supportActionBar?.setDisplayShowTitleEnabled(false)
        }
        setHasOptionsMenu(true);

        tvViewMoreGuitar = fragment.findViewById(R.id.tv_view_more_guitar)
        tvViewMorePiano = fragment.findViewById(R.id.tv_view_more_piano)
        tvViewMoreViolin = fragment.findViewById(R.id.tv_view_more_violin)
        tvViewMoreDrum = fragment.findViewById(R.id.tv_view_more_drum)
        tvViewMoreBestSelling = fragment.findViewById(R.id.tv_view_more_best_selling)

        rcvBestSelling = fragment.findViewById(R.id.rcv_best_selling)
        bestSellingController = ViewModelProvider(this)[BestSellingController::class.java]
        bestSellingAdapter = ProductAdapter(mutableListOf())
        rcvBestSelling.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rcvBestSelling.adapter = bestSellingAdapter
        bestSellingController.products.observe(viewLifecycleOwner){ products->
            bestSellingAdapter.setData(products)
        }
        bestSellingController.fetchProductBestSelling()

        // Insert data to Recycler View Product
        rcvGuitar = fragment.findViewById(R.id.rcv_guitar)
        guitarController = ViewModelProvider(this)[GuitarController::class.java]
        guitarAdapter = ProductAdapter(ArrayList())
        rcvGuitar.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rcvGuitar.adapter = guitarAdapter

        // Observe product view model
        guitarController.products.observe(viewLifecycleOwner) { products ->
            guitarAdapter.setData(products)
        }
        guitarController.fetchProducts(categoryId = UUID.fromString(CATEGOTY_ID_ASCOUTIC_GUITAR))
        // Piano
        rcvPiano = fragment.findViewById(R.id.rcv_piano)
        pianoController = ViewModelProvider(this)[PianoController::class.java]
        pianoAdapter = ProductAdapter(ArrayList())
        rcvPiano.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rcvPiano.adapter = pianoAdapter

        // Observe product view model
        pianoController.products.observe(viewLifecycleOwner) { products ->
            pianoAdapter.setData(products)
        }
        pianoController.fetchProducts(categoryId = UUID.fromString(CATEGOTY_ID_PIANO))
        // Violin
        rcvViolin = fragment.findViewById(R.id.rcv_violin)
        violinViewModel = ViewModelProvider(this)[ViolinController::class.java]
        violinAdapter = ProductAdapter(ArrayList())
        rcvViolin.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rcvViolin.adapter = violinAdapter

        // Observe product view model
        violinViewModel.products.observe(viewLifecycleOwner) { products ->
            violinAdapter.setData(products)
        }
        violinViewModel.fetchProducts(categoryId = UUID.fromString(CATEGOTY_ID_VIOLIN))
        // Violin
        rcvDrum = fragment.findViewById(R.id.rcv_drum)
        drumController = ViewModelProvider(this)[DrumController::class.java]
        drumAdapter = ProductAdapter(ArrayList())
        rcvDrum.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rcvDrum.adapter = drumAdapter

        // Observe product view model
        drumController.products.observe(viewLifecycleOwner) { products ->
            drumAdapter.setData(products)
        }
        drumController.fetchProducts(categoryId = UUID.fromString(CATEGOTY_ID_DRUM))
        // View More Guitar
        Guitar = fragment.findViewById(R.id.Guitar)
        Guitar.setOnClickListener{
            viewMoreClick(CATEGOTY_ID_ASCOUTIC_GUITAR)
        }
        tvViewMoreGuitar.setOnClickListener(object : OnClickListener {
            override fun onClick(v: View?) {
                viewMoreClick(CATEGOTY_ID_ASCOUTIC_GUITAR)
            }
        })

        Piano = fragment.findViewById(R.id.Piano)
        Piano.setOnClickListener{
            viewMoreClick(CATEGOTY_ID_PIANO)
        }
        tvViewMorePiano.setOnClickListener(object : OnClickListener {
            override fun onClick(v: View?) {
                viewMoreClick(CATEGOTY_ID_PIANO)

            }
        })

        Violin = fragment.findViewById(R.id.Violin)
        Violin.setOnClickListener{
            viewMoreClick(CATEGOTY_ID_VIOLIN)
        }
        tvViewMoreViolin.setOnClickListener(object : OnClickListener {
            override fun onClick(v: View?) {
                viewMoreClick(CATEGOTY_ID_VIOLIN)

            }
        })

        Drum = fragment.findViewById(R.id.Drum)
        Drum.setOnClickListener{
            viewMoreClick(CATEGOTY_ID_DRUM)
        }
        tvViewMoreDrum.setOnClickListener(object : OnClickListener {
            override fun onClick(v: View?) {
                viewMoreClick(CATEGOTY_ID_DRUM)
            }
        })

        tvViewMoreBestSelling.setOnClickListener(object : OnClickListener {
            override fun onClick(v: View?) {
                viewMoreClick("BEST_SELLING")
            }
        })



        return fragment

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
        val menuItem : MenuItem = menu.findItem(R.id.app_bar_search)
        menuItem.setOnMenuItemClickListener {
            val intent = Intent(requireActivity(), SearchActivity::class.java)
            startActivity(intent)
            true
        }

        super.onCreateOptionsMenu(menu, inflater)
    }



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Tab1Fragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    fun viewMoreClick(categoryId: String) {
        val intent = Intent(requireActivity(), ViewMoreActivity::class.java)
        intent.putExtra("categoryId", categoryId)
        startActivity(intent)
    }

}
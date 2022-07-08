package com.testing.myapplication.home

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.testing.myapplication.R
import com.testing.myapplication.adapter.AdapterHome
import com.testing.myapplication.databinding.ActivityHomeBinding
import com.testing.myapplication.home.create.CreateActivity
import com.testing.myapplication.home.detail.DetailActivity
import com.testing.myapplication.response.create.CreateResponse
import com.testing.myapplication.response.product.Data
import com.testing.myapplication.response.product.ProductResponse
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle

class HomeActivity : AppCompatActivity(), HomeContract.View, AdapterHome.ItemAdapterCallback {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var presenter: HomePresenter
    private var loadingDialog: Dialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initLoading()
        presenter = HomePresenter(this)
        presenter.getHome()


        binding.floatingActionButton.setOnClickListener {
            val intent = Intent(this, CreateActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initLoading() {
        loadingDialog = Dialog(this)
        val dialogView = layoutInflater.inflate(R.layout.loading_dialog, null)

        loadingDialog.let {
            it?.setContentView(dialogView)
            it?.setCancelable(false)
            it?.window?.setBackgroundDrawableResource(R.color.tsp)
        }
    }

    override fun onClick(view: View, data: Data) {
        val intent = Intent(this, DetailActivity::class.java)
            .putExtra("data", data)
        startActivity(intent)
    }

    override fun homeSuccess(productResponse: ProductResponse) {
        val adapterHome = AdapterHome(productResponse.data, this, this)
        binding.rvProduct.layoutManager = LinearLayoutManager(this)
        binding.rvProduct.adapter = adapterHome


        adapterHome.setOnDeleteListener { position ->
            productResponse.data[position].id.apply {
                showDialogDelete(this)
            }
        }
    }

    private fun showDialogDelete(it: Int?) {
        val alertDialogBuilder = AlertDialog.Builder(this)

        // set title dialog
        alertDialogBuilder.setTitle("Anda yakin ingin hapus?")

        // set pesan dari dialog
        alertDialogBuilder
            .setMessage("Klik Ya untuk menghapus!")
            .setIcon(R.drawable.ic_launcher_background)
            .setCancelable(false)
            .setPositiveButton(
                "Ya"
            ) { _, id ->
                presenter.submitDelete(it!!)
            }
            .setNegativeButton(
                "Tidak"
            ) { dialog, _ ->
                dialog.cancel()
            }

        val alertDialog: AlertDialog = alertDialogBuilder.create()

        alertDialog.show()
    }

    override fun homeFailure(message: String) {
        MotionToast.darkColorToast(this,
            "Failed ☹️",
            "Cek Internet anda",
            MotionToastStyle.WARNING,
            MotionToast.GRAVITY_BOTTOM,
            MotionToast.LONG_DURATION,
            ResourcesCompat.getFont(this, R.font.helvetica_regular))
    }

    override fun deleteItem(createResponse: CreateResponse) {
        MotionToast.darkColorToast(this,
            "Success",
            "Keranjang Berhasil di Delete",
            MotionToastStyle.SUCCESS,
            MotionToast.GRAVITY_BOTTOM,
            MotionToast.LONG_DURATION,
            ResourcesCompat.getFont(this, R.font.helvetica_regular))
        presenter.getHome()
    }

    override fun showLoading(loading: Boolean) {
        when(loading) {
            true -> loadingDialog?.show()
            false -> loadingDialog?.dismiss()
        }
    }

    override fun onDestroy() {
        presenter.unSubscribe()
        super.onDestroy()
    }
}
package com.testing.myapplication.home.create

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.view.Window
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.res.ResourcesCompat
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.gms.location.*
import com.google.gson.Gson
import com.testing.myapplication.R
import com.testing.myapplication.databinding.ActivityCreateBinding
import com.testing.myapplication.home.HomeActivity
import com.testing.myapplication.response.create.CreateResponse
import com.testing.myapplication.utils.Warungku
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt
import uk.co.samuelwall.materialtaptargetprompt.extras.backgrounds.RectanglePromptBackground
import uk.co.samuelwall.materialtaptargetprompt.extras.focals.RectanglePromptFocal
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle

class CreateActivity : AppCompatActivity(), CreateContract.View {
    private lateinit var binding: ActivityCreateBinding
    private lateinit var presenter: CreatePresenter
    private var loadingDialog: Dialog? = null

    var lat: Double = 0.0
    var long: Double = 0.0
    var filePath: Uri? = null

    lateinit var  fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest
    private var permissionId = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initLoading()
        presenter = CreatePresenter(this)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        getLastLocation()

        binding.trackGps.setOnClickListener {
            getLastLocation()
        }

        binding.ivProduct.setOnClickListener {
            showDialogCamera()
        }

        initView()
    }

    private fun showDialogCamera() {
        val alertDialogBuilder = AlertDialog.Builder(this)

        // set title dialog
        alertDialogBuilder.setTitle("Camera or Galery")

        // set pesan dari dialog
        alertDialogBuilder
            .setMessage("Klik dari salah satu")
            .setIcon(R.drawable.ic_launcher_background)
            .setCancelable(true)
            .setPositiveButton(
                "Galeri"
            ) { _, id ->
                ImagePicker.with(this)
                    .galleryOnly()
                    .cropSquare()
                    .start()
            }
            .setNegativeButton(
                "Camera"
            ) { dialog, _ ->
                ImagePicker.with(this)
                    .cameraOnly()
                    .start()
            }

        val alertDialog: AlertDialog = alertDialogBuilder.create()

        alertDialog.show()
    }

    private fun initView() {
        binding.btnAdd.setOnClickListener {
            val nameWarung = binding.etName.text.toString()
            val alamatWarung = binding.etAddress.text.toString()

            when {

                nameWarung.isEmpty() -> {
                    binding.textInputLayout.error = "isi nama warung anda"
                    binding.textInputLayout.requestFocus()
                }
                alamatWarung.isEmpty() -> {
                    binding.textInputLayout1.error = "isi alamat warung anda"
                    binding.textInputLayout1.requestFocus()
                }

                long == 0.0 -> {
                    addressPrompt()
                }
                else -> {
                    presenter.submitCreate(nameWarung, alamatWarung, lat, long, filePath!!)
                }
            }
        }
    }

    private fun addressPrompt() {
        MaterialTapTargetPrompt.Builder(this).apply {
            setTarget(binding.trackGps)
            setPrimaryText("Klik Tombol disini")
            promptBackground = RectanglePromptBackground()
            promptFocal = RectanglePromptFocal()
            setSecondaryText("untuk mendapatkan Gps anda jgn lupa nyalakan Gps")
            backgroundColour = (Color.BLUE)
        }.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(resultCode) {
            Activity.RESULT_OK -> {
                filePath = data?.data

                Glide.with(this)
                    .load(filePath)
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(binding.ivProduct)
            }
            ImagePicker.RESULT_ERROR -> {
                Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            }
            else -> {
                Toast.makeText(this, "Tekan sekali lagi", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun getLastLocation() {
        if (checkPermission()) {
            if (isLocationEnabled()){
                fusedLocationProviderClient.lastLocation.addOnCompleteListener {
                    val location = it.result
                    if (location == null) {
                        getLocationNew()
                    }else {
                        lat = location.latitude
                        long = location.longitude
                        binding.tvLong.text = "${location.latitude}, ${location.latitude}"

                    }
                }
            }else {
                showDialog()
            }
        }else {
            requestPermission()
        }
    }

    private fun showDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.gps_dialog)

        val btnClose = dialog.findViewById<ImageView>(R.id.btnClose)
        btnClose.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun isLocationEnabled():Boolean {

        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

    }

    private fun checkPermission():Boolean {
        if (ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_COARSE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            return true
        }

        return false
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION,android.Manifest.permission.ACCESS_COARSE_LOCATION,
            ),permissionId
        )
    }

    @SuppressLint("MissingPermission")
    private fun getLocationNew() {
        locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 0
        locationRequest.fastestInterval = 0
        locationRequest.numUpdates = 2
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,locationCallback, Looper.myLooper()!!
        )
    }

    private val locationCallback = object : LocationCallback() {
        @SuppressLint("SetTextI18n")
        override fun onLocationResult(p0: LocationResult) {
            val lastLocation = p0.lastLocation
            lat = lastLocation!!.latitude
            long = lastLocation.longitude
            binding.tvLong.text = "${lastLocation.latitude}, ${lastLocation.latitude}"

        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode == permissionId) {
            if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) return
        }
    }


    @SuppressLint("InflateParams")
    private fun initLoading() {
        loadingDialog = Dialog(this)
        val dialogView = layoutInflater.inflate(R.layout.loading_dialog, null)

        loadingDialog.let {
            it?.setContentView(dialogView)
            it?.setCancelable(false)
            it?.window?.setBackgroundDrawableResource(R.color.tsp)
        }
    }

    override fun createSuccess(createResponse: CreateResponse) {
        MotionToast.darkColorToast(this,
            "Success",
            "Product Berhasil di tambahkan",
            MotionToastStyle.SUCCESS,
            MotionToast.GRAVITY_BOTTOM,
            MotionToast.LONG_DURATION,
            ResourcesCompat.getFont(this, R.font.helvetica_regular))

        startActivity(Intent(this, HomeActivity::class.java))
    }

    override fun createFailure(message: String) {
        MotionToast.darkColorToast(this,
            "Failed ☹️",
            "Cek Internet anda",
            MotionToastStyle.WARNING,
            MotionToast.GRAVITY_BOTTOM,
            MotionToast.LONG_DURATION,
            ResourcesCompat.getFont(this, R.font.helvetica_regular))
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
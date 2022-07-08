package com.testing.myapplication.signup

import android.app.Dialog
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import androidx.core.content.res.ResourcesCompat
import com.google.gson.Gson
import com.testing.myapplication.R
import com.testing.myapplication.databinding.ActivityRegisterBinding
import com.testing.myapplication.home.HomeActivity
import com.testing.myapplication.response.sign.SignResponse
import com.testing.myapplication.sign.LoginActivity
import com.testing.myapplication.utils.Warungku
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle

class RegisterActivity : AppCompatActivity(),RegisterContract.View {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var presenter: RegisterPresenter
    private var loadingDialog: Dialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initLoading()
        presenter = RegisterPresenter(this)

        binding.btnRegister.setOnClickListener{
            val email = binding.etEmail.text.toString()
            val etPassword = binding.etPassword.text.toString()
            val etpasswordRetry = binding.etpasswordRetry.text.toString()

            when {

                !Patterns.EMAIL_ADDRESS.matcher(email).matches() || email.isEmpty() -> {
                    binding.textInputLayout.error = "Harus email format nya"
                    binding.textInputLayout.requestFocus()
                }

                etPassword.length < 6 -> {
                    binding.textInputLayout1.error = "Isi Password minimal 6 karakter"
                    binding.textInputLayout1.requestFocus()
                }

                etpasswordRetry != etPassword -> {
                    binding.textInputLayout2.error = "Password Harus sama"
                    binding.textInputLayout2.requestFocus()
                }


                else -> {
                    presenter.submitRegis(
                        email,
                        email,
                        etPassword,
                        etpasswordRetry
                    )
                }
            }
        }
    }


    private fun initLoading() {
        loadingDialog = Dialog(this)
        val dialogView = layoutInflater.inflate(R.layout.loading_dialog, null)

        loadingDialog?.let {
            it.setContentView(dialogView)
            it.setCancelable(false)
            it.window?.setBackgroundDrawableResource(R.color.tsp)
        }
    }

    override fun regisSuccess(signResponse: SignResponse) {
        Warungku.getApp().setToken(signResponse.accessToken)
        val gson = Gson()
        val json = gson.toJson(signResponse.user)
        Warungku.getApp().setUser(json)

        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun regisFailed(message: String) {
        MotionToast.createToast(this,
            "Failed 😍",
            "Email harus valid & password Minimal 6 Karakter",
            MotionToastStyle.ERROR,
            MotionToast.GRAVITY_BOTTOM,
            MotionToast.LONG_DURATION,
            ResourcesCompat.getFont(this,R.font.helvetica_regular))
    }

    override fun showLoading(loading: Boolean) {
        when(loading){
            true -> loadingDialog?.show()
            false -> loadingDialog?.dismiss()
        }
    }

    override fun onDestroy() {
        presenter.unSubscribe()
        super.onDestroy()
    }
}
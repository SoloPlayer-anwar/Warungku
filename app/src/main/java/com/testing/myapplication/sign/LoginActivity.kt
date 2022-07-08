package com.testing.myapplication.sign

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import androidx.core.content.res.ResourcesCompat
import com.google.gson.Gson
import com.testing.myapplication.R
import com.testing.myapplication.databinding.ActivityLoginBinding
import com.testing.myapplication.home.HomeActivity
import com.testing.myapplication.response.sign.SignResponse
import com.testing.myapplication.signup.RegisterActivity
import com.testing.myapplication.signup.RegisterPresenter
import com.testing.myapplication.utils.Warungku
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle
import java.util.regex.Pattern

class LoginActivity : AppCompatActivity(), LoginContract.View {
    private lateinit var binding: ActivityLoginBinding
    private var loadingDialog: Dialog? = null
    private lateinit var presenter: LoginPresenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initLoading()
        presenter = LoginPresenter(this)

        binding.register.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

//        if (!Warungku.getApp().getToken().isNullOrEmpty()){
//            val home = Intent(this, HomeActivity::class.java)
//            startActivity(home)
//            finishAffinity()
//        }


        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            when {
                !Patterns.EMAIL_ADDRESS.matcher(email).matches() || email.isEmpty() -> {
                    binding.textInputLayout.error = "Email Harus Valid"
                    binding.textInputLayout.requestFocus()
                }

                password.length < 6 -> {
                    binding.textInputLayout2.error = "Password minimal 6 Karakter"
                    binding.textInputLayout2.requestFocus()
                }

                else -> {
                    presenter.submitLogin(email, password)
                }
            }
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

    override fun loginSuccess(signResponse: SignResponse) {
        Warungku.getApp().setToken(signResponse.accessToken)
        val gson = Gson()
        val json = gson.toJson(signResponse.user)
        Warungku.getApp().setUser(json)

        val home = Intent(this, HomeActivity::class.java)
        startActivity(home)
        finishAffinity()
    }

    override fun loginFailed(message: String) {
        MotionToast.darkColorToast(this,
            "Failed ☹️",
            "Akun belum terdaftar",
            MotionToastStyle.INFO,
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
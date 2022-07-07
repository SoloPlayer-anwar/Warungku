package com.testing.myapplication.sign

import com.testing.myapplication.base.BasePresenter
import com.testing.myapplication.base.BaseView
import com.testing.myapplication.response.sign.SignResponse

interface LoginContract {
    interface View: BaseView {
        fun loginSuccess(signResponse: SignResponse)
        fun loginFailed(message: String)
    }

    interface Presenter: LoginContract, BasePresenter {
        fun submitLogin(email: String, password: String)
    }
}
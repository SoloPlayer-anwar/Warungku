package com.testing.myapplication.signup

import com.testing.myapplication.base.BasePresenter
import com.testing.myapplication.base.BaseView
import com.testing.myapplication.response.sign.SignResponse

interface RegisterContract {
    interface View: BaseView {
        fun regisSuccess(signResponse: SignResponse)
        fun regisFailed(message:String)
    }

    interface Presenter: RegisterContract, BasePresenter {
        fun submitRegis(name:String,
                        email:String,
                        password:String,
                        passwordConfirm:String)
    }
}
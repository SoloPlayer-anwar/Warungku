package com.testing.myapplication.home.create

import android.net.Uri
import com.testing.myapplication.base.BasePresenter
import com.testing.myapplication.base.BaseView
import com.testing.myapplication.response.create.CreateResponse

interface CreateContract {
    interface View: BaseView {
        fun createSuccess(createResponse: CreateResponse)
        fun createFailure(message:String)
    }

    interface Presenter: CreateContract, BasePresenter {
        fun submitCreate(nameWarung:String, alamatWarung:String, lat:Double, long:Double, photo_warung:Uri)
    }
}
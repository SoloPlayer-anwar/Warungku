package com.testing.myapplication.home.update

import android.net.Uri
import com.testing.myapplication.base.BasePresenter
import com.testing.myapplication.base.BaseView
import com.testing.myapplication.response.create.CreateResponse

interface UpdateContract {
    interface View: BaseView {
        fun updateSuccess(createResponse: CreateResponse)
        fun updateFailure(message:String)
    }

    interface Presenter: UpdateContract, BasePresenter {
        fun submitUpdate(id: Int, nameWarung:String, alamatWarung:String, lat:Double, long:Double, photo_warung: Uri)
    }
}
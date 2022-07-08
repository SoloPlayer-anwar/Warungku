package com.testing.myapplication.home

import com.testing.myapplication.base.BasePresenter
import com.testing.myapplication.base.BaseView
import com.testing.myapplication.response.create.CreateResponse
import com.testing.myapplication.response.product.ProductResponse

interface HomeContract {
    interface View: BaseView {
        fun homeSuccess(productResponse: ProductResponse)
        fun homeFailure(message:String)
        fun deleteItem(createResponse: CreateResponse)
    }

    interface Presenter: HomeContract, BasePresenter {
        fun getHome()
        fun submitDelete(id:Int)
    }
}
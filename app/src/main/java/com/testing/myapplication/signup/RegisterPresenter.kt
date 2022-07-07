package com.testing.myapplication.signup

import com.testing.myapplication.httpnetwork.HttpClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class RegisterPresenter(private val view: RegisterContract.View):RegisterContract.Presenter {

    private val mCompositeDisposable: CompositeDisposable?
    init {
        mCompositeDisposable = CompositeDisposable()
    }

    override fun submitRegis(
        name: String,
        email: String,
        password: String,
        passwordConfirm: String
    ) {
        view.showLoading(true)
        val disposable = HttpClient.getInstance().getApi()!!.register(
            name,email,password, passwordConfirm
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                when(it.meta.status.equals("success", true)) {
                    true -> {
                        it.data?.let { it1 -> view.regisSuccess(it1) }
                        view.showLoading(false)
                    }

                    false -> {
                        it.meta.message.let { it1 -> view.regisFailed(it1) }
                        view.showLoading(false)
                    }
                }
            }, {
                view.regisFailed(it.message.toString())
                view.showLoading(false)
            })

        mCompositeDisposable?.add(disposable)
    }

    override fun subscribe() {}

    override fun unSubscribe() {
        mCompositeDisposable?.clear()
    }
}
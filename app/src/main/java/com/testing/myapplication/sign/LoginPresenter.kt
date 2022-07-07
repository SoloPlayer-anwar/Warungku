package com.testing.myapplication.sign

import com.testing.myapplication.httpnetwork.HttpClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class LoginPresenter(private val view: LoginContract.View): LoginContract.Presenter {
    private val mCompositeDisposable: CompositeDisposable?

    init {
        mCompositeDisposable = CompositeDisposable()
    }

    override fun submitLogin(email: String, password: String) {
        view.showLoading(true)
        val disposable = HttpClient.getInstance().getApi()!!.login(
            email, password
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                when(it.meta.status.equals("success", true)) {
                    true -> {
                        it.data?.let { it1 -> view.loginSuccess(it1) }
                        view.showLoading(false)
                    }

                    false -> {
                        it.meta.message?.let { it1 -> view.loginFailed(it1) }
                        view.showLoading(false)
                    }
                }
            }, {
                view.loginFailed(it.message.toString())
                view.showLoading(false)
            })
        mCompositeDisposable?.add(disposable)
    }

    override fun subscribe() {}

    override fun unSubscribe() {
        mCompositeDisposable?.clear()
    }
}
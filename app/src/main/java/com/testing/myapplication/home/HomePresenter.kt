package com.testing.myapplication.home

import com.testing.myapplication.httpnetwork.HttpClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class HomePresenter(private val view: HomeContract.View): HomeContract.Presenter {
    private var mCompositeDisposable : CompositeDisposable? = CompositeDisposable()

    override fun getHome() {
        view.showLoading(true)
        val disposable = HttpClient.getInstance().getApi()!!.getHome()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                when(it.meta.status.equals("success", true)) {
                    true -> {
                        view.homeSuccess(it)
                        view.showLoading(false)
                    }

                    false -> {
                        view.homeFailure(it.meta.message)
                        view.showLoading(false)
                    }
                }
            }, {
                view.homeFailure(it.message.toString())
                view.showLoading(false)
            })

        mCompositeDisposable?.add(disposable)
    }

    override fun submitDelete(id: Int) {
        view.showLoading(true)
        val disposable = HttpClient.getInstance().getApi()!!.deleteProduct(
            id
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                when(it.meta.status.equals("success", true)) {
                    true -> {
                        it.data?.let { it1 -> view.deleteItem(it1) }
                        view.showLoading(false)
                    }

                    false -> {
                        view.homeFailure(it.meta.message)
                        view.showLoading(false)
                    }
                }
            }, {
                view.homeFailure(it.message.toString())
                view.showLoading(false)
            })

        mCompositeDisposable?.add(disposable)
    }

    override fun subscribe() {}

    override fun unSubscribe() {
        mCompositeDisposable?.clear()
    }
}
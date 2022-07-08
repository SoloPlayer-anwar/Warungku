package com.testing.myapplication.home.update

import android.net.Uri
import com.testing.myapplication.httpnetwork.HttpClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class UpdatePresenter(private val view: UpdateContract.View): UpdateContract.Presenter {
    private val mCompositeDisposable: CompositeDisposable?
    init {
        mCompositeDisposable = CompositeDisposable()
    }

    override fun submitUpdate(
        id:Int,
        nameWarung: String,
        alamatWarung: String,
        lat: Double,
        long: Double,
        photo_warung: Uri,
    ) {
        view.showLoading(true)
        val image = File(photo_warung.path)
        val imageRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), image)
        val imageParams = MultipartBody.Part.createFormData("photo_warung", image.name, imageRequestBody)

        val disposable = HttpClient.getInstance().getApi()!!.updateProduct(
            id, nameWarung, alamatWarung, lat, long, imageParams
        )

            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                when(it.meta.status.equals("success", true)) {
                    true -> {
                        it.data?.let { it1 -> view.updateSuccess(it1) }
                        view.showLoading(false)
                    }

                    false -> {
                        view.updateFailure(it.meta.message)
                        view.showLoading(false)
                    }
                }
            }, {
                view.updateFailure(it.message.toString())
                view.showLoading(false)
            })

        mCompositeDisposable?.add(disposable)
    }

    override fun subscribe() {}

    override fun unSubscribe() {
        mCompositeDisposable?.clear()
    }
}
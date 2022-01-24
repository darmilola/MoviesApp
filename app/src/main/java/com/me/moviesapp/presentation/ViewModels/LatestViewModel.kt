package com.me.moviesapp.presentation.ViewModels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.me.moviesapp.data.MainRepository
import com.me.moviesapp.data.MoviesModel
import com.me.moviesapp.data.MoviesResult
import com.me.moviesapp.data.Resource
import com.me.moviesapp.presentation.NetworkUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class LatestViewModel  @Inject constructor(var mainRepository: MainRepository) : ViewModel() {
    var compositeDisposable: CompositeDisposable = CompositeDisposable()
    val latestMovies = MutableLiveData<Resource<MoviesModel>>()



    fun fetchMovies(context: Context) {

        var networkUtil = NetworkUtil()
        var isAvailable = networkUtil.isNetworkAvailable(context)
        if (!isAvailable) {
            latestMovies.postValue(Resource.error("Network not Available",null))
            return
        }


        latestMovies.postValue(Resource.loading(null))
        compositeDisposable.add(
            mainRepository.getRemoteLatestMovies()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({moviesModel: MoviesModel? -> HandleResults(moviesModel!!)

                }, {t: Throwable -> this.HandleError(t)

                })
        )
    }



    fun HandleResults(moviesModel: MoviesModel) {
        latestMovies.postValue(Resource.success(moviesModel))
    }

    private fun HandleError(t: Throwable) {
        latestMovies.postValue(Resource.error(t.localizedMessage, null))
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable!!.dispose()
    }

    fun getLatestMovies(): LiveData<Resource<MoviesModel>> {
        return latestMovies
    }


}
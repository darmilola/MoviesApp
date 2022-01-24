package com.me.moviesapp.presentation.ViewModels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.me.moviesapp.data.MainRepository
import com.me.moviesapp.data.MoviesResult
import com.me.moviesapp.data.Resource
import com.me.moviesapp.presentation.NetworkUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class PopularViewModel @Inject constructor(var mainRepository: MainRepository) : ViewModel() {
    var compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val popularMovies = MutableLiveData<Resource<MoviesResult>>()






    fun fetchMovies(context: Context) {

        var networkUtil = NetworkUtil()
        var isAvailable = networkUtil.isNetworkAvailable(context)
        if (!isAvailable) {
            popularMovies.postValue(Resource.error("Network not Available",null))
            return
        }

        popularMovies.postValue(Resource.loading(null))
        compositeDisposable.add(
            mainRepository.getRemotePopularMovies()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ moviesResult: MoviesResult? -> HandleResults(moviesResult!!)

                }, {t: Throwable -> this.HandleError(t)

                })
        )
    }

    fun fetchMoreMovies(pageNumber: Int) {
        compositeDisposable.add(
            mainRepository.getMoreRemotePopularMovies(pageNumber)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ moviesResult: MoviesResult? -> HandleResults(moviesResult!!)

                }, {t: Throwable -> this.HandleError(t)

                })
        )
    }



    fun HandleResults(moviesResult: MoviesResult) {
        popularMovies.postValue(Resource.success(moviesResult))
    }

    private fun HandleError(t: Throwable) {
        popularMovies.postValue(Resource.error(t.localizedMessage, null))
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable!!.dispose()
    }

    fun getPopularMovies(): LiveData<Resource<MoviesResult>> {
        return popularMovies
    }


}
package com.me.moviesapp.presentation.ViewModels

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.room.Room
import com.me.moviesapp.data.Database
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
class  UpcomingViewModel @Inject constructor(var mainRepository: MainRepository) : ViewModel() {
    var compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val upcomingMovies = MutableLiveData<Resource<MoviesResult>>()


    init {


    }

    fun fetchMovies(context: Context) {

        var db: Database = Room.databaseBuilder(
            context,
            Database::class.java, "upcomingMovies"
        ).build()


        var networkUtil = NetworkUtil()
        var isAvailable = networkUtil.isNetworkAvailable(context)
        if (!isAvailable) {
            upcomingMovies.postValue(Resource.error("Network not Available",null))
          /*  compositeDisposable.add(
                db.upcomingMovieDao().getAll()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ moviesResult: MoviesResult? -> HandleResults(moviesResult!!)

                    }, {t: Throwable -> this.HandleError(t)

                    })
            )*/
            return
        }

        upcomingMovies.postValue(Resource.loading(null))
        compositeDisposable.add(
            mainRepository.getRemoteUpcomingMovies()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ moviesResult: MoviesResult? -> HandleResults(moviesResult!!)

                }, {t: Throwable -> this.HandleError(t)

                })
        )
    }

    fun fetchMoreMovies(pageNumber: Int) {
        compositeDisposable.add(
            mainRepository.getMoreRemoteUpcomingMovies(pageNumber)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ moviesResult: MoviesResult? -> HandleResults(moviesResult!!)

                }, {t: Throwable -> this.HandleError(t)

                })
        )
    }



    fun HandleResults(moviesResult: MoviesResult) {
        Log.e("HandleResults: ",moviesResult.toString())
        upcomingMovies.postValue(Resource.success(moviesResult))
    }

    private fun HandleError(t: Throwable) {
        upcomingMovies.postValue(Resource.error(t.localizedMessage, null))
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable!!.dispose()
    }

    fun getUpcomingMovies(): LiveData<Resource<MoviesResult>> {
        return upcomingMovies
    }


}
package com.me.moviesapp.presentation.ViewModels

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.room.Room
import com.me.moviesapp.data.*
import com.me.moviesapp.data.Entity.UpcomingMoviesEntity
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

    fun fetchMovies(context: Context) {

        var networkUtil = NetworkUtil()
        var isAvailable = networkUtil.isNetworkAvailable(context)
        if (!isAvailable) {
            upcomingMovies.postValue(Resource.error("Network not Available",null))
            compositeDisposable.add(
                mainRepository.getLocalUpcomingMovies(context)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ localResults: List<UpcomingMoviesEntity> -> HandLocalResults(localResults)

                    }, {t: Throwable -> this.HandleError(t)

                    })
            )
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

    fun HandLocalResults(localResults: List<UpcomingMoviesEntity>) {
        var moviesList: MutableList<MoviesModel> = arrayListOf()
        for (movieEntity in localResults){
            var movieModel = MoviesModel(movieEntity.id,movieEntity.title,movieEntity.overview,movieEntity.voteAvearge,movieEntity.posterPath,movieEntity.releaseDate)
             moviesList.add(movieModel)
        }
        var movieResult = MoviesResult(0,0, moviesList as ArrayList<MoviesModel>)

        upcomingMovies.postValue(Resource.success(movieResult))
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
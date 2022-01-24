package com.me.moviesapp.data.DaggerModule

import com.me.moviesapp.data.ApiHelper
import com.me.moviesapp.data.MainRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ViewModelComponent
import io.reactivex.disposables.CompositeDisposable

@Module
@InstallIn(ActivityComponent::class, ViewModelComponent::class)
class RepositoryModule {
    @Provides
    fun provideRepository(): MainRepository {
        return MainRepository(ApiHelper())
    }
}
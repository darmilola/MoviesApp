package com.me.moviesapp.data.DaggerModule

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ViewModelComponent
import io.reactivex.disposables.CompositeDisposable

@Module
@InstallIn(ActivityComponent::class, ViewModelComponent::class)
class CompositeDisposableModule {
    @Provides
    fun provideCompositeDisposable(): CompositeDisposable {
        return CompositeDisposable()
    }
}


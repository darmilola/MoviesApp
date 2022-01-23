package com.me.moviesapp.data.DaggerModule

import com.me.moviesapp.domain.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ViewModelComponent
import retrofit2.Retrofit
import javax.inject.Inject

@Module
@InstallIn(ViewModelComponent::class, ActivityComponent::class)
class ApiModule {
    @Provides
    @Inject
    fun provideAPI(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}
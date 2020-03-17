package com.hyejineee.musicapp.util

import com.hyejineee.musicapp.data.dataSource.RemoteSongInfoDataSource
import com.hyejineee.musicapp.data.dataSource.serviceAPI.FloServiceAPI
import com.hyejineee.musicapp.data.repository.SongInfoRepository
import com.hyejineee.musicapp.data.repository.SongInfoRepositoryImpl
import com.hyejineee.musicapp.mVIewModel.SongInfoViewModel
import okhttp3.OkHttpClient
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

fun getNetworkModule(baseUrl: String) = module {
    single {
        OkHttpClient.Builder()
            .writeTimeout(5000, TimeUnit.MILLISECONDS)
            .readTimeout(5000, TimeUnit.MILLISECONDS)
            .build()
    }

    single {
        GsonConverterFactory.create() as Converter.Factory
    }

    single {
        Retrofit.Builder()
            .client(get())
            .addConverterFactory(get())
            .baseUrl(baseUrl)
            .build()
    }
}

val data = module {
    single<SongInfoRepository> { SongInfoRepositoryImpl() }
    factory { RemoteSongInfoDataSource() }
}

val apiModules = module {
    single { FloServiceAPI(get()) }
}

val songInfoViewModel = module {
    viewModel { SongInfoViewModel(get()) }
}

val floModules = listOf(
    getNetworkModule("https://grepp-programmers-challenges.s3.ap-northeast-2.amazonaws.com"),
    apiModules,
    data,
    songInfoViewModel
)

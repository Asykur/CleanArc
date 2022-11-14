package com.asykur.core.di

import androidx.room.Room
import com.asykur.core.BuildConfig
import com.asykur.core.data.repository.IMovieRepository
import com.asykur.core.data.source.MovieRepository
import com.asykur.core.data.source.local.LocalDataSource
import com.asykur.core.data.source.local.room.MovieDatabase
import com.asykur.core.data.source.remote.ApiServices
import com.asykur.core.data.source.remote.RemoteDataSource
import com.asykur.core.domain.usecase.MovieInteractor
import com.asykur.core.domain.usecase.MovieUseCase
import com.asykur.core.utils.AppExecutor
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val networkModule = module {
    single {
        val certificate = CertificatePinner.Builder()
            .add(BuildConfig.HOST_NAME, BuildConfig.CERTIFICATE_PINNING)
            .build()

        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .certificatePinner(certificate)
            .build()
    }

    single {
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
        retrofit.create(ApiServices::class.java)

    }
}

val databaseModule = module {
    factory { get<MovieDatabase>().movieDao() }
    val factory = SupportFactory(SQLiteDatabase.getBytes(BuildConfig.ROOM_PWD.toCharArray()))
    single {
        Room.databaseBuilder(
            androidContext(),
            MovieDatabase::class.java, "Movie.db"
        ).fallbackToDestructiveMigration()
            .openHelperFactory(factory)
            .build()
    }
}

val repositoryModule = module {
    single { LocalDataSource(get()) }
    single { RemoteDataSource(get()) }
    factory { AppExecutor() }
    single<IMovieRepository> { MovieRepository(get(), get(), get()) }
}

val useCaseModule = module {
    factory<MovieUseCase> { MovieInteractor(get()) }
}


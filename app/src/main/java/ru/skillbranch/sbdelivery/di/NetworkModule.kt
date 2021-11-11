package ru.skillbranch.sbdelivery.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.ToJson
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import ru.skillbranch.sbdelivery.AppConfig
import ru.skillbranch.sbdelivery.data.network.RestService
import javax.inject.Singleton
import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonQualifier
import java.sql.Timestamp
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


//@Retention(AnnotationRetention.RUNTIME)
//@JsonQualifier
//annotation class TextDate

object CustomDateAdapter {
    var dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.", Locale.US)

    @ToJson
    @Synchronized
    fun dateToJson(d: Long): String {
        return dateFormat.format(d)
    }

    @FromJson
    @Synchronized
    fun dateFromJson(
        s: String): Long {
        return dateFormat.parse(s)?.time ?: 0
    }
}

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {
    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor() : HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    @Provides
    @Singleton
    fun provideRestService(retrofit: Retrofit): RestService =
        retrofit.create(RestService::class.java)


    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient, moshi: Moshi): Retrofit = Retrofit.Builder()
        .client(client) //set http client
        .addConverterFactory(MoshiConverterFactory.create(moshi)) //set json converter/parser
        .baseUrl(AppConfig.BASE_URL)
        .build()

    @Provides
    @Singleton
    fun provideOkHttpClient(
        logging: HttpLoggingInterceptor
    ): OkHttpClient = OkHttpClient().newBuilder()
        .addInterceptor(logging) //intercept req/res for logging
        .build()



    @Provides
    @Singleton
    fun provideMoshi() : Moshi =  Moshi.Builder()
        .add(CustomDateAdapter)
        .addLast(KotlinJsonAdapterFactory())
        .build()
}
package com.droidafricana.globalmail.service.network

import com.droidafricana.globalmail.service.model.ArticleResponse
import com.droidafricana.globalmail.utils.Constants
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface ArticleApiService {
    @GET("/v2/{endPoint}")
    fun getArticleListAsync(@Path("endPoint") endPoint: String?,
                            @Query("q") query: String?,
                            @Query("category") category: String?,
                            @Query("country") country: String?,
                            @Query("pageSize") pageSize: Int,
                            @Query("sortBy") sortBy: String?,
                            @Query("apiKey") apiKey: String): Deferred<ArticleResponse>
}

/**
 * Build the Moshi object that Retrofit will be using, making sure to add the Kotlin adapter for
 * full Kotlin compatibility.
 */
private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

/*OkHttp for basic retrofit setup*/
//TODO: Get rid of this logging
val interceptor: HttpLoggingInterceptor =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

val client: OkHttpClient = OkHttpClient.Builder()
        .callTimeout(30, TimeUnit.SECONDS)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .addInterceptor(interceptor)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()
/**
 * Use the Retrofit builder to build a retrofit object using a Moshi converter with our Moshi
 * object.
 */
private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .baseUrl(Constants.BASE_URL)
        .client(client)
        .build()

/**
 * A public Api object that exposes the lazy-initialized Retrofit service
 */
object ArticleApi {
    val retrofitService: ArticleApiService by lazy { retrofit.create(ArticleApiService::class.java) }
}

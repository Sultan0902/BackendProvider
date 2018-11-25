package com.sultan.utils.backendprovider.provider

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.sultan.utils.backendprovider.model.BackendLogLevel
import com.sultan.utils.backendprovider.utility.JsonUtitlity
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import org.jetbrains.annotations.NotNull
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 * Created by Sultan Ahmed on 22/11/2018.
 */

/*
* A wrapper library for Retrofit. It can be used to handle the Backend API responses
* and map their value to the variables accordingly using JSON mapper or any other.
* By default, it contains the Http Interceptor which has a JSON mapper.
* However user can add a custom interceptor to handle the response and mapp them accordingly as well.*/

/*T is type of interface which will be used to create the retrofit object*/
open class BackendProvider<T>(private val context: Context, private val apiClass: Class<T>) {
    private var baseUrl = ""

    private var backendConnectionTimeout = 30
    private var backendReadTimeout = 30
    private var backendWriteTimeout = 30

    private var internetError = "No internet connection"
    private var backendConnectivityError = "Unable to connect. Some error occured"
    private var backendLogLevel = BackendLogLevel.NONE

    private var backendApi: T? = null
    private val backendGson: Gson
    private val jsonUtitlity: JsonUtitlity

    init {
        backendGson = GsonBuilder().create()
        jsonUtitlity = JsonUtitlity()
    }

    /*
    * Interceptor to handle the backend response. map the values using JSON
    * */
    protected val backendCallsInterceptor = Interceptor { chain ->
        try {
            val originalResponse = chain.proceed(chain.request())
            var response = jsonUtitlity.fromJson(originalResponse.body()!!.string(), JsonObject::class.java)
            Log.i(TAG, response.toString())

            if (response == null) {
                response = jsonUtitlity.fromJson("{}", JsonObject::class.java)
            }

            response.addProperty("code", originalResponse.code())
            response.addProperty("isSuccess", originalResponse.isSuccessful())
            response.addProperty("message", originalResponse.message())

            val responseBody = ResponseBody.create(originalResponse.body()!!.contentType(), response.toString())

            originalResponse.newBuilder().body(responseBody).build()
        } catch (e: Exception) {
            Log.e(TAG, e.message)
            val isConnected = isIntenetConnected
            throw IOException(if (isConnected) backendConnectivityError else internetError)
        }
    }

    /*
    * Check if the internet is connected or not on the device
    * */
    private val isIntenetConnected: Boolean
        get() {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = cm.activeNetworkInfo
            return activeNetwork != null && activeNetwork.isConnectedOrConnecting
        }

    /*
    * map HttpLoggingInterceptor.Level ENUM values to BackendLogLevel ENUM
    * */
    private val loggingInterceptor: HttpLoggingInterceptor.Level
        get() {
            when (backendLogLevel) {
                BackendLogLevel.BODY -> return HttpLoggingInterceptor.Level.BODY
                BackendLogLevel.HEADERS -> return HttpLoggingInterceptor.Level.HEADERS
                BackendLogLevel.BASIC -> return HttpLoggingInterceptor.Level.BASIC
                BackendLogLevel.NONE -> return HttpLoggingInterceptor.Level.NONE
                else -> return HttpLoggingInterceptor.Level.NONE
            }
        }

    /*
    * Set the value of baseUrl for retrofit.
    * ByDefault value is ""
    * @param baseUrl : String
    * */
    fun setBaseUrl(@NotNull baseUrl: String): BackendProvider<T> {
        this.baseUrl = baseUrl
        return this
    }

    /*
    * Set the value of connectionTimeout for retrofit.
    * ByDefault value is 30 sec
    * @param timout: Int value of timout
    * */
    fun setConnectionTimeout(timeout: Int): BackendProvider<T> {
        this.backendConnectionTimeout = timeout
        return this
    }

    /*
    * Set the value of backendReadTimout for retrofit.
    * ByDefault value is 30 sec
    * @param timout: Int value of timout
    * */
    fun setReadTimeout(timeout: Int): BackendProvider<T> {
        this.backendReadTimeout = timeout
        return this
    }

    /*
    * Set the value of backendWritetimout for retrofit.
    * ByDefault value is 30 sec
    * @param timout: Int value of timout
    * */
    fun setWriteTimeout(timeout: Int): BackendProvider<T> {
        this.backendWriteTimeout = timeout
        return this
    }

    /*
    * Set the value of backendConnectivityError for interceptor.
    * ByDefault value is Unable to connect. Some error occured
    * @param backendConnectivityError: String value of backendConnectivityError
    * */
    fun setConnectivityError(backendConnectivityError: String): BackendProvider<T> {
        this.backendConnectivityError = backendConnectivityError
        return this
    }

    /*
    * Set the value of internetError for interceptor.
    * ByDefault value is No internet connection
    * @param internetError: String value of internetError
    * */
    fun setInternetError(internetError: String): BackendProvider<T> {
        this.internetError = internetError
        return this
    }

    /*
    * Set the value of backendLogLevel for loggin interceptor.
    * ByDefault value is BackendLogLevel.NONE
    * @param backendLogLevel: BackendLogLevel value of log level
    * */
    fun setLogLevel(backendLogLevel: BackendLogLevel): BackendProvider<T> {
        this.backendLogLevel = backendLogLevel
        return this
    }

    /*
       * fun initHttpClient()  used to provide the okHttpClient. It will use default @backendCallsInterceptor
       * and default HttpLogginInterceptor
       *
       * */
    private fun initHttpClient(): OkHttpClient {
        Log.i(TAG, "initHttpClient")

        val retrofitClientBuilder = OkHttpClient.Builder()
        val logging = HttpLoggingInterceptor()
        logging.level = loggingInterceptor

        retrofitClientBuilder.addInterceptor(logging)
        retrofitClientBuilder.addInterceptor(backendCallsInterceptor)

        retrofitClientBuilder.writeTimeout(backendWriteTimeout.toLong(), TimeUnit.SECONDS)
        retrofitClientBuilder.readTimeout(backendReadTimeout.toLong(), TimeUnit.SECONDS)
        retrofitClientBuilder.connectTimeout(backendConnectionTimeout.toLong(), TimeUnit.SECONDS)
        return retrofitClientBuilder.build()
    }

    /*
    * fun initHttpClient(interceptors: List<Interceptor>)  used to provide the okHttpClient. It will
    * add the customer interceptors in the client what user actually need
    * @Param interceptors custom interceptors passed by user
    *
    * */
    private fun initHttpClient(interceptors: List<Interceptor>): OkHttpClient {
        Log.i(TAG, "initHttpClient")

        val retrofitClientBuilder = OkHttpClient.Builder()
        val logging = HttpLoggingInterceptor()
        logging.level = loggingInterceptor

        retrofitClientBuilder.addInterceptor(logging)
        for (interceptor in interceptors) {
            retrofitClientBuilder.addInterceptor(interceptor)
        }

        retrofitClientBuilder.writeTimeout(backendWriteTimeout.toLong(), TimeUnit.SECONDS)
        retrofitClientBuilder.readTimeout(backendReadTimeout.toLong(), TimeUnit.SECONDS)
        retrofitClientBuilder.connectTimeout(backendConnectionTimeout.toLong(), TimeUnit.SECONDS)
        return retrofitClientBuilder.build()
    }

    /*
    * fun build()  used to create the backend api class using retrofit.
    * It will add the customer interceptors in the okHttpclient what user actually need
    * */
    fun build(): T {
        val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(initHttpClient())
                .addConverterFactory(GsonConverterFactory.create(backendGson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        backendApi = retrofit.create(apiClass)
        return backendApi!!
    }

    /*
     * fun build(interceptors: List<Interceptor>)  used to create the backend api class using retrofit.
     * It will add the customer interceptors in the okHttpclient what user actually need
    * @Param interceptors custom interceptors passed by user
    *
   * */
    fun build(interceptors: List<Interceptor>): T {
        val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(initHttpClient(interceptors))
                .addConverterFactory(GsonConverterFactory.create(backendGson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        backendApi = retrofit.create(apiClass)
        return backendApi!!
    }

    companion object {
        val TAG = BackendProvider::class.java.name
    }
}

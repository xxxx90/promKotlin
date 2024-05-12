package ru.netology.nmedia.api

import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import ru.netology.nmedia.dto.Post
import java.util.concurrent.TimeUnit

private const val BASE_URL= "http://10.0.2.2:9999/api/slow/"


private val retrofit = Retrofit.Builder()
    .client(
        OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .build()
    )
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

interface PostApi {
    @GET("posts")
   fun getAll(): Call<List<Post>>

   @POST("posts")
   fun save(@Body post: Post): Call <Post>

   @POST("posts/{id}/likes")
   fun likeById(@Path("id") id: Long): Call <Post>

    @DELETE("posts/{id}/likes")
    fun unlikeById(@Path("id") id: Long): Call <Post>

    @DELETE("posts/{id}")
    fun deleteById(@Path("id") id: Long): Call <Unit>
}

object ApiService {
    val service:PostApi by lazy {
        retrofit.create()
    }

}
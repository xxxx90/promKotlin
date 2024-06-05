package ru.netology.nmedia.api

import okhttp3.OkHttpClient
import retrofit2.Response
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
  suspend fun getAll(): Response <List<Post>>

   @POST("posts")
  suspend fun save(@Body post: Post): Post

   @POST("posts/{id}/likes")
  suspend fun likeById(@Path("id") id: Long): Response <Post>

    @DELETE("posts/{id}/likes")
  suspend  fun unlikeById(@Path("id") id: Long):Response <Post>

    @DELETE("posts/{id}")
   suspend fun deleteById(@Path("id") id: Long): Response <Unit>
}

object ApiService {
    val service:PostApi by lazy {
        retrofit.create<PostApi>()
    }

}
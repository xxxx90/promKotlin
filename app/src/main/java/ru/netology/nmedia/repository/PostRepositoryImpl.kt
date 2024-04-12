package ru.netology.nmedia.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import ru.netology.nmedia.dto.Post
import java.io.IOException
import java.lang.Exception
import java.util.concurrent.TimeUnit


class PostRepositoryImpl : PostRepository {
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .build()
    private val gson = Gson()
    private val typeToken = object : TypeToken<List<Post>>() {}

    companion object {
        private const val BASE_URL = "http://10.0.2.2:9999"
        private val jsonType = "application/json".toMediaType()
    }

    override fun getAll(callback: PostRepository.GetAllCallback) {
        val request: Request = Request.Builder()
            .url("${BASE_URL}/api/slow/posts")
            .build()

        return client.newCall(request)
            .enqueue(
               object : Callback {
                   override fun onFailure(call: Call, e: IOException) {
                       callback.onError(e)
                   }

                   override fun onResponse(call: Call, response: Response) {
                       val responseBody = response.body?.string()

                       if (!response.isSuccessful){
                           callback.onError(RuntimeException(responseBody))
                           return
                       }

                       try {
                          callback.onSuccess(gson.fromJson(responseBody, typeToken))
                       } catch (e: Exception){
                           callback.onError(e)
                       }
                   }

               }
            )
    }



    override fun likeById(post: Post): Post {
        val request = if (post.likedByMe) {
            Request.Builder()
                .delete() // <--- DELETE запрос
                .url("${BASE_URL}/api/posts/${post.id}/likes")
                .build()

        } else {
            Request.Builder()
                .url("${BASE_URL}/api/posts/${post.id}/likes")
                .post(
                    gson.toJson(post, Post::class.java).toRequestBody(jsonType)
                ) // <---- POST запрос
                .build()
        }

        return client.newCall(request)
            .execute()
            .let { it.body?.string() ?: throw RuntimeException("body is null") }
            .let {
                gson.fromJson(it, Post::class.java)
            }


    }

    override fun save(post: Post) {
        val request: Request = Request.Builder()
            .post(gson.toJson(post).toRequestBody(jsonType))
            .url("${BASE_URL}/api/slow/posts")
            .build()

        client.newCall(request)
            .execute()
            .close()
    }

    override fun removeById(id: Long) {
        val request: Request = Request.Builder()
            .delete()
            .url("${BASE_URL}/api/slow/posts/$id")
            .build()

        client.newCall(request)
            .execute()
            .close()
    }
}

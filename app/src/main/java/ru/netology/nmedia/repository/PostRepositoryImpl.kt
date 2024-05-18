package ru.netology.nmedia.repository


import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.netology.nmedia.api.ApiService
import ru.netology.nmedia.dto.Post


class PostRepositoryImpl : PostRepository {

    override fun getAll(callback: PostRepository.GetAllCallback) {
        return ApiService.service.getAll()
            .enqueue(object : Callback<List<Post>> {
                override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                    if (!response.isSuccessful) {
                        callback.onError(RuntimeException("Code: ${response.code()}; Message: ${response.message()}"))
                        getAll(callback)
                    }

                    val list = response.body()
                        ?: return callback.onError(RuntimeException("Code: ${response.code()}; Message: ${response.message()}"))
                    callback.onSuccess(list)
                }

                override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                    callback.onError(RuntimeException("Message: ${t.message}"))
                }
            })
    }


    override fun likeById(post: Post, callback: PostRepository.LikeBiIdCallback) {
        if (post.likedByMe) {
            return ApiService.service.unlikeById(post.id)
                .enqueue(object : Callback<Post> {

                    override fun onResponse(call: Call<Post>, response: Response<Post>) {
                        if (!response.isSuccessful) {
                            callback.onError(RuntimeException("Code: ${response.code()}; Message: ${response.message()}"))

                        }

                        val post = response.body()
                            ?: return callback.onError(RuntimeException("Code: ${response.code()}; Message: ${response.message()}"))
                        callback.onSuccess(post)

                    }

                    override fun onFailure(call: Call<Post>, t: Throwable) {
                        callback.onError(RuntimeException("Message: ${t.message}"))
                    }
                })

        } else {
            return ApiService.service.likeById(post.id)
                .enqueue(object : Callback<Post> {

                    override fun onResponse(call: Call<Post>, response: Response<Post>) {
                        if (!response.isSuccessful) {
                            callback.onError(RuntimeException("Code: ${response.code()}; Message: ${response.message()}"))

                        }

                        val post = response.body()
                            ?: return callback.onError(RuntimeException("Code: ${response.code()}; Message: ${response.message()}"))
                        callback.onSuccess(post)
                    }

                    override fun onFailure(call: Call<Post>, t: Throwable) {
                        callback.onError(RuntimeException("Message: ${t.message}"))
                    }
                })


        }


    }

    override fun save(post: Post, callback: PostRepository.SavePostCallback) {
        ApiService.service.save(post).enqueue(object : Callback<Post> {


            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                if (!response.isSuccessful) {
                    callback.onError(RuntimeException("Code: ${response.code()}; Message: ${response.message()}"))
                    save(post, callback)
                }
                val list = response.body()
                    ?: return callback.onError(RuntimeException("Code: ${response.code()}; Message: ${response.message()}"))
                callback.onSuccess(list)
            }

            override fun onFailure(call: Call<Post>, t: Throwable) {
                callback.onError(RuntimeException("Message: ${t.message}"))
            }
        })


    }

    override fun removeById(id: Long, callback: PostRepository.RemotePostCallback) {
        return ApiService.service.deleteById(id)
            .enqueue(object : Callback<Unit> {

                override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                    if (!response.isSuccessful) {
                        callback.onError(RuntimeException("Code: ${response.code()}; Message: ${response.message()}"))
                    }

                    val list = response.body()
                        ?: return callback.onError(RuntimeException("Code: ${response.code()}; Message: ${response.message()}"))
                    callback.onSuccess(list)
                }

                override fun onFailure(call: Call<Unit>, t: Throwable) {
                    callback.onError(RuntimeException("Message: ${t.message}"))
                }
            })
    }


}





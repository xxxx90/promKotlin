package ru.netology.nmedia.repository


import okhttp3.Callback
import ru.netology.nmedia.dto.Post
import java.lang.Exception

interface PostRepository {
    fun getAll(callback: GetAllCallback)
    fun likeById(id:Long, callback: LikeBiIdCallback )
   // fun likeById(callback: LikeBiIdCallback): Post
    fun save(post: SavePostCallback)
    fun removeById(id: Long)

    interface GetAllCallback {
        fun onSuccess (posts: List<Post>)
        fun onError (exception: Exception)

    }

    interface LikeBiIdCallback {
        fun onSuccess (posts: Post):Post
        fun onError (exception: Exception)

    }

    interface SavePostCallback {
        fun onSuccess (post: Post)
        fun onError (exception: Exception)

    }
}

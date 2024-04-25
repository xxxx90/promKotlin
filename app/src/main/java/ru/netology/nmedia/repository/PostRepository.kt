package ru.netology.nmedia.repository


import okhttp3.Response
import ru.netology.nmedia.dto.Post
import java.lang.Exception

interface PostRepository {
    fun getAll(callback: GetAllCallback)
    fun likeById(post: Post, callback: LikeBiIdCallback )
   // fun likeById(callback: LikeBiIdCallback): Post
    fun save(post: Post, callback: SavePostCallback)
    fun removeById(id: Long, callback: RemotePostCallback)

    interface GetAllCallback {
        fun onSuccess (posts: List<Post>)
        fun onError (exception: Exception)

    }

    interface LikeBiIdCallback {
        fun onSuccess (posts: Post):Post
        fun onError (exception: Exception)

    }

    interface SavePostCallback {
        fun onSuccess (posts: Post)
        fun onError (exception: Exception)

    }

    interface RemotePostCallback {
        fun onSuccess (response: Unit)
        fun onError (exception: Exception)

    }
}

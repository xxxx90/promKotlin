package ru.netology.nmedia.repository


import ru.netology.nmedia.dto.Post
import java.lang.Exception

interface PostRepository {
    fun getAll(callback: GetAllCallback)
    fun likeById(post: Post): Post
    fun save(post: Post)
    fun removeById(id: Long)

    interface GetAllCallback {
        fun onSuccess (posts: List<Post>)
        fun onError (exception: Exception)

    }
}

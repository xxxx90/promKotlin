package ru.netology.nmedia.repository

import ru.netology.nmedia.api.ApiService
import ru.netology.nmedia.dto.Post


class PostRepositoryImpl : PostRepository {

    override fun getAll(callback: PostRepository.GetAllCallback) {


        return ApiService.service.getAll()
            .execute()
            .let { it.body() ?: throw RuntimeException("body is null") }
    }


    override fun likeById(post: Post, callback: PostRepository.LikeBiIdCallback) {
        if (post.likedByMe) {
            ApiService.service.unlikeById(post.id)
        } else {
            ApiService.service.likeById(post.id)
        }


    }

    override fun save(post: Post, callback: PostRepository.SavePostCallback) {
        ApiService.service.save(post).execute()
    }

    override fun removeById(id: Long, callback: PostRepository.RemotePostCallback) {
        ApiService.service.deleteById(id).execute()


    }
}





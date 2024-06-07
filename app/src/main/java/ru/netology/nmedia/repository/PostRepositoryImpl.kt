package ru.netology.nmedia.repository


import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import okio.IOException
import ru.netology.nmedia.api.ApiService
import ru.netology.nmedia.dao.PostDao
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.entity.toDto
import ru.netology.nmedia.entity.toEntity
import ru.netology.nmedia.error.ApiError
import ru.netology.nmedia.error.NetworkException
import ru.netology.nmedia.error.UnknownException


class PostRepositoryImpl(private val dao: PostDao) : PostRepository {
    override val data: LiveData<List<Post>> = dao.getAll().map { it.toDto() }


    @SuppressLint("SuspiciousIndentation")
    override suspend fun getAll() {

        try {
            val response = ApiService.service.getAll()
            if (!response.isSuccessful) throw ApiError(response.code())

            val posts = response.body() ?: throw UnknownException
            dao.insert(posts.toEntity())

        } catch (e: IOException) {
            throw NetworkException
        } catch (e: ApiError) {
            throw e
        } catch (e: Exception) {
            throw UnknownException
        }




    }

    override suspend fun likeById(id: Long) {

        try {
            val response = ApiService.service.likeById(id)
            if (!response.isSuccessful) throw ApiError(response.code())

            val post = response.body() ?: throw UnknownException
            dao.likeById(id)

        } catch (e: IOException) {
            throw NetworkException
        } catch (e: ApiError) {
            throw e
        } catch (e: Exception) {
            throw UnknownException
        }


    }

    override suspend fun save(post: Post) {


        try {
            val response = ApiService.service.save(post)
            if (!response.isSuccessful) throw ApiError(response.code())

            val posts = response.body() ?: throw UnknownException
        //    dao.insert(posts.toEntity())

        } catch (e: IOException) {
            throw NetworkException
        } catch (e: ApiError) {
            throw e
        } catch (e: Exception) {
            throw UnknownException
        }



    }

    override suspend fun removeById(id: Long) {
        try {
            val response = ApiService.service.deleteById(id)
            if (!response.isSuccessful) throw ApiError(response.code())

            dao.removeById(id)

        } catch (e: IOException) {
            throw NetworkException
        } catch (e: ApiError) {
            throw e
        } catch (e: Exception) {
            throw UnknownException
        }


    }

}





package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.model.FeedModel
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryImpl
import ru.netology.nmedia.util.SingleLiveEvent
import java.io.IOException

private val empty = Post(
    id = 0,
    content = "",
    author = "",
    authorAvatar = "",
    likedByMe = false,
    likes = 0,
    published = ""
)

class PostViewModel(application: Application) : AndroidViewModel(application) {
    // упрощённый вариант
    private val repository: PostRepository = PostRepositoryImpl()
    private val _data = MutableLiveData(FeedModel())
    val data: LiveData<FeedModel>
        get() = _data
    val edited = MutableLiveData(empty)
    private val _postCreated = SingleLiveEvent<Unit>()
    val postCreated: LiveData<Unit>
        get() = _postCreated

    private val _error = SingleLiveEvent<Unit>()
    val error: LiveData<Unit>
        get() = _postCreated


    init {
        loadPosts()
    }

    fun loadPosts() {

        // Начинаем загрузку
        _data.value = FeedModel(loading = true)

        // Данные успешно получены
        repository.getAll(
            object : PostRepository.GetAllCallback {
                override fun onSuccess(posts: List<Post>) {
                    _data.postValue(FeedModel(posts = posts, empty = posts.isEmpty()))
                }

                override fun onError(exception: Exception) {
                    _data.postValue(FeedModel(error = true))
                }
            }
        )
    }

    fun save() {
        _data.postValue(_data.value?.copy(loading = true))
        val currentPost = edited.value ?: return
        edited.postValue(empty)
        repository.save(currentPost,
            object : PostRepository.SavePostCallback {
                override fun onSuccess(post: Post) {
                    _data.postValue(
                        _data.value?.copy(
                            posts = _data.value?.posts.orEmpty().let { list ->
                                if (list.none { it.id == post.id }) {
                                    listOf(post) + list
                                } else {
                                    list.map {
                                        if (it.id == post.id) post else it
                                    }
                                }
                            }, loading = false
                        )
                    )
                    _postCreated.postValue(Unit)
                }

                override fun onError(exception: Exception) {
                    _data.postValue(FeedModel(error = true))
                    _error.value=Unit
                }

            }
        )
    }


    fun edit(post: Post) {
        edited.value = post
    }

    fun changeContent(content: String) {
        val text = content.trim()
        if (edited.value?.content == text) {
            return
        }
        edited.value = edited.value?.copy(content = text)
    }


    fun likeById(post: Post) {
        repository.likeById(post, object : PostRepository.LikeBiIdCallback {
            override fun onSuccess(postServ: Post): Post {
                _data.postValue(
                    _data.value?.copy(posts = _data.value?.posts.orEmpty().map {
                        if (it.id == postServ.id)
                            postServ else it
                    }
                    )
                )
                return post
            }

            override fun onError(exception: Exception) {
                // do nothing
            }

        })
    }


    fun removeById(id: Long) {

        // Оптимистичная модель
        val old = _data.value?.posts.orEmpty()
        _data.postValue(
            _data.value?.copy(posts = _data.value?.posts.orEmpty()
                .filter { it.id != id }
            )
        )
        try {
            repository.removeById(id, object : PostRepository.RemotePostCallback {
                override fun onSuccess(response: Unit) {

                }

                override fun onError(exception: Exception) {
                    _data.postValue(_data.value?.copy(posts = old))
                }


            })
        } catch (e: IOException) {
            _data.postValue(_data.value?.copy(posts = old))
        }
        //  }
    }
}


package com.besirkaraoglu.newsapp.ui

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.besirkaraoglu.newsapp.data.NewsRepository
import com.besirkaraoglu.newsapp.model.Article
import com.besirkaraoglu.newsapp.model.ArticleDbEntity
import com.besirkaraoglu.newsapp.model.News
import com.besirkaraoglu.newsapp.util.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel
@Inject constructor(
    private val repository: NewsRepository
    ) : ViewModel() {
    private val TAG = "NewsViewModel"

    private val _dataState : MutableLiveData<DataState<News>> = MutableLiveData()
    val dataState : LiveData<DataState<News>>
        get() = _dataState

    private val _favState : MutableLiveData<DataState<List<ArticleDbEntity>>> = MutableLiveData()
    val favState : LiveData<DataState<List<ArticleDbEntity>>>
        get() = _favState

    private val currentQuery : Flow<String> = flowOf()



    @ExperimentalCoroutinesApi
    fun getResults(query: String, pageSize: Int, page: Int) = viewModelScope.launch(Dispatchers.IO) {
        repository.getResults(query, pageSize, page)
            .catch {
                dataState -> _dataState.value = DataState.Error(dataState)
            }.collect {
                dataState -> _dataState.value = dataState
            }
    }


    suspend fun getSearchResults(query: String): Flow<PagingData<Article>> {
        return repository.getSearchResults(query).cachedIn(viewModelScope)
    }

    fun getFavorites() = viewModelScope.launch {
        repository.getFavorites()
            .catch {
                    favState -> _favState.value = DataState.Error(favState)
            }.collect{
                    favState -> _favState.value = favState
            }
    }

    fun insert(article: Article) = viewModelScope.launch {
        repository.insert(article)
    }
    fun delete(articleDbEntity: ArticleDbEntity) = viewModelScope.launch {
        repository.delete(articleDbEntity)
    }
    fun deleteAll() = viewModelScope.launch {
        repository.deleteAll()
    }

}
package com.example.repocollector.presentation.vm

import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.repocollector.data.RepoPagingSource
import com.example.repocollector.presentation.model.Repo
import com.example.repocollector.presentation.view.RepoListState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

class RepoViewModel : ViewModel() {

    private var _state by mutableStateOf(
        RepoListState(
            repo = emptyList(),
            isLoading = true,
        )
    )

    val state: State<RepoListState>
        get() = derivedStateOf { _state }

    private val errorHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
        _state = _state.copy(
            isLoading = false,
            error = throwable.message
        )
    }
    private val _searchQuery = MutableStateFlow("")

    private val repoFlow: Flow<PagingData<Repo>> = _searchQuery
        .flatMapLatest { query ->
            Pager(PagingConfig(pageSize = 3)) {
                RepoPagingSource(query)
            }.flow.cachedIn(viewModelScope)
        }
    val repos: Flow<PagingData<Repo>> = repoFlow


    init {
        getRepos()
    }

    private fun getRepos() {
        viewModelScope.launch(errorHandler) {
            _state = _state.copy(
                isLoading = false
            )
        }
    }
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }
}

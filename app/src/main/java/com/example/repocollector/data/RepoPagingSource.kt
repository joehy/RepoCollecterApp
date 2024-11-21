package com.example.repocollector.data

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.myapp.api.RetrofitClient
import com.example.repocollector.presentation.model.Repo

class RepoPagingSource (private val searchQuery: String): PagingSource<Int, Repo>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Repo> {
        val page = params.key ?: 1
        return try {
            val response: List<Repo>
            if (searchQuery.isNotEmpty()) {
                response= RetrofitClient.api.searchRepos(searchQuery, page, params.loadSize).items
                Log.d("RepoPagingSource",response.size.toString())
            } else {
                response=RetrofitClient.api.getRepos(page, params.loadSize)
            }

            Log.d("pageNum",page.toString())
            LoadResult.Page(
                data = response,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (response.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)

        }
    }

    override fun getRefreshKey(state: PagingState<Int, Repo>): Int? {
        return state.anchorPosition
    }
}

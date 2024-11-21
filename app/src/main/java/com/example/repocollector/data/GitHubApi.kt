package com.example.myapp.api

import com.example.repocollector.presentation.model.Repo
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface GitHubApi {
    @GET("repositories")
    suspend fun getRepos(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): List<Repo>
    @GET("search/repositories")
    suspend fun searchRepos(
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): SearchResponse

}
object RetrofitClient {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.github.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api: GitHubApi = retrofit.create(GitHubApi::class.java)
}
data class SearchResponse(
    val total_count: Int,
    val incomplete_results: Boolean,
    val items: List<Repo>  // This is the actual list of repositories
)
package com.example.repocollector.presentation.view

import com.example.repocollector.presentation.model.Repo

data class RepoListState(
    val repo: List<Repo>,
    val isLoading: Boolean,
    val error: String? = null,
)
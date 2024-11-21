package com.example.repocollector.presentation.pages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.example.repocollector.presentation.vm.RepoViewModel
import com.example.repocollector.presentation.view.RepoListItem

@Composable
fun RepoListScreen(paddingValues: PaddingValues, onSignOut: () -> Unit) {
    val vm: RepoViewModel = viewModel()
    val repos = vm.repos.collectAsLazyPagingItems()
    var searchQuery by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        // Sign Out Icon at the Top
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 1.dp),
            horizontalArrangement = Arrangement.End // Align to the end of the row
        ) {
            IconButton(
                onClick = { onSignOut() },
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ExitToApp, // Replace with your desired icon
                    contentDescription = "Sign Out",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = {
                searchQuery = it
                vm.updateSearchQuery(it)
            },
            label = { Text("Search by Repository name") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search Icon") },
            shape = MaterialTheme.shapes.medium, // Rounded corners
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        )

        Spacer(modifier = Modifier.height(8.dp)) // Space between components

        // List of Repositories
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        ) {
            items(
                count = repos.itemCount,
                key = repos.itemKey { it.id }
            ) { index ->
                val item = repos.get(index)
                item?.let {
                    RepoListItem(repo = it)
                    Spacer(modifier = Modifier.height(8.dp)) // Space between items
                }
            }
        }
    }
}


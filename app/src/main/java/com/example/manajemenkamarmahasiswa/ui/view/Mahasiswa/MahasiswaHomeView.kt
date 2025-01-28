package com.example.manajemenkamarmahasiswa.ui.view.Mahasiswa

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.manajemenkamarmahasiswa.model.Mahasiswa
import com.example.manajemenkamarmahasiswa.ui.view.Bangunan.OnLoading
import com.example.manajemenkamarmahasiswa.ui.viewmodel.Kamar.KamarHomeViewModel
import com.example.manajemenkamarmahasiswa.ui.viewmodel.Kamar.KamarInsertViewModel
import com.example.manajemenkamarmahasiswa.ui.viewmodel.Mahasiswa.HomeUiState
import com.example.manajemenkamarmahasiswa.ui.viewmodel.Mahasiswa.MahasiswaHomeViewModel
import com.example.manajemenkamarmahasiswa.ui.viewmodel.PenyediaViewModel
import com.example.manajemenkamarmahasiswa.ui.widget.TopAppBar
import kotlinx.coroutines.flow.StateFlow

@Composable
fun MahasiswaHomeScreen(
    navigateToItemEntry: () -> Unit, // Navigate to item entry screen (insert form)
    onDetailClick: (String) -> Unit, // Navigate to student detail
    onEditClick: (String) -> Unit, // Navigate to student edit screen
    onBackClick: () -> Unit, // Handle back navigation
    viewModel: MahasiswaHomeViewModel = viewModel(factory = PenyediaViewModel.Factory),
) {
    LaunchedEffect(Unit) {
        viewModel.getMahasiswa()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                onBack = onBackClick,
                showBackButton = true,
                judul = "Detail Mahasiswa"
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToItemEntry, // Navigate to insert form when clicked
                shape = MaterialTheme.shapes.large,
                containerColor = Color(0xFF03A9F4)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Mahasiswa",
                    tint = Color.White
                )
            }
        },
    ) { innerPadding ->
        HomeStatus(
            homeUiState = viewModel.mahasiswaUIState,
            retryAction = { viewModel.getMahasiswa() },
            modifier = Modifier.padding(innerPadding),
            onDetailClick = onDetailClick,
            onDeleteClick = { mahasiswa ->
                viewModel.deleteMahasiswa(mahasiswa.idMahasiswa)
                viewModel.getMahasiswa() // Refresh data after delete
            },
            onEditClick = onEditClick
        )
    }
}

@Composable
fun HomeStatus(
    homeUiState: StateFlow<HomeUiState>,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
    onDeleteClick: (Mahasiswa) -> Unit,
    onDetailClick: (String) -> Unit,
    onEditClick: (String) -> Unit
) {
    when (val state = homeUiState.collectAsState().value) {
        is HomeUiState.Loading -> OnLoading(modifier = modifier.fillMaxSize())
        is HomeUiState.Success -> {
            if (state.mahasiswa.isEmpty()) {
                Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "Tidak Ada Data Mahasiswa",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                MhsLayout(
                    mahasiswa = state.mahasiswa,
                    modifier = modifier.fillMaxWidth(),
                    onDetailClick = { onDetailClick(it.idMahasiswa.toString()) },
                    onDeleteClick = onDeleteClick,
                    onEditClick = onEditClick
                )
            }
        }
        is HomeUiState.Error -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Error loading data, please try again.",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = retryAction) {
                        Text("Retry")
                    }
                }
            }
        }
    }
}

@Composable
fun MhsLayout(
    mahasiswa: List<Mahasiswa>,
    modifier: Modifier = Modifier,
    onDetailClick: (Mahasiswa) -> Unit,
    onDeleteClick: (Mahasiswa) -> Unit = {},
    onEditClick: (String) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(mahasiswa) { mahasiswa ->
            MhsCard(
                mahasiswa = mahasiswa,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onDetailClick(mahasiswa) },
                onDeleteClick = { onDeleteClick(mahasiswa) },
                onEditClick = { onEditClick(mahasiswa.idMahasiswa.toString()) }
            )
        }
    }
}

@Composable
fun MhsCard(
    mahasiswa: Mahasiswa,
    modifier: Modifier = Modifier,
    onDeleteClick: (Mahasiswa) -> Unit = {},
    onEditClick: () -> Unit,
) {
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        DeleteConfirmationDialog(
            onDeleteConfirm = {
                showDialog = false
                onDeleteClick(mahasiswa)
            },
            onDeleteCancel = { showDialog = false }
        )
    }

    Card(
        modifier = modifier.padding(vertical = 8.dp),
        shape = RoundedCornerShape(size = 12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF5F5F5),
            contentColor = Color(0xFF212121)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.primary,
                                    MaterialTheme.colorScheme.secondary
                                )
                            ),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = mahasiswa.namaMahasiswa.firstOrNull()?.uppercaseChar()?.toString() ?: "?",
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = mahasiswa.namaMahasiswa,
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "NIM : ${mahasiswa.nim}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onEditClick) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Mahasiswa",
                            tint = Color(0xFF41B9F0),
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(onClick = { showDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Hapus Mahasiswa",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Divider(color = Color.Gray, thickness = 1.dp)
            // Removed Kamar related information
        }
    }
}

@Composable
private fun DeleteConfirmationDialog(
    onDeleteConfirm: () -> Unit,
    onDeleteCancel: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = {},
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    tint = Color(0xFF2196F3),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Delete Data", color = Color(0xFF2D2D2D))
            }
        },
        text = { Text("Apakah anda yakin ingin menghapus data ini?") },
        dismissButton = {
            TextButton(onClick = onDeleteCancel) {
                Text(text = "Cancel", color = Color(0xFF03A9F4))
            }
        },
        confirmButton = {
            TextButton(onClick = onDeleteConfirm) {
                Text(text = "Yes", color = Color(0xFF2196F3))
            }
        }
    )
}

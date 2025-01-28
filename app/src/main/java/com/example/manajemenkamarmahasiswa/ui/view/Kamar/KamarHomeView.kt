package com.example.manajemenkamarmahasiswa.ui.view.Kamar

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.manajemenkamarmahasiswa.R
import com.example.manajemenkamarmahasiswa.model.Kamar
import com.example.manajemenkamarmahasiswa.ui.view.Bangunan.DeleteConfirmationDialog
import com.example.manajemenkamarmahasiswa.ui.view.Bangunan.OnLoading
import com.example.manajemenkamarmahasiswa.ui.view.Bangunan.HomeStatus
import com.example.manajemenkamarmahasiswa.ui.viewmodel.Bangunan.BangunanHomeViewModel
import com.example.manajemenkamarmahasiswa.ui.viewmodel.Kamar.HomeUiState

import com.example.manajemenkamarmahasiswa.ui.viewmodel.Kamar.KamarHomeViewModel
import com.example.manajemenkamarmahasiswa.ui.viewmodel.PenyediaViewModel
import com.example.manajemenkamarmahasiswa.ui.widget.TopAppBar
import kotlinx.coroutines.flow.StateFlow

@Composable
fun KamarHomeScreen(
    navigateToItemEntry: () -> Unit,
    onDetailClick: (String) -> Unit = {},
    onBackClick: () -> Unit,
    onEditClick: (String) -> Unit,
    onDeleteClick: (Int) -> Unit, // Expecting an Int ID for delete action
    viewModel: KamarHomeViewModel = viewModel(factory = PenyediaViewModel.Factory),
) {
    LaunchedEffect(Unit) {
        viewModel.getKamar()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                onBack = onBackClick,
                showBackButton = true,
                judul = "Detail Barang",
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToItemEntry,
                shape = MaterialTheme.shapes.large,
                containerColor = Color(0xFF03A9F4)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Kamar",
                    tint = Color.White
                )
            }
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        HomeStatus(
            homeUiState = viewModel.kamarUIState,
            retryAction = { viewModel.getKamar() },
            modifier = Modifier.padding(innerPadding),
            onDetailClick = onDetailClick,
            onDeleteClick = onDeleteClick, // Expect ID here for deletion
            onEditClick = onEditClick
        )
    }
}

@Composable
fun HomeStatus(
    homeUiState: StateFlow<HomeUiState>,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
    onDeleteClick: (Int) -> Unit, // Correct type (Int)
    onDetailClick: (String) -> Unit,
    onEditClick: (String) -> Unit
) {
    when (val state = homeUiState.collectAsState().value) {
        is HomeUiState.Loading -> OnLoading(modifier = modifier.fillMaxSize())
        is HomeUiState.Success -> {
            if (state.kamar.isEmpty()) {  // Use 'kamar' instead of 'Kamar'
                Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "Tidak Ada Data Kamar",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                        textAlign = TextAlign.Center
                    )
                }
            }
            else {
                KmrLayout(
                    kamar = state.kamar,
                    modifier = modifier.fillMaxWidth(),
                    onClick = onDetailClick,
                    onDeleteClick = { kamar ->
                        onDeleteClick(kamar.idKamar) // Make sure 'kamar' here is a Kamar object
                    },
                    onEditClick = { kamar ->
                        onEditClick(kamar.toString()) // Pass the idKamar as string for editing
                    }
                )
            }
        }

        HomeUiState.Error -> TODO()
    }
}

@Composable
fun KmrLayout(
    kamar: List<Kamar>, // kamar is a list of Kamar objects
    modifier: Modifier = Modifier,
    onClick: (String) -> Unit,
    onEditClick: (String) -> Unit,
    onDeleteClick: (Kamar) -> Unit // Accepts a Kamar object
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(kamar) { singleKamar -> // Iterating over each Kamar object
            KmrCard(
                kamar = singleKamar, // Single Kamar object
                modifier = Modifier.fillMaxWidth(),
                onClick = { onClick(singleKamar.idKamar.toString()) },
                onDeleteClick = onDeleteClick, // Pass Kamar object
                onEditClick = { onEditClick(singleKamar.idKamar.toString()) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KmrCard(
    kamar: Kamar, // single kamar object
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    onEditClick: () -> Unit,
    onDeleteClick: (Kamar) -> Unit = {} // Accept Kamar object for deletion
) {
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        DeleteConfirmationDialog(
            onDeleteConfirm = {
                showDialog = false
                onDeleteClick(kamar) // Make sure to pass full Kamar object here
            },
            onDeleteCancel = { showDialog = false }
        )
    }

    Card(
        modifier = modifier,
        onClick = onClick,
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF5F5F5),
            contentColor = Color(0xFF212121)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.iconasrama), // Correct resource reference for image
                    contentDescription = null,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color.Gray, CircleShape)
                )
                Spacer(modifier = Modifier.width(8.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Bangunan: ${kamar.idBangunan}", // Reference 'bangunan' from Kamar object
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Nomor Kamar: ${kamar.nomorKamar}", // Reference 'nomorKamar' from Kamar object
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                IconButton(onClick = onEditClick) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit",
                        tint = Color(0xFF2196F3)
                    )
                }
                IconButton(onClick = { showDialog = true }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }

            Divider()

            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Status: ${kamar.statusKamar}", // Correct reference from Kamar object
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

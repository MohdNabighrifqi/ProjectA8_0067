package com.example.manajemenkamarmahasiswa.ui.view.PembayaranSewa

import androidx.compose.foundation.Image
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.manajemenkamarmahasiswa.R
import com.example.manajemenkamarmahasiswa.model.Pembayaran
import com.example.manajemenkamarmahasiswa.ui.view.Bangunan.OnLoading
import com.example.manajemenkamarmahasiswa.ui.viewmodel.Mahasiswa.MahasiswaHomeViewModel
import com.example.manajemenkamarmahasiswa.ui.viewmodel.PembayaranSewa.HomeUiState
import com.example.manajemenkamarmahasiswa.ui.viewmodel.PembayaranSewa.PembayaranHomeViewModel
import com.example.manajemenkamarmahasiswa.ui.viewmodel.PenyediaViewModel
import com.example.manajemenkamarmahasiswa.ui.widget.TopAppBar

@Composable
fun PembayaranHomeScreen(
    navigateToItemEntry: () -> Unit, // Navigasi untuk tombol tambah
    onDetailClick: (String) -> Unit = {},
    onBackClick: () -> Unit,
    onEditClick: (String) -> Unit,
    viewModel: PembayaranHomeViewModel = viewModel(factory = PenyediaViewModel.Factory),
    mahasiswaViewModel: MahasiswaHomeViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    LaunchedEffect(Unit) {
        viewModel.getPembayaran()
        mahasiswaViewModel.getMahasiswa()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                judul = "Pembayaran Home",
                showBackButton = true,
                onBack = onBackClick
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToItemEntry, // Arahkan ke layar tambah
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Tambah Pembayaran"
                )
            }
        }
    )  { innerPadding ->
        HomeStatus(
            homeUiState = viewModel.pembayaranUIState,
            retryAction = { viewModel.getPembayaran() },
            modifier = Modifier.padding(innerPadding),
            onDetailClick = onDetailClick,
            onDeleteClick = { idPembayaran ->
                viewModel.deletePembayaran(idPembayaran)
                viewModel.getPembayaran() // Refetch data after deletion
            },
            onEditClick = onEditClick
        )
    }
}

    @Composable
fun HomeStatus(
    homeUiState: HomeUiState,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
    onEditClick: (String) -> Unit,
    onDeleteClick: (Int) -> Unit = {},
    onDetailClick: (String) -> Unit
) {
    when (homeUiState) {
        is HomeUiState.Loading -> {
            OnLoading(modifier = modifier.fillMaxSize())
        }

        is HomeUiState.Success -> {
            if (homeUiState.Pembayaran.isEmpty()) {
                Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "Tidak Ada Data Pembayaran",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            } else {
                PembayaranLayout(
                    pembayaran = homeUiState.Pembayaran,
                    modifier = modifier.fillMaxWidth(),
                    onDetailClick = { onDetailClick(it.toString()) },
                    onDeleteClick = onDeleteClick,
                    onEditClick = onEditClick
                )
            }
        }

        is HomeUiState.Error -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = "Terjadi Kesalahan. Coba Lagi.",
                    style = MaterialTheme.typography.titleMedium
                )
                Button(onClick = retryAction) {
                    Text("Coba Lagi")
                }
            }
        }
    }
}

@Composable
fun PembayaranLayout(
    pembayaran: List<Pembayaran>,
    modifier: Modifier = Modifier,
    onDetailClick: (String) -> Unit,
    onDeleteClick: (Int) -> Unit = {},
    onEditClick: (String) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(pembayaran) { pembayaran ->
            PsCard(
                pembayaran = pembayaran,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onDetailClick(pembayaran.idPembayaran.toString()) },
                onDeleteClick = { onDeleteClick(pembayaran.idPembayaran) },
                onEditClick = { onEditClick(pembayaran.idPembayaran.toString()) }
            )
        }
    }
}

@Composable
fun PsCard(
    pembayaran: Pembayaran,
    nama: String = "Nama Mahasiswa",
    modifier: Modifier = Modifier,
    onDeleteClick: (Int) -> Unit = {},
    onEditClick: () -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        DeleteConfirmationDialog(
            onDeleteConfirm = {
                showDialog = false
                onDeleteClick(pembayaran.idPembayaran)  // Using the idPembayaran for deletion
            },
            onDeleteCancel = { showDialog = false }
        )
    }

    Card(
        modifier = modifier.padding(vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
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
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.iconpembayaran),
                    contentDescription = null,
                    modifier = Modifier.size(56.dp)
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = nama,
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Tanggal: ${pembayaran.tanggal}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onEditClick) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Transaksi",
                            tint = Color(0xFF03A9F8)
                        )
                    }
                    IconButton(onClick = { showDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Hapus Transaksi",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Divider(color = Color.Gray, thickness = 1.dp)
            Text(
                text = "Status: ${pembayaran.status}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
private fun DeleteConfirmationDialog(
    onDeleteConfirm: () -> Unit,
    onDeleteCancel: () -> Unit
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
                Text("Hapus Data", style = MaterialTheme.typography.titleMedium)
            }
        },
        text = {
            Text(
                "Apakah Anda yakin ingin menghapus data ini?",
                style = MaterialTheme.typography.bodyMedium
            )
        },
        dismissButton = {
            TextButton(onClick = onDeleteCancel) {
                Text(text = "Batal", color = MaterialTheme.colorScheme.primary)
            }
        },
        confirmButton = {
            TextButton(onClick = onDeleteConfirm) {
                Text(text = "Hapus", color = MaterialTheme.colorScheme.error)
            }
        }
    )
}

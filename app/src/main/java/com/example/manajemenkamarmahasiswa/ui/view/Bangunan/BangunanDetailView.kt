package com.example.manajemenkamarmahasiswa.ui.view.Bangunan

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.manajemenkamarmahasiswa.model.Bangunan
import com.example.manajemenkamarmahasiswa.ui.viewmodel.Bangunan.BangunanDetailUiState
import com.example.manajemenkamarmahasiswa.ui.viewmodel.Bangunan.BangunanDetailViewModel
import com.example.manajemenkamarmahasiswa.ui.viewmodel.PenyediaViewModel
import com.example.manajemenkamarmahasiswa.ui.widget.TopAppBar

@Composable
fun BangunanDetailScreen(
    onClickBack: () -> Unit,
    onUpdateClick: () -> Unit,
    viewModel: BangunanDetailViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    Scaffold(
        topBar = {
            TopAppBar(
                onBack = onClickBack,
                showBackButton = true,
                judul = "Detail Barang",
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onUpdateClick,
                shape = MaterialTheme.shapes.large,
                containerColor = Color(0xFF03A9F4)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit Bangunan",
                    tint = Color.White
                )
            }
        }
    ) { innerPadding ->
        DetailBgnStatus(
            modifier = Modifier.padding(innerPadding),
            bangunanDetailUiState = viewModel.bangunanDetailState,
            retryAction = { viewModel.getBangunanById() }
        )
    }
}

@Composable
fun DetailBgnStatus(
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
    bangunanDetailUiState: BangunanDetailUiState
) {
    when (bangunanDetailUiState) {
        is BangunanDetailUiState.Loading -> OnLoading(modifier)

        is BangunanDetailUiState.Success -> BangunanContent(
            modifier = modifier,
            bangunan = bangunanDetailUiState.bangunan
        )

        is BangunanDetailUiState.Error -> OnError(retryAction, modifier)
    }
}

@Composable
fun BangunanContent(modifier: Modifier, bangunan: Bangunan) {
    if (bangunan.namaBangunan.isEmpty()) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Data Bangunan tidak ditemukan",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                textAlign = TextAlign.Center
            )
        }
    } else {
        ItemDetailBgn(
            bangunan = bangunan,
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
    }
}

@Composable
fun ItemDetailBgn(
    bangunan: Bangunan,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = bangunan.namaBangunan,
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
            )

            Spacer(modifier = Modifier.height(8.dp))

            InfoRow(icon = Icons.Default.LocationOn, text = bangunan.alamat)

            Spacer(modifier = Modifier.height(16.dp))

            InfoRow(icon = Icons.Default.Home, text = "Jumlah lantai: ${bangunan.jumlahLt}")
        }
    }
}

@Composable
fun InfoRow(icon: ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun OnLoading(modifier: Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun OnError(retryAction: () -> Unit, modifier: Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Terjadi kesalahan saat memuat data.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = retryAction) {
                Text(text = "Coba Lagi")
            }
        }
    }
}

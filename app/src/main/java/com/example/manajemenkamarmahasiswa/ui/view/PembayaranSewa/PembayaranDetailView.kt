package com.example.manajemenkamarmahasiswa.ui.view.PembayaranSewa

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.manajemenkamarmahasiswa.model.Pembayaran
import com.example.manajemenkamarmahasiswa.ui.view.Bangunan.OnError
import com.example.manajemenkamarmahasiswa.ui.view.Bangunan.OnLoading
import com.example.manajemenkamarmahasiswa.ui.viewmodel.PembayaranSewa.PembayaranDetailUiState
import com.example.manajemenkamarmahasiswa.ui.viewmodel.PembayaranSewa.PembayaranDetailViewModel
import com.example.manajemenkamarmahasiswa.ui.viewmodel.PenyediaViewModel
import com.example.manajemenkamarmahasiswa.ui.widget.TopAppBar

@Composable
fun PembayaranDetailScreen(
    onClickBack: () -> Unit,
    idPembayran: String? = null,
    idMahasiswa: String? = null,
    viewModel: PembayaranDetailViewModel = viewModel(factory = PenyediaViewModel.Factory),
    idPembayaran: String,

    ) {

    LaunchedEffect(idPembayran, idMahasiswa) {
        when {
            idPembayran != null -> viewModel.getPembayaranById(idPembayran)
            idMahasiswa != null -> viewModel.getPembayaranByIdMhs(idMahasiswa)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                onBack = onClickBack,
                showBackButton = true,
                judul = "Detail Barang"
            )
        }
    ) { innerPadding ->
        DetailPsStatus(
            modifier = Modifier.padding(innerPadding),
            detailPsUiState = viewModel.pembayaranDetailState,
            retryAction = {
                when {
                    idPembayran != null -> viewModel.getPembayaranById(idPembayran)
                    idMahasiswa != null -> viewModel.getPembayaranByIdMhs(idMahasiswa)
                }
            }
        )
    }
}

@Composable
fun DetailPsStatus(
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
    detailPsUiState: PembayaranDetailUiState
) {
    when (detailPsUiState) {
        is PembayaranDetailUiState.Loading -> OnLoading(modifier = modifier.fillMaxSize())

        is PembayaranDetailUiState.Success -> {
            val pembayaran = detailPsUiState.pembayaran
            if (pembayaran.idPembayaran.toString().isEmpty()) {
                Box(
                    modifier = modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Data Pembayaran tidak ditemukan",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                AnimatedVisibility(visible = true) {
                    ItemDetailPs(
                        pembayaran = pembayaran,
                        nama = "Mahasiswa tidak ditemukan",
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                }
            }
        }

        is PembayaranDetailUiState.Error -> OnError(
            retryAction = retryAction,
            modifier = modifier.fillMaxSize()
        )
    }
}

@Composable
fun ItemDetailPs(
    modifier: Modifier = Modifier,
    pembayaran: Pembayaran,
    nama: String,
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF5F5F5),
            contentColor = Color(0xFF212121)
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "Detail Pembayaran",
                style = MaterialTheme.typography.headlineMedium,
                color = Color(0xFF03A9F4),
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // Nama Mahasiswa
            ComponentDetailPs(
                title = "Nama Mahasiswa",
                content = nama,
                icon = {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = "Ikon Nama Mahasiswa",
                        tint = Color(0xFF2196F3)
                    )
                }
            )

            Spacer(modifier = Modifier.height(12.dp))
            Divider()

            // Jumlah Pembayaran
            ComponentDetailPs(
                title = "Jumlah Pembayaran",
                content = "Rp.${pembayaran.jumlah}",
                icon = {
                    Icon(
                        Icons.Default.Info,
                        contentDescription = "Ikon Jumlah Pembayaran",
                        tint = Color(0xFF2196F3)
                    )
                }
            )

            Spacer(modifier = Modifier.height(12.dp))
            Divider()

            // Tanggal Pembayaran
            ComponentDetailPs(
                title = "Tanggal Pembayaran",
                content = pembayaran.tanggal,
                icon = {
                    Icon(
                        Icons.Default.DateRange,
                        contentDescription = "Ikon Tanggal Pembayaran",
                        tint = Color(0xFF2196F3)
                    )
                }
            )

            Spacer(modifier = Modifier.height(12.dp))
            Divider()

            // Status Pembayaran
            ComponentDetailPs(
                title = "Status Pembayaran",
                content = pembayaran.status,
                icon = {
                    Icon(
                        Icons.Default.MailOutline,
                        contentDescription = "Ikon Status Pembayaran",
                        tint = Color(0xFF2196F3)
                    )
                }
            )
        }
    }
}

@Composable
fun ComponentDetailPs(
    title: String,
    content: String,
    icon: @Composable () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .padding(4.dp),
            contentAlignment = Alignment.Center
        ) {
            icon()
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.surfaceTint
            )
            Text(
                text = content,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

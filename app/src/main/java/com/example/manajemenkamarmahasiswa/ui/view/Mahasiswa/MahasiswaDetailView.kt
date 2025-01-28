package com.example.manajemenkamarmahasiswa.ui.view.Mahasiswa

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
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.manajemenkamarmahasiswa.model.Mahasiswa
import com.example.manajemenkamarmahasiswa.navigation.DestinasiMahasiswaDetail
import com.example.manajemenkamarmahasiswa.repository.MahasiswaRepository
import com.example.manajemenkamarmahasiswa.ui.view.Bangunan.OnError
import com.example.manajemenkamarmahasiswa.ui.view.Bangunan.OnLoading
import com.example.manajemenkamarmahasiswa.ui.viewmodel.Kamar.KamarHomeViewModel
import com.example.manajemenkamarmahasiswa.ui.viewmodel.Mahasiswa.MahasiswaDetailViewModel
import com.example.manajemenkamarmahasiswa.ui.viewmodel.PenyediaViewModel
import com.example.manajemenkamarmahasiswa.ui.widget.TopAppBar
import com.example.manajemenkamarmahasiswa.ui.viewmodel.Mahasiswa.MahasiswaDetailUiState
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException

@Composable
fun MahasiswaDetailScreen(
    onClickBack: () -> Unit,
    onTambahPembayaranClick: (String) -> Unit,
    viewModel: MahasiswaDetailViewModel = viewModel(factory = PenyediaViewModel.Factory),
) {
    val DetailMahasiswaUiState = viewModel.mahasiswaDetailState

    // Memastikan data mahasiswa diambil saat layar pertama kali dibuka
    LaunchedEffect(Unit) {
        viewModel.getMahasiswaById()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                onBack = onClickBack,
                showBackButton = true,
                judul = "Detail Mahasiswa" // Menjaga konsistensi judul
            )
        }
    ) { innerPadding ->
        DetailMhsStatus(
            modifier = Modifier.padding(innerPadding),
            MahasiswaDetailUiState = DetailMahasiswaUiState,
            retryAction = { viewModel.getMahasiswaById() },
            onTambahPembayaranClick = onTambahPembayaranClick
        )
    }
}


@Composable
fun DetailMhsStatus(
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
    MahasiswaDetailUiState: MahasiswaDetailUiState,
    onTambahPembayaranClick: (String) -> Unit
) {
    when (MahasiswaDetailUiState) {
        is MahasiswaDetailUiState.Loading -> OnLoading(
            modifier = modifier.fillMaxSize()
        )

        is MahasiswaDetailUiState.Success -> {
            val mahasiswa = MahasiswaDetailUiState.mahasiswa
            if (mahasiswa.idMahasiswa.toString().isEmpty()) {
                Box(
                    modifier = modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Data mahasiswa tidak ditemukan",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                ItemDetailMhs(
                    mahasiswa = mahasiswa,
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    onTambahPembayaranClick = { onTambahPembayaranClick(mahasiswa.idMahasiswa.toString()) }
                )
            }
        }

        is MahasiswaDetailUiState.Error -> OnError(
            retryAction = retryAction,
            modifier = modifier.fillMaxSize()
        )

        else -> {}
    }
}

@Composable
fun ItemDetailMhs(
    modifier: Modifier = Modifier,
    mahasiswa: Mahasiswa,
    onTambahPembayaranClick: () -> Unit
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
                text = "Detail Mahasiswa",
                style = MaterialTheme.typography.headlineMedium,
                color = Color(0xFF03A9F4),
                modifier = Modifier.padding(bottom = 12.dp)
            )

            ComponentDetailMhs(
                title = "Nama Mahasiswa",
                content = mahasiswa.namaMahasiswa,
                icon = {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = "Ikon Nama",
                        tint = Color(0xFF2196F3)
                    )
                }
            )

            Spacer(modifier = Modifier.height(12.dp))
            Divider()

            ComponentDetailMhs(
                title = "NIM Mahasiswa",
                content = mahasiswa.nim,
                icon = {
                    Icon(
                        Icons.Default.Info,
                        contentDescription = "Ikon NIM",
                        tint = Color(0xFF2196F3)
                    )
                }
            )

            Spacer(modifier = Modifier.height(12.dp))
            Divider()

            ComponentDetailMhs(
                title = "Nomor Hp Mahasiswa",
                content = mahasiswa.noTelp,
                icon = {
                    Icon(
                        Icons.Default.Phone,
                        contentDescription = "Ikon Nomor Hp",
                        tint = Color(0xFF2196F3)
                    )
                }
            )

            Spacer(modifier = Modifier.height(12.dp))
            Divider()

            ComponentDetailMhs(
                title = "Email Mahasiswa",
                content = mahasiswa.email,
                icon = {
                    Icon(
                        Icons.Default.MailOutline,
                        contentDescription = "Ikon Email",
                        tint = Color(0xFF2196F3)
                    )
                }
            )

            Spacer(modifier = Modifier.height(12.dp))
            Divider()

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onTambahPembayaranClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = MaterialTheme.shapes.large,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF03A9F4),
                    contentColor = Color.White
                )
            ) {
                Text(text = "Tambah Pembayaran", fontSize = 18.sp)
            }
        }
    }
}

@Composable
fun ComponentDetailMhs(
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
package com.example.manajemenkamarmahasiswa.ui.view.Kamar

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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.manajemenkamarmahasiswa.R
import com.example.manajemenkamarmahasiswa.model.Kamar
import com.example.manajemenkamarmahasiswa.ui.view.Bangunan.OnError
import com.example.manajemenkamarmahasiswa.ui.view.Bangunan.OnLoading
import com.example.manajemenkamarmahasiswa.ui.viewmodel.Bangunan.BangunanHomeViewModel
import com.example.manajemenkamarmahasiswa.ui.viewmodel.Kamar.KamarDetailUiState
import com.example.manajemenkamarmahasiswa.ui.viewmodel.Kamar.KamarDetailViewModel
import com.example.manajemenkamarmahasiswa.ui.viewmodel.PenyediaViewModel
import com.example.manajemenkamarmahasiswa.ui.widget.TopAppBar

@Composable
fun KamarDetailScreen(
    onClickBack: () -> Unit,
    onUpdateClick: () -> Unit,
    viewModel: KamarDetailViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    LaunchedEffect(Unit) {
        viewModel.getKamarById()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                onBack = onClickBack,
                showBackButton = true,
                judul = "Detail Kamar",
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
                    contentDescription = "Edit kamar",
                    tint = Color.White
                )
            }
        }
    ) { innerPadding ->
        DetailKmrStatus(
            modifier = Modifier.padding(innerPadding),
            detailKmrUiState = viewModel.kamarDetailState,
            retryAction = { viewModel.getKamarById() }
        )
    }

    // BangunanViewModel is now declared but not used, keeping it for future use or reference
    // If you decide to use it later, you can reference bangunanViewModel here.
}


@Composable
fun DetailKmrStatus(
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
    detailKmrUiState: KamarDetailUiState
) {
    when (detailKmrUiState) {
        is KamarDetailUiState.Loading -> OnLoading(
            modifier = modifier.fillMaxSize()
        )
        is KamarDetailUiState.Success -> {
            val kamar = detailKmrUiState.kamar
            if (kamar.idKamar.toString().isEmpty()) {
                Box(
                    modifier = modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Data kamar tidak ditemukan",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                AnimatedVisibility(visible = true) {
                    ItemDetailKmr(
                        kamar = kamar,
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                }
            }
        }

        is KamarDetailUiState.Error -> OnError(
            retryAction = retryAction,
            modifier = modifier.fillMaxSize()
        )
    }
}

@Composable
fun ItemDetailKmr(
    modifier: Modifier = Modifier,
    kamar: Kamar
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
                text = "Detail Kamar",
                style = MaterialTheme.typography.headlineMedium,
                color = Color(0xFF03A9F4),
                modifier = Modifier.padding(bottom = 12.dp)
            )

            ComponentDetailKmr(
                title = "Nomor kamar",
                content = kamar.nomorKamar,
                icon = {
                    Icon(
                        painter = painterResource(R.drawable.iconkamar),
                        contentDescription = "Ikon Nomor kamar",
                        tint = Color(0xFF2196F3)
                    )
                }
            )

            Spacer(modifier = Modifier.height(12.dp))
            Divider()

            ComponentDetailKmr(
                title = "Kapasitas",
                content = kamar.kapasitas.toString(),
                icon = {
                    Icon(
                        painter = painterResource(R.drawable.iconkapasitas),
                        contentDescription = "Ikon Kapasitas",
                        tint = Color(0xFF2196F3)
                    )
                }
            )

            Spacer(modifier = Modifier.height(12.dp))
            Divider()

            ComponentDetailKmr(
                title = "Status Kamar",
                content = kamar.statusKamar,
                icon = {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = "Ikon Status",
                        tint = Color(0xFF3EBBF3)
                    )
                }
            )
        }
    }
}

@Composable
fun ComponentDetailKmr(
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

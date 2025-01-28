package com.example.manajemenkamarmahasiswa.ui.view.Kamar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.manajemenkamarmahasiswa.R
import com.example.manajemenkamarmahasiswa.navigation.DestinasiKamarEdit
import com.example.manajemenkamarmahasiswa.ui.viewmodel.Bangunan.BangunanHomeViewModel
import com.example.manajemenkamarmahasiswa.ui.viewmodel.Kamar.FormErrorKamarState
import com.example.manajemenkamarmahasiswa.ui.viewmodel.Kamar.KamarEditViewModel
import com.example.manajemenkamarmahasiswa.ui.viewmodel.Kamar.KamarInsertUiEvent
import com.example.manajemenkamarmahasiswa.ui.viewmodel.Kamar.KamarInsertUiState
import com.example.manajemenkamarmahasiswa.ui.viewmodel.PenyediaViewModel
import com.example.manajemenkamarmahasiswa.ui.widget.TopAppBar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun KamarEditScreen(
    onClickBack: () -> Unit,
    onNavigate: () -> Unit,
    viewModel: KamarEditViewModel = viewModel(factory = PenyediaViewModel.Factory),
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val uiState = viewModel.KamarEditUiState // Mengambil state dari ViewModel

    // Handling snackbar messages ketika ada perubahan snackbarMessage
    LaunchedEffect(uiState.snackBarMessage) {
        uiState.snackBarMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.resetSnackBarKmrMessage() // Reset message setelah ditampilkan
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                onBack = onClickBack,
                showBackButton = true,
                judul = "Detail Barang",
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AnimatedVisibility(visible = true) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFF5F5F5),
                        contentColor = MaterialTheme.colorScheme.onBackground
                    )
                ) {
                    InsertUpdateBodyKmr(
                        uiState = uiState,
                        formTitle = "Form ${DestinasiKamarEdit.titleRes}",
                        onValueChange = { updatedKamarEvent ->
                            viewModel.updateInsertKamarState(updatedKamarEvent)
                        },
                        onClick = {
                            val isSaved = viewModel.updateKamar()
                            if (isSaved) {
                                delay(1000)
                                onNavigate()
                            }
                        },
                        isReadOnly = true,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun InsertUpdateBodyKmr(
    modifier: Modifier = Modifier,
    formTitle: String,
    onValueChange: (KamarInsertUiEvent) -> Unit,
    uiState: KamarInsertUiState,
    onClick: suspend () -> Unit,
    isReadOnly: Boolean = false
) {
    val coroutineScope = rememberCoroutineScope()

    val bangunan = uiState.kamarInsertUiEvent
    val namaBgn = "Nama Bangunan Tidak Ditemukan"  // Temporarily set as no dropdown function needed

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface, shape = MaterialTheme.shapes.medium)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = formTitle,
            color = Color(0xFF03A9F4),
            style = MaterialTheme.typography.headlineMedium
        )

        // Form input for Room data
        FormKmrInputUpdate(
            insertKmrUiEvent = uiState.kamarInsertUiEvent,
            onValueChange = onValueChange,
            errorKmrState = uiState.isKamarEntryValid,
            isReadOnly = isReadOnly,
            modifier = Modifier.fillMaxWidth(),
            namaBgn = namaBgn
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                coroutineScope.launch {
                    onClick()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = MaterialTheme.shapes.large,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF03A9F4),
                contentColor = Color.White
            )
        ) {
            Text(text = "Simpan", fontSize = 18.sp)
        }
    }
}

@Composable
fun FormKmrInputUpdate(
    insertKmrUiEvent: KamarInsertUiEvent,
    modifier: Modifier = Modifier,
    onValueChange: (KamarInsertUiEvent) -> Unit,
    errorKmrState: FormErrorKamarState = FormErrorKamarState(),
    isReadOnly: Boolean = false,
    namaBgn: String
) {
    Column(
        modifier = modifier
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Input untuk nomor kamar
        OutlinedTextField(
            value = insertKmrUiEvent.nomorKamar,
            onValueChange = {
                onValueChange(insertKmrUiEvent.copy(nomorKamar = it))
            },
            label = { Text("Nomor Kamar") },
            placeholder = { Text("Masukkan Nomor Kamar") },
            leadingIcon = {
                Icon(
                    painter = painterResource(R.drawable.iconkamar),
                    contentDescription = "Door Icon",
                    tint = Color(0xFF2196F3),
                    modifier = Modifier.size(28.dp)
                )
            },
            isError = errorKmrState.nomorKamar != null,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF03A9F4),
                unfocusedBorderColor = Color(0xFFB0BEC5),
                errorBorderColor = Color.Red
            )
        )

        if (errorKmrState.nomorKamar != null) {
            Text(
                text = errorKmrState.nomorKamar,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 8.dp, top = 2.dp)
            )
        }

        // Kapasitas Field
        OutlinedTextField(
            value = insertKmrUiEvent.kapasitas,
            onValueChange = {
                onValueChange(insertKmrUiEvent.copy(kapasitas = it))
            },
            label = { Text("Kapasitas") },
            placeholder = { Text("Masukkan Jumlah Kapasitas") },
            leadingIcon = {
                Icon(
                    painter = painterResource(R.drawable.iconkapasitas),
                    contentDescription = "Ikon Jumlah Lantai",
                    tint = Color(0xFF2196F3),
                    modifier = Modifier.size(28.dp)
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            isError = errorKmrState.kapasitas != null,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF03A9F4),
                unfocusedBorderColor = Color(0xFFB0BEC5),
                errorBorderColor = Color.Red
            )
        )

        if (errorKmrState.kapasitas != null) {
            Text(
                text = errorKmrState.kapasitas,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 8.dp, top = 2.dp)
            )
        }
    }
}

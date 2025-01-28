package com.example.manajemenkamarmahasiswa.ui.view.PembayaranSewa

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.manajemenkamarmahasiswa.R
import com.example.manajemenkamarmahasiswa.navigation.DestinasiPembayaranInsert
import com.example.manajemenkamarmahasiswa.ui.viewmodel.PembayaranSewa.FormErrorPembayaranState
import com.example.manajemenkamarmahasiswa.ui.viewmodel.PembayaranSewa.PembayaranInsertUiEvent
import com.example.manajemenkamarmahasiswa.ui.viewmodel.PembayaranSewa.PembayaranInsertUiState
import com.example.manajemenkamarmahasiswa.ui.viewmodel.PembayaranSewa.PembayaranInsertViewModel
import com.example.manajemenkamarmahasiswa.ui.viewmodel.PenyediaViewModel
import com.example.manajemenkamarmahasiswa.ui.widget.TopAppBar
import kotlinx.coroutines.launch

@Composable
fun PembayaranInsertView(
    idMahasiswa: String,
    onBackClick: () -> Unit,
    onNavigate: () -> Unit,
    viewModel: PembayaranInsertViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val uiState = viewModel.uiPembayaranState
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(uiState.snackBarMessage) {
        uiState.snackBarMessage?.let { message ->
            coroutineScope.launch {
                snackbarHostState.showSnackbar(message)
                viewModel.resetSnackBarPembayaranMessage()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                judul = "Tambah Pembayaran",
                showBackButton = true,
                onBack = onBackClick,
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
            verticalArrangement = Arrangement.Top
        ) {
            InsertBodyPembayaran(
                idMahasiswa = idMahasiswa,
                uiState = uiState,
                formTitle = "Form Tambah Pembayaran",
                onValueChange = { updatedPembayaranEvent ->
                    viewModel.updateInsertPembayaranState(updatedPembayaranEvent)
                },
                onClick = {
                    val isSaved = viewModel.insertPembayaran()
                    if (isSaved) {
                        onNavigate()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun InsertBodyPembayaran(
    idMahasiswa: String,
    modifier: Modifier = Modifier,
    formTitle: String,
    onValueChange: (PembayaranInsertUiEvent) -> Unit,
    uiState: PembayaranInsertUiState,
    onClick: suspend () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = modifier
            .background(Color(0xFFF5F5F5), shape = MaterialTheme.shapes.medium)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = formTitle,
            color = Color(0xFF009688), // Warna teks yang sama
            style = MaterialTheme.typography.headlineMedium
        )

        FormPembayaranInput(
            idMahasiswa = idMahasiswa,
            pembayaranInsertUiEvent = uiState.pembayaranInsertUiEvent,
            onValueChange = onValueChange,
            errorPembayaranState = uiState.isPembayaranEntryValid,
            modifier = Modifier.fillMaxWidth()
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
                containerColor = Color(0xFF009688), // Warna tombol yang sama
                contentColor = Color.White
            )
        ) {
            Text(text = "Simpan", fontSize = 18.sp)
        }
    }
}

@Composable
fun FormPembayaranInput(
    idMahasiswa: String,
    pembayaranInsertUiEvent: PembayaranInsertUiEvent,
    modifier: Modifier = Modifier,
    onValueChange: (PembayaranInsertUiEvent) -> Unit = {},
    errorPembayaranState: FormErrorPembayaranState = FormErrorPembayaranState()
) {
    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Input ID Mahasiswa (Manual Input)
        OutlinedTextField(
            value = pembayaranInsertUiEvent.idMahasiswa,
            onValueChange = {
                onValueChange(pembayaranInsertUiEvent.copy(idMahasiswa = it))
            },
            label = { Text("ID Mahasiswa") },
            placeholder = { Text("Masukkan ID Mahasiswa") },
            leadingIcon = {
                Icon(
                    Icons.Default.Person,
                    contentDescription = "Ikon Mahasiswa",
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = errorPembayaranState.idMahasiswa != null,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                errorBorderColor = MaterialTheme.colorScheme.error
            )
        )
        // Error Message for ID Mahasiswa
        errorPembayaranState.idMahasiswa?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 8.dp, top = 2.dp)
            )
        }

        // Input Tanggal
        OutlinedTextField(
            value = pembayaranInsertUiEvent.tanggal,
            onValueChange = {
                onValueChange(pembayaranInsertUiEvent.copy(tanggal = it))
            },
            label = { Text("Tanggal") },
            placeholder = { Text("yyyy-mm-dd") },
            leadingIcon = {
                Icon(
                    Icons.Default.DateRange,
                    contentDescription = "Ikon Tanggal",
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = errorPembayaranState.tanggal != null,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                errorBorderColor = MaterialTheme.colorScheme.error
            )
        )
        // Error Message for Tanggal
        errorPembayaranState.tanggal?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 8.dp, top = 2.dp)
            )
        }

        // Input Jumlah Pembayaran
        OutlinedTextField(
            value = pembayaranInsertUiEvent.jumlah,
            onValueChange = {
                onValueChange(pembayaranInsertUiEvent.copy(jumlah = it))
            },
            label = { Text("Jumlah Pembayaran") },
            placeholder = { Text("Masukkan Jumlah Pembayaran") },
            leadingIcon = {
                Icon(
                    Icons.Default.Star,
                    contentDescription = "Ikon Transaksi",
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = errorPembayaranState.jumlah != null,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                errorBorderColor = MaterialTheme.colorScheme.error
            )
        )
        // Error Message for Jumlah Pembayaran
        errorPembayaranState.jumlah?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 8.dp, top = 2.dp)
            )
        }
    }
}

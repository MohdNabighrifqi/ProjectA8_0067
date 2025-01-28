package com.example.manajemenkamarmahasiswa.ui.view.PembayaranSewa

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.manajemenkamarmahasiswa.R
import com.example.manajemenkamarmahasiswa.navigation.DestinasiPembayaranEdit
import com.example.manajemenkamarmahasiswa.ui.viewmodel.Mahasiswa.MahasiswaHomeViewModel
import com.example.manajemenkamarmahasiswa.ui.viewmodel.PembayaranSewa.FormErrorPembayaranState
import com.example.manajemenkamarmahasiswa.ui.viewmodel.PembayaranSewa.PembayaranEditViewModel
import com.example.manajemenkamarmahasiswa.ui.viewmodel.PembayaranSewa.PembayaranInsertUiEvent
import com.example.manajemenkamarmahasiswa.ui.viewmodel.PembayaranSewa.PembayaranInsertUiState
import com.example.manajemenkamarmahasiswa.ui.viewmodel.PenyediaViewModel
import com.example.manajemenkamarmahasiswa.ui.widget.TopAppBar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun UpdatePsScreen(
    onClickBack: () -> Unit,
    onNavigate: () -> Unit,
    viewModel: PembayaranEditViewModel = viewModel(factory = PenyediaViewModel.Factory),

) {
    val snackbarHostState = remember { SnackbarHostState() }
    val uiState = viewModel.pembayaranEditUiState

    // Show Snackbar for errors or success
    LaunchedEffect(uiState.snackBarMessage) {
        uiState.snackBarMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.resetSnackBarPSMessage()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                judul = "Detail Barang",
                showBackButton = true,
                onBack = onClickBack,
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
                    InsertUpdateBodyPS(
                        uiState = uiState,
                        formTitle = "Form ${DestinasiPembayaranEdit.titleRes}",
                        onValueChange = { updatedPembayaranEvent ->
                            viewModel.updateInsertPembayaranState(updatedPembayaranEvent)
                        },
                        onClick = {
                            val isSaved = viewModel.editPembayaran()
                            if (isSaved) {
                                delay(1000)
                                onNavigate()
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun InsertUpdateBodyPS(
    modifier: Modifier = Modifier,
    formTitle: String,
    onValueChange: (PembayaranInsertUiEvent) -> Unit,
    uiState: PembayaranInsertUiState,
    onClick: suspend () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()

    val pembayaran = uiState.pembayaranInsertUiEvent

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

        FormPSInputUpdate(
            insertPSUiEvent = uiState.pembayaranInsertUiEvent,
            onValueChange = onValueChange,
            errorPSState = uiState.isPembayaranEntryValid,
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
                containerColor = Color(0xFF03A9F4),
                contentColor = Color.White
            )
        ) {
            Text(text = "Simpan", fontSize = 18.sp)
        }
    }
}


@Composable
fun FormPSInputUpdate(
    insertPSUiEvent: PembayaranInsertUiEvent,
    modifier: Modifier = Modifier,
    onValueChange: (PembayaranInsertUiEvent) -> Unit = {},
    errorPSState: FormErrorPembayaranState = FormErrorPembayaranState(),
) {
    Column(
        modifier = modifier
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedTextField(
            value = insertPSUiEvent.tanggal,
            onValueChange = {
                onValueChange(insertPSUiEvent.copy(tanggal = it))
            },
            label = { Text("Tanggal") },
            placeholder = { Text("yyyy-mm-dd") },
            leadingIcon = {
                Icon(
                    Icons.Default.DateRange,
                    contentDescription = "Ikon Tanggal",
                    tint = Color(0xFF2196F3),
                    modifier = Modifier.size(28.dp)
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            isError = errorPSState.tanggal != null,
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF03A9F4),
                unfocusedBorderColor = Color(0xFFB0BEC5),
                errorBorderColor = Color.Red
            )
        )

        if (errorPSState.tanggal != null) {
            Text(
                text = errorPSState.tanggal,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 8.dp, top = 2.dp)
            )
        }

        OutlinedTextField(
            value = insertPSUiEvent.jumlah,
            onValueChange = {
                onValueChange(insertPSUiEvent.copy(jumlah = it))
            },
            label = { Text("Jumlah Pembayaran") },
            placeholder = { Text("Masukkan Jumlah Pembayaran") },
            leadingIcon = {
                Icon(
                    painter = painterResource(R.drawable.iconkapasitas),
                    contentDescription = "Ikon Jumlah Pembayaran",
                    tint = Color(0xFF2196F3),
                    modifier = Modifier.size(28.dp)
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            isError = errorPSState.jumlah != null,
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF1DDBAF),
                unfocusedBorderColor = Color(0xFFB0BEC5),
                errorBorderColor = Color.Red
            )
        )

        if (errorPSState.jumlah != null) {
            Text(
                text = errorPSState.jumlah,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 8.dp, top = 2.dp)
            )
        }

        StatusBayarRadioButton(
            statusBayar = insertPSUiEvent.status,
            onStatusChanged = {
                onValueChange(insertPSUiEvent.copy(status = it))
            },
            error = errorPSState.status
        )
    }
}
@Composable
fun StatusBayarRadioButton(
    statusBayar: String,
    onStatusChanged: (String) -> Unit,
    error: String? = null
) {
    // Available payment statuses to choose from
    val statuses = listOf("Lunas", "Belum Lunas")

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Title for the Payment Status field
        Text(
            text = "Status Pembayaran",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF757575)
        )

        // Radio buttons for each status option
        statuses.forEach { status ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = statusBayar == status,
                    onClick = { onStatusChanged(status) }
                )
                Text(
                    text = status,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }

        // If there is an error, display it below the radio buttons
        if (error != null) {
            Text(
                text = error,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 8.dp, top = 4.dp)
            )
        }
    }
}


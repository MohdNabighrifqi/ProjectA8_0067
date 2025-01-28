package com.example.manajemenkamarmahasiswa.ui.view.Kamar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
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
import com.example.manajemenkamarmahasiswa.ui.viewmodel.Kamar.FormErrorKamarState
import com.example.manajemenkamarmahasiswa.ui.viewmodel.Kamar.KamarInsertUiEvent
import com.example.manajemenkamarmahasiswa.ui.viewmodel.Kamar.KamarInsertUiState
import com.example.manajemenkamarmahasiswa.ui.viewmodel.Kamar.KamarInsertViewModel
import com.example.manajemenkamarmahasiswa.ui.viewmodel.PenyediaViewModel
import com.example.manajemenkamarmahasiswa.ui.widget.TopAppBar
import kotlinx.coroutines.launch

@Composable
fun KamarInsertView(
    onBackClick: () -> Unit,
    onNavigate: () -> Unit,
    viewModel: KamarInsertViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val uiState = viewModel.uiKamarState
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(uiState.snackBarMessage) {
        uiState.snackBarMessage?.let { message ->
            coroutineScope.launch {
                snackbarHostState.showSnackbar(message)
                viewModel.resetSnackBarKmrMessage() // Reset after showing snackbar
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                onBack = onBackClick,
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
                    InsertBodyKamar(
                        uiState = uiState,
                        formTitle = "Form Insert Kamar",  // Replace with proper title
                        onValueChange = { updatedKamarEvent ->
                            viewModel.updateInsertKamarState(updatedKamarEvent)
                        },
                        onClick = {
                            val isSaved = viewModel.insertKamar()
                            if (isSaved) {
                                onNavigate()
                            }
                        },
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun InsertBodyKamar(
    modifier: Modifier = Modifier,
    formTitle: String,
    onValueChange: (KamarInsertUiEvent) -> Unit,
    uiState: KamarInsertUiState,
    onClick: suspend () -> Unit,
    isReadOnly: Boolean = false
) {
    val coroutineScope = rememberCoroutineScope()

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

        // Remove the Dropdown, use OutlinedTextField for idBangunan
        OutlinedTextField(
            value = uiState.kamarInsertUiEvent.idBangunan,
            onValueChange = {
                onValueChange(uiState.kamarInsertUiEvent.copy(idBangunan = it))
            },
            label = { Text("ID Bangunan") },
            placeholder = { Text("Masukkan ID Bangunan") },
            leadingIcon = {
                Icon(
                    painter = painterResource(R.drawable.iconasrama),
                    contentDescription = "Building Icon",
                    tint = Color(0xFF2196F3),
                    modifier = Modifier.size(28.dp)
                )
            },
            modifier = Modifier.fillMaxWidth(),
            isError = uiState.isKamarEntryValid.idBangunan != null,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF03A9F4),
                unfocusedBorderColor = Color(0xFFB0BEC5),
                errorBorderColor = Color.Red
            )
        )

        if (uiState.isKamarEntryValid.idBangunan != null) {
            Text(
                text = uiState.isKamarEntryValid.idBangunan,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 8.dp, top = 2.dp)
            )
        }

        // Existing fields for "Nomor Kamar", "Kapasitas", etc...
        OutlinedTextField(
            value = uiState.kamarInsertUiEvent.nomorKamar,
            onValueChange = {
                onValueChange(uiState.kamarInsertUiEvent.copy(nomorKamar = it))
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
            modifier = Modifier.fillMaxWidth(),
            isError = uiState.isKamarEntryValid.nomorKamar != null,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF03A9F4),
                unfocusedBorderColor = Color(0xFFB0BEC5),
                errorBorderColor = Color.Red
            )
        )

        if (uiState.isKamarEntryValid.nomorKamar != null) {
            Text(
                text = uiState.isKamarEntryValid.nomorKamar,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 8.dp, top = 2.dp)
            )
        }

        // Other fields for "Kapasitas" and "Status Kamar" handled the same way
        OutlinedTextField(
            value = uiState.kamarInsertUiEvent.kapasitas,
            onValueChange = {
                onValueChange(uiState.kamarInsertUiEvent.copy(kapasitas = it))
            },
            label = { Text("Kapasitas") },
            placeholder = { Text("Masukkan Jumlah Kapasitas") },
            leadingIcon = {
                Icon(
                    painter = painterResource(R.drawable.iconkapasitas),
                    contentDescription = "Capacity Icon",
                    tint = Color(0xFF2196F3),
                    modifier = Modifier.size(28.dp)
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            isError = uiState.isKamarEntryValid.kapasitas != null,
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF03A9F4),
                unfocusedBorderColor = Color(0xFFB0BEC5),
                errorBorderColor = Color.Red
            )
        )

        if (uiState.isKamarEntryValid.kapasitas != null) {
            Text(
                text = uiState.isKamarEntryValid.kapasitas,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 8.dp, top = 2.dp)
            )
        }

        // Status Kamar radio buttons, handled like before
        StatusKamarRadioButton(
            statusKamar = uiState.kamarInsertUiEvent.statusKamar,
            onStatusChanged = {
                onValueChange(uiState.kamarInsertUiEvent.copy(statusKamar = it))
            },
            error = uiState.isKamarEntryValid.statusKamar
        )
    }
}


@Composable
fun FormKmrInput(
    insertKmrUiEvent: KamarInsertUiEvent,
    modifier: Modifier = Modifier,
    onValueChange: (KamarInsertUiEvent) -> Unit = {},
    errorKmrState: FormErrorKamarState = FormErrorKamarState(),
    isReadOnly: Boolean = false
) {
    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Replaced dropdown with OutlinedTextField for Bangunan input
        OutlinedTextField(
            value = insertKmrUiEvent.idBangunan,
            onValueChange = { onValueChange(insertKmrUiEvent.copy(idBangunan = it)) },
            label = { Text("ID Bangunan") },
            placeholder = { Text("Masukkan ID Bangunan") },
            leadingIcon = {
                Icon(
                    painter = painterResource(R.drawable.iconasrama),
                    contentDescription = "Building Icon",
                    tint = Color(0xFF2196F3),
                    modifier = Modifier.size(28.dp)
                )
            },
            modifier = Modifier.fillMaxWidth(),
            isError = errorKmrState.idBangunan != null,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF03A9F4),
                unfocusedBorderColor = Color(0xFFB0BEC5),
                errorBorderColor = Color.Red
            )
        )

        if (errorKmrState.idBangunan != null) {
            Text(
                text = errorKmrState.idBangunan,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 8.dp, top = 2.dp)
            )
        }

        // Other fields for "Nomor Kamar", "Kapasitas", etc.
        OutlinedTextField(
            value = insertKmrUiEvent.nomorKamar,
            onValueChange = { onValueChange(insertKmrUiEvent.copy(nomorKamar = it)) },
            label = { Text("Nomor Kamar") },
            placeholder = { Text("Masukkan Nomor Kamar") },
            leadingIcon = {
                Icon(
                    painter = painterResource(R.drawable.iconkamar),
                    contentDescription = "Door Icon",
                    tint = Color(0xFFFF6F61),
                    modifier = Modifier.size(28.dp)
                )
            },
            modifier = Modifier.fillMaxWidth(),
            isError = errorKmrState.nomorKamar != null,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
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

        // Continue similar changes for other fields like "Kapasitas" and "Status Kamar"
        OutlinedTextField(
            value = insertKmrUiEvent.kapasitas,
            onValueChange = { onValueChange(insertKmrUiEvent.copy(kapasitas = it)) },
            label = { Text("Kapasitas") },
            placeholder = { Text("Masukkan Jumlah Kapasitas") },
            leadingIcon = {
                Icon(
                    painter = painterResource(R.drawable.iconkapasitas),
                    contentDescription = "Capacity Icon",
                    tint = Color(0xFF2196F3),
                    modifier = Modifier.size(28.dp)
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            isError = errorKmrState.kapasitas != null,
            singleLine = true,
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

        // Status Kamar radio buttons handled the same way
        StatusKamarRadioButton(
            statusKamar = insertKmrUiEvent.statusKamar,
            onStatusChanged = { onValueChange(insertKmrUiEvent.copy(statusKamar = it)) },
            error = errorKmrState.statusKamar
        )
    }
}

@Composable
fun StatusKamarRadioButton(
    statusKamar: String,
    onStatusChanged: (String) -> Unit,
    error: String? = null
) {
    // Define radio button options, assuming "Available" and "Not Available" status
    val statusOptions = listOf("Available", "Not Available")

    Column(modifier = Modifier.fillMaxWidth()) {
        // Loop through the options and create a Row for each radio button
        statusOptions.forEach { status ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = statusKamar == status,
                    onClick = { onStatusChanged(status) },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = Color(0xFF03A9F4),
                        unselectedColor = Color.Gray
                    )
                )
                Text(
                    text = status,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        // Display error message if provided
        error?.let {
            Text(
                text = it,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 8.dp, top = 2.dp)
            )
        }
    }
}

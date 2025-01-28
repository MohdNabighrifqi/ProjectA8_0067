package com.example.manajemenkamarmahasiswa.ui.view.Bangunan

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
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
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
import com.example.manajemenkamarmahasiswa.model.Bangunan
import com.example.manajemenkamarmahasiswa.ui.viewmodel.Bangunan.BangunanInsertUiEvent
import com.example.manajemenkamarmahasiswa.ui.viewmodel.Bangunan.BangunanInsertUiState
import com.example.manajemenkamarmahasiswa.ui.viewmodel.Bangunan.BangunanInsertViewModel
import com.example.manajemenkamarmahasiswa.ui.viewmodel.Bangunan.FormErrorBangunanState
import com.example.manajemenkamarmahasiswa.ui.viewmodel.PenyediaViewModel
import com.example.manajemenkamarmahasiswa.ui.widget.TopAppBar
import kotlinx.coroutines.launch

@Composable
fun BangunanInsertView(
    onClickBack: () -> Unit,
    onNavigate: () -> Unit,
    viewModel: BangunanInsertViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val uiState = viewModel.uiBangunanState
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(uiState.snackBarMessage) {
        uiState.snackBarMessage?.let { message ->
            coroutineScope.launch {
                snackbarHostState.showSnackbar(message)
                viewModel.resetSnackBarBgnMessage()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                onBack = onClickBack,
                showBackButton = true,
                judul = "input Bangunan",
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
                    InsertBodyBangunan(
                        uiState = uiState,
                        formTitle = "Form Input Bangunan",
                        onValueChange = { updatedEvent ->
                            viewModel.updateInsertBangunanState(updatedEvent)
                        },
                        onClick = {
                            coroutineScope.launch {
                                val isSaved = viewModel.insertBangunan()
                                if (isSaved) onNavigate()
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
fun InsertBodyBangunan(
    modifier: Modifier = Modifier,
    formTitle: String,
    onValueChange: (BangunanInsertUiEvent) -> Unit,
    uiState: BangunanInsertUiState,
    onClick: () -> Unit
) {
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

        FormBgnInput(
            InsertBangunanUiEvent = uiState.BangunanInsertUiEvent,
            onValueChange = onValueChange,
            errorBgnState = uiState.isBangunanEntryValid,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onClick,
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
fun FormBgnInput(
    InsertBangunanUiEvent: BangunanInsertUiEvent,
    modifier: Modifier = Modifier,
    onValueChange: (BangunanInsertUiEvent) -> Unit,
    errorBgnState: FormErrorBangunanState
) {
    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Nama bangunan
        OutlinedTextField(
            value = InsertBangunanUiEvent.namaBangunan,
            onValueChange = {
                onValueChange(InsertBangunanUiEvent.copy(namaBangunan = it))
            },
            label = { Text("Nama Bangunan") },
            placeholder = { Text("Masukkan nama Bangunan") },
            leadingIcon = {
                Icon(
                    Icons.Default.Home,
                    contentDescription = "Bangunan Icon",
                    tint = Color(0xFF2196F3) // Secondary color
                )
            },
            modifier = Modifier.fillMaxWidth(),
            isError = errorBgnState.namaBangunan != null,
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF03A9F4), // Focused border color
                unfocusedBorderColor = Color(0xFFB0BEC5), // Light border color
                errorBorderColor = Color.Red
            )
        )
        if (errorBgnState.namaBangunan != null) {
            Text(
                text = errorBgnState.namaBangunan,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 8.dp, top = 2.dp)
            )
        }

        // Alamat
        OutlinedTextField(
            value = InsertBangunanUiEvent.alamat,
            onValueChange = {
                onValueChange(InsertBangunanUiEvent.copy(alamat = it))
            },
            label = { Text("Alamat") },
            placeholder = { Text("Masukkan Alamat") },
            leadingIcon = {
                Icon(
                    Icons.Default.LocationOn,
                    contentDescription = "Alamat Icon",
                    tint = Color(0xFF2196F3) // Secondary color
                )
            },
            modifier = Modifier.fillMaxWidth(),
            isError = errorBgnState.alamat != null,
            singleLine = false,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF03A9F4), // Focused border color
                unfocusedBorderColor = Color(0xFFB0BEC5), // Light border color
                errorBorderColor = Color.Red
            )
        )
        if (errorBgnState.alamat != null) {
            Text(
                text = errorBgnState.alamat,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 8.dp, top = 2.dp)
            )
        }

        // Jumlah lantai
        OutlinedTextField(
            value = InsertBangunanUiEvent.jumlahLt,
            onValueChange = {
                onValueChange(InsertBangunanUiEvent.copy(jumlahLt = it))
            },
            label = { Text("Jumlah Lantai") },
            placeholder = { Text("Masukkan Jumlah Lantai") },
            leadingIcon = {
                Icon(
                    painter = painterResource(R.drawable.iconmahasiswa),
                    contentDescription = "Ikon Jumlah Lantai",
                    tint = Color(0xFF41A9FC),
                    modifier = Modifier.size(28.dp)
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            isError = errorBgnState.jumlahLt != null,
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF03A9F4),
                unfocusedBorderColor = Color(0xFFB0BEC5),
                errorBorderColor = Color.Red
            )
        )
        if (errorBgnState.jumlahLt != null) {
            Text(
                text = errorBgnState.jumlahLt,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 8.dp, top = 2.dp)
            )
        }
    }
}


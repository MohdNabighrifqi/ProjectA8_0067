package com.example.manajemenkamarmahasiswa.ui.view.Mahasiswa

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.manajemenkamarmahasiswa.R
import com.example.manajemenkamarmahasiswa.navigation.DestinasiMahasiswaEdit
import com.example.manajemenkamarmahasiswa.ui.viewmodel.Kamar.KamarHomeViewModel
import com.example.manajemenkamarmahasiswa.ui.viewmodel.Mahasiswa.FormErrorMahasiswaState
import com.example.manajemenkamarmahasiswa.ui.viewmodel.Mahasiswa.InsertMahasiswaUiEvent
import com.example.manajemenkamarmahasiswa.ui.viewmodel.Mahasiswa.InsertMahasiswaUiState
import com.example.manajemenkamarmahasiswa.ui.viewmodel.Mahasiswa.MahasiswaEditViewModel
import com.example.manajemenkamarmahasiswa.ui.viewmodel.PenyediaViewModel
import com.example.manajemenkamarmahasiswa.ui.widget.TopAppBar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun MahasiswaEditScreen(
    onClickBack: () -> Unit,
    onNavigate: () -> Unit,
    viewModel: MahasiswaEditViewModel = viewModel(factory = PenyediaViewModel.Factory),
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val uiState = viewModel.mahasiswaEditUiState

    LaunchedEffect(uiState.snackBarMessage) {
        uiState.snackBarMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.resetSnackBarMhsMessage()
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
                    InsertUpdateBodyMhs(
                        uiState = uiState,
                        formTitle = "Form ${DestinasiMahasiswaEdit.titleRes}",
                        onValueChange = { updatedMahasiswaEvent ->
                            viewModel.updateInsertMahasiswaState(updatedMahasiswaEvent)
                        },
                        onClick = {
                            val isSaved = viewModel.editMahasiswa()
                            if (isSaved) {
                                delay(1000)
                                onNavigate()
                            }
                        },
                        isReadOnly = false, // Allow editing
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun InsertUpdateBodyMhs(
    modifier: Modifier = Modifier,
    formTitle: String,
    onValueChange: (InsertMahasiswaUiEvent) -> Unit,
    uiState: InsertMahasiswaUiState,
    onClick: suspend () -> Unit,
    isReadOnly: Boolean = false,
) {
    val coroutineScope = rememberCoroutineScope()

    val kamar = uiState.insertMahasiswaUiEvent
    val idKmrMhs = kamar.idKamar

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

        FormMhsInputUpdate(
            insertMhsUiEvent = uiState.insertMahasiswaUiEvent,
            onValueChange = onValueChange,
            errorMhsState = uiState.isMahasiswaEntryValid,
            isReadOnly = isReadOnly,
            idKamarMahasiswa = idKmrMhs, // Use the value directly from UI State
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
fun FormMhsInputUpdate(
    insertMhsUiEvent: InsertMahasiswaUiEvent,
    modifier: Modifier = Modifier,
    onValueChange: (InsertMahasiswaUiEvent) -> Unit = {},
    errorMhsState: FormErrorMahasiswaState = FormErrorMahasiswaState(),
    isReadOnly: Boolean = false,
    idKamarMahasiswa: String
) {
    Column(
        modifier = modifier
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedTextField(
            value = insertMhsUiEvent.namaMahasiswa,
            onValueChange = {
                onValueChange(insertMhsUiEvent.copy(namaMahasiswa = it))
            },
            label = { Text("Nama Mahasiswa") },
            placeholder = { Text("Masukkan Nama Mahasiswa") },
            leadingIcon = {
                Icon(
                    Icons.Default.Person,
                    contentDescription = "Person Icon",
                    tint = Color(0xFF2196F3),
                    modifier = Modifier.size(28.dp)
                )
            },
            modifier = Modifier.fillMaxWidth(),
            isError = errorMhsState.namaMahasiswa != null,
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF03A9F4),
                unfocusedBorderColor = Color(0xFFB0BEC5),
                errorBorderColor = Color.Red
            )
        )
        if (errorMhsState.namaMahasiswa != null) {
            Text(
                text = errorMhsState.namaMahasiswa,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 8.dp, top = 2.dp)
            )
        }

        // NIM Field
        OutlinedTextField(
            value = insertMhsUiEvent.nim,
            onValueChange = {
                onValueChange(insertMhsUiEvent.copy(nim = it))
            },
            label = { Text("NIM") },
            placeholder = { Text("Masukkan NIM") },
            leadingIcon = {
                Icon(
                    Icons.Default.Info,
                    contentDescription = "Ikon NIM",
                    tint = Color(0xFF2196F3),
                    modifier = Modifier.size(28.dp)
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            isError = errorMhsState.nim != null,
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF03A9F4),
                unfocusedBorderColor = Color(0xFFB0BEC5),
                errorBorderColor = Color.Red
            )
        )

        if (errorMhsState.nim != null) {
            Text(
                text = errorMhsState.nim,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 8.dp, top = 2.dp)
            )
        }

        // Email Field
        OutlinedTextField(
            value = insertMhsUiEvent.email,
            onValueChange = {
                onValueChange(insertMhsUiEvent.copy(email = it))
            },
            label = { Text("Email") },
            placeholder = { Text("Masukkan Email") },
            leadingIcon = {
                Icon(
                    Icons.Default.MailOutline,
                    contentDescription = "Email Icon",
                    tint = Color(0xFF2196F3),
                    modifier = Modifier.size(28.dp)
                )
            },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            isError = errorMhsState.email != null,
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF03A9F4),
                unfocusedBorderColor = Color(0xFFB0BEC5),
                errorBorderColor = Color.Red
            )
        )

        if (errorMhsState.email != null) {
            Text(
                text = errorMhsState.email,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 8.dp, top = 2.dp)
            )
        }

        // Nomor HP Field
        OutlinedTextField(
            value = insertMhsUiEvent.noTelp,
            onValueChange = {
                onValueChange(insertMhsUiEvent.copy(noTelp = it))
            },
            label = { Text("Nomor HP") },
            placeholder = { Text("Nomor HP") },
            leadingIcon = {
                Icon(
                    Icons.Default.Phone,
                    contentDescription = "Ikon Nomor HP",
                    tint = Color(0xFF2196F3),
                    modifier = Modifier.size(28.dp)
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            isError = errorMhsState.noTelp != null,
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF03A9F4),
                unfocusedBorderColor = Color(0xFFB0BEC5),
                errorBorderColor = Color.Red
            )
        )

        if (errorMhsState.noTelp != null) {
            Text(
                text = errorMhsState.noTelp,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 8.dp, top = 2.dp)
            )
        }

        // Kamar (Room) field: Display the selected room number
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF5F5F5), shape = MaterialTheme.shapes.medium)
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.iconkamar),
                contentDescription = "Door Icon",
                modifier = Modifier
                    .size(48.dp)
                    .background(Color(0xFFDFF7F0), shape = MaterialTheme.shapes.small)
                    .padding(8.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))

            Column(
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Nomor Kamar",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF757575)
                )
                Text(
                    text = idKamarMahasiswa,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}

package com.example.manajemenkamarmahasiswa.ui.viewmodel.Mahasiswa


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.manajemenkamarmahasiswa.navigation.DestinasiMahasiswaEdit
import com.example.manajemenkamarmahasiswa.repository.MahasiswaRepository
import kotlinx.coroutines.launch

class MahasiswaEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val mahasiswaRepository: MahasiswaRepository
) : ViewModel() {
    var mahasiswaEditUiState by mutableStateOf(InsertMahasiswaUiState())
        private set

    private val _idMahasiswa: String = checkNotNull(savedStateHandle[DestinasiMahasiswaEdit.idMahasiswa])

    fun updateInsertMahasiswaState(insertMahasiswaUiEvent: InsertMahasiswaUiEvent) {
        if (!mahasiswaEditUiState.isEditing || insertMahasiswaUiEvent.idKamar == mahasiswaEditUiState.insertMahasiswaUiEvent.idKamar) {
            mahasiswaEditUiState = InsertMahasiswaUiState(insertMahasiswaUiEvent = insertMahasiswaUiEvent, isEditing = mahasiswaEditUiState.isEditing)
        }
    }

    init {
        viewModelScope.launch {
            val mahasiswa = mahasiswaRepository.getMahasiswaById(_idMahasiswa.toInt())
            mahasiswaEditUiState = mahasiswa.toUiStateMahasiswa().copy(isEditing = true)
        }
    }

    private fun validateMahasiswaFields() : Boolean {
        val event = mahasiswaEditUiState.insertMahasiswaUiEvent
        val errorMahasiswaState = FormErrorMahasiswaState(
            idKamar = if (event.idKamar.isNotEmpty()) null else "Masukkan Kamar",
            nim = if (event.nim.isNotEmpty()) null else "NIM mahasiswa tidak boleh kosong",
            namaMahasiswa = if (event.namaMahasiswa.isNotEmpty()) null else "Nama mahasiswa tidak boleh kosong",
            email = if (event.email.isNotEmpty()) null else "Email mahasiswa tidak boleh kosong",
            noTelp = if (event.noTelp.isNotEmpty()) null else "Nomor HP mahasiswa tidak boleh kosong",
        )

        mahasiswaEditUiState = mahasiswaEditUiState.copy(isMahasiswaEntryValid = errorMahasiswaState)
        return errorMahasiswaState.isMahasiswaValid()
    }

    suspend fun editMahasiswa() : Boolean {
        val currentMahasiswaEvent = mahasiswaEditUiState.insertMahasiswaUiEvent

        return if (validateMahasiswaFields()) {
            try {
                mahasiswaRepository.editMahasiswa(_idMahasiswa.toInt(), currentMahasiswaEvent.toMahasiswa())

                mahasiswaEditUiState = mahasiswaEditUiState.copy(
                    snackBarMessage = "Data berhasil diupdate",
                    insertMahasiswaUiEvent = InsertMahasiswaUiEvent(),
                    isMahasiswaEntryValid = FormErrorMahasiswaState()
                )
                true
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        } else {
            mahasiswaEditUiState = mahasiswaEditUiState.copy(snackBarMessage = "Input tidak valid, periksa data kembali")
            false
        }
    }
    fun resetSnackBarMhsMessage() {
        mahasiswaEditUiState = mahasiswaEditUiState.copy(snackBarMessage = null)
    }
}

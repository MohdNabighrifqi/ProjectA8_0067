package com.example.manajemenkamarmahasiswa.ui.viewmodel.Kamar

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.manajemenkamarmahasiswa.navigation.DestinasiKamarEdit
import com.example.manajemenkamarmahasiswa.repository.KamarRepository
import kotlinx.coroutines.launch

class KamarEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val kamarRepository: KamarRepository
) : ViewModel() {
    var KamarEditUiState by mutableStateOf(KamarInsertUiState()) // Menyimpan state KamarEditUiState
        private set

    private val _idKamar : String = checkNotNull(savedStateHandle[DestinasiKamarEdit.idKamar])

    // Fungsi untuk memperbarui state KamarEditUiState
    fun updateInsertKamarState(kamarInsertUiEvent: KamarInsertUiEvent) {
        if (!KamarEditUiState.isEditing || kamarInsertUiEvent.idBangunan == KamarEditUiState.kamarInsertUiEvent.idBangunan) {
            KamarEditUiState = KamarInsertUiState(kamarInsertUiEvent = kamarInsertUiEvent, isEditing = KamarEditUiState.isEditing)
        }
    }

    // Untuk mendapatkan data kamar ketika ViewModel ini diinisialisasi
    init {
        viewModelScope.launch {
            val kamar = kamarRepository.getKamarById(_idKamar.toInt())
            KamarEditUiState = kamar.toUiStateKamar().copy(isEditing = true) // Update UI state dengan data kamar
        }
    }

    // Fungsi validasi untuk mengecek apakah data kamar valid
    private fun validateKamarFields() : Boolean {
        val event = KamarEditUiState.kamarInsertUiEvent
        val errorKamarState = FormErrorKamarState(
            idBangunan = if (event.idBangunan.isNotEmpty()) null else "Masukkan Bangunan",
            nomorKamar = if (event.nomorKamar.isNotEmpty()) null else "Nomor kamar tidak boleh kosong",
            kapasitas = if (event.kapasitas.isNotEmpty()) null else "Kapasitas tidak boleh kosong",
            statusKamar = if (event.statusKamar.isNotEmpty()) null else "Status kamar tidak boleh kosong",
        )

        KamarEditUiState = KamarEditUiState.copy(isKamarEntryValid = errorKamarState)
        return errorKamarState.isKamarValid()
    }

    // Fungsi untuk memperbarui kamar, jika valid
    suspend fun updateKamar() : Boolean {
        val currentKamarEvent = KamarEditUiState.kamarInsertUiEvent

        return if (validateKamarFields()) {
            try {
                kamarRepository.updateKamar(_idKamar.toInt(), currentKamarEvent.toKamar())

                KamarEditUiState = KamarEditUiState.copy(
                    snackBarMessage = "Data berhasil diupdate",
                    kamarInsertUiEvent = KamarInsertUiEvent(),
                    isKamarEntryValid = FormErrorKamarState()
                )
                true
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        } else {
            KamarEditUiState = KamarEditUiState.copy(snackBarMessage = "Input tidak valid, periksa data kembali")
            false
        }
    }

    // Reset Snackbar setelah menampilkan pesan
    fun resetSnackBarKmrMessage() {
        KamarEditUiState = KamarEditUiState.copy(snackBarMessage = null)
    }
}

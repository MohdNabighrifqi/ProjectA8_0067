package com.example.manajemenkamarmahasiswa.ui.viewmodel.PembayaranSewa

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.manajemenkamarmahasiswa.navigation.DestinasiPembayaranEdit
import com.example.manajemenkamarmahasiswa.repository.PembayaranRepository
import kotlinx.coroutines.launch

class PembayaranEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val pembayaranRepository: PembayaranRepository
) : ViewModel() {
    var pembayaranEditUiState by mutableStateOf(PembayaranInsertUiState())
        private set

    private val _idPembayaran : String = checkNotNull(savedStateHandle[DestinasiPembayaranEdit.idPembayaran])

    fun updateInsertPembayaranState(pembayaranInsertUiEvent: PembayaranInsertUiEvent) {
        if (!pembayaranEditUiState.isEditing || pembayaranInsertUiEvent.idMahasiswa == pembayaranEditUiState.pembayaranInsertUiEvent.idPembayaran) {
            pembayaranEditUiState = PembayaranInsertUiState(pembayaranInsertUiEvent = pembayaranInsertUiEvent, isEditing = pembayaranEditUiState.isEditing)
        }
    }

    init {
        viewModelScope.launch {
            val pembayaran = pembayaranRepository.getPembayaranById(_idPembayaran.toInt())
            pembayaranEditUiState = pembayaran.toUiStatePembayaran().copy(isEditing = false)
        }
    }

    private fun validatePembayaranFields() : Boolean {
        val event = pembayaranEditUiState.pembayaranInsertUiEvent
        val errorPembayaranState = FormErrorPembayaranState(
            idMahasiswa = if (event.idMahasiswa.isNotEmpty()) null else "Mahasiswa tidak boleh kosong",
            jumlah = if (event.jumlah.isNotEmpty()) null else "Jumlah Pembayaran tidak boleh kosong",
            tanggal = if (event.tanggal.isNotEmpty()) null else "Tanggal Pembayaran tidak boleh kosong",
            status = if (event.status.isNotEmpty()) null else "Status Pembayaran tidak boleh kosong",
        )
        pembayaranEditUiState = pembayaranEditUiState.copy(isPembayaranEntryValid = errorPembayaranState)
        return errorPembayaranState.isPembayaranValid()
    }

    suspend fun editPembayaran() : Boolean {
        val currentPembayaranEvent = pembayaranEditUiState.pembayaranInsertUiEvent

        return if (validatePembayaranFields()) {
            try {
                pembayaranRepository.updatePembayaran(_idPembayaran.toInt(), currentPembayaranEvent.toPembayaran())

                pembayaranEditUiState = pembayaranEditUiState.copy(
                    snackBarMessage = "Data berhasil diupdate",
                    pembayaranInsertUiEvent = PembayaranInsertUiEvent(),
                    isPembayaranEntryValid = FormErrorPembayaranState()
                )
                true
            } catch (e:Exception) {
                e.printStackTrace()
                false
            }
        } else {
            pembayaranEditUiState = pembayaranEditUiState.copy(snackBarMessage = "Input tidak valid, periksa data kembali")
            false
        }
    }
    fun resetSnackBarPSMessage() {
        pembayaranEditUiState = pembayaranEditUiState.copy(snackBarMessage = null)
    }

}
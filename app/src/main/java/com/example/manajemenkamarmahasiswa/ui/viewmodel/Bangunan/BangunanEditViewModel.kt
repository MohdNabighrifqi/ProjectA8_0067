package com.example.manajemenkamarmahasiswa.ui.viewmodel.Bangunan

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.manajemenkamarmahasiswa.navigation.DestinasiBangunanEdit
import com.example.manajemenkamarmahasiswa.repository.BangunanRepository
import kotlinx.coroutines.launch

class BangunanEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val bangunanRepository: BangunanRepository
) : ViewModel() {
    var BangunanEditUiState by mutableStateOf(BangunanInsertUiState())
        private set

    private val _idBangunan : String = checkNotNull(savedStateHandle[DestinasiBangunanEdit.idBangunan])

    fun updateInsertBangunanState(BangunanInsertUiEvent: BangunanInsertUiEvent) {
        BangunanEditUiState = BangunanInsertUiState(BangunanInsertUiEvent = BangunanInsertUiEvent)
    }

    init {
        viewModelScope.launch {
            BangunanEditUiState = bangunanRepository.getBangunanById(_idBangunan.toInt())
                .toUiStateBangunan()
        }
    }

    private fun validateBangunanFields() : Boolean {
        val event = BangunanEditUiState.BangunanInsertUiEvent
        val errorBangunanState = FormErrorBangunanState(
            namaBangunan = if (event.namaBangunan.isNotEmpty()) null else "Nama Bangunan Tidak Boleh Kosong",
            alamat = if (event.alamat.isNotEmpty()) null else "Alamat tidak boleh kosong",
            jumlahLt = if (event.jumlahLt.isNotEmpty()) null else "Jumlah lantai tidak boleh kosong",
        )

        BangunanEditUiState = BangunanEditUiState.copy(isBangunanEntryValid = errorBangunanState)
        return errorBangunanState.isBangunanValid()
    }

    suspend fun updateBangunan() : Boolean {
        val currentBangunanState = BangunanEditUiState.BangunanInsertUiEvent

        return if (validateBangunanFields()) {
            try {
                bangunanRepository.updateBangunan(_idBangunan.toInt(), currentBangunanState.toBangunan())

                BangunanEditUiState = BangunanEditUiState.copy(
                    snackBarMessage = "Data berhasil diupdate",
                    BangunanInsertUiEvent = BangunanInsertUiEvent(),
                    isBangunanEntryValid = FormErrorBangunanState()
                )
                true
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        } else {
            BangunanEditUiState = BangunanEditUiState.copy(snackBarMessage = "Input tidak valid, periksa data kembali")
            false
        }
    }
    fun resetSnackBarBgnMessage() {
        BangunanEditUiState = BangunanEditUiState.copy(snackBarMessage = null)
    }
}

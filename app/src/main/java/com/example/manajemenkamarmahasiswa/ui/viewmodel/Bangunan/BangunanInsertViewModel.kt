package com.example.manajemenkamarmahasiswa.ui.viewmodel.Bangunan

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.manajemenkamarmahasiswa.model.Bangunan
import com.example.manajemenkamarmahasiswa.repository.BangunanRepository

class BangunanInsertViewModel (
    private val bangunanRepository: BangunanRepository
) : ViewModel() {
    var uiBangunanState by mutableStateOf(BangunanInsertUiState())
        private set
    fun updateInsertBangunanState(BangunanInsertUiEvent: BangunanInsertUiEvent) {
        uiBangunanState = BangunanInsertUiState(BangunanInsertUiEvent =  BangunanInsertUiEvent)
    }

    private fun validateBgnFields() : Boolean {
        val event = uiBangunanState. BangunanInsertUiEvent
        val errorBangunanState = FormErrorBangunanState(
            namaBangunan = if (event.namaBangunan.isNotEmpty()) null else "Nama Bangunan Tidak Boleh Kosong",
            alamat = if (event.alamat.isNotEmpty()) null else "Alamat tidak boleh kosong",
            jumlahLt = if (event.jumlahLt.isNotEmpty()) null else "Jumlah lantai tidak boleh kosong",
        )

        uiBangunanState = uiBangunanState.copy(isBangunanEntryValid = errorBangunanState)
        return errorBangunanState.isBangunanValid()
    }

    suspend fun insertBangunan() : Boolean {
        val currentBangunanEvent = uiBangunanState. BangunanInsertUiEvent

        return if (validateBgnFields()) {
            try {
                bangunanRepository.insertBangunan(currentBangunanEvent.toBangunan())
                uiBangunanState = uiBangunanState.copy(
                    snackBarMessage = "Data berhasil disimpan",
                    BangunanInsertUiEvent = BangunanInsertUiEvent(),
                    isBangunanEntryValid = FormErrorBangunanState()
                )
                true
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        } else {
            uiBangunanState = uiBangunanState.copy(snackBarMessage = "Input tidak valid, periksa data kembali")
            false
        }
    }
    fun resetSnackBarBgnMessage() {
        uiBangunanState = uiBangunanState.copy(snackBarMessage = null)
    }
}

data class BangunanInsertUiState(
    val BangunanInsertUiEvent: BangunanInsertUiEvent = BangunanInsertUiEvent(),
    val isBangunanEntryValid : FormErrorBangunanState = FormErrorBangunanState(),
    val snackBarMessage : String? = null
)

data class FormErrorBangunanState(
    val namaBangunan: String? = null,
    val alamat: String? = null,
    val jumlahLt: String? = null
) {
    fun isBangunanValid() : Boolean {
        return namaBangunan == null && alamat == null &&
                jumlahLt == null
    }
}

fun Bangunan.toBangunanUiInsertEvent() : BangunanInsertUiEvent = BangunanInsertUiEvent (
    idBangunan = idBangunan.toString(),
    namaBangunan = namaBangunan,
    alamat = alamat,
    jumlahLt = jumlahLt.toString()
)

fun Bangunan.toUiStateBangunan() : BangunanInsertUiState = BangunanInsertUiState(
    BangunanInsertUiEvent = toBangunanUiInsertEvent()
)

fun BangunanInsertUiEvent.toBangunan(): Bangunan = Bangunan (
    idBangunan = idBangunan.toIntOrNull() ?:0,
    namaBangunan = namaBangunan,
    alamat = alamat,
    jumlahLt = jumlahLt.toIntOrNull() ?:0
)

data class BangunanInsertUiEvent(
    val idBangunan: String = "",
    val namaBangunan: String = "",
    val alamat: String = "",
    val jumlahLt: String = ""
)
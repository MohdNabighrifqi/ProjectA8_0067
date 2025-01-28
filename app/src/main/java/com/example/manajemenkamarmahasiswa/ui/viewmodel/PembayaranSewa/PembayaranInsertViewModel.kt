package com.example.manajemenkamarmahasiswa.ui.viewmodel.PembayaranSewa

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.manajemenkamarmahasiswa.model.Pembayaran
import com.example.manajemenkamarmahasiswa.repository.PembayaranRepository

class PembayaranInsertViewModel (
    private val pembayaranRepository: PembayaranRepository
) : ViewModel() {
    var uiPembayaranState by mutableStateOf(PembayaranInsertUiState())
        private set
    fun updateInsertPembayaranState(pembayaranInsertUiEvent: PembayaranInsertUiEvent) {
        uiPembayaranState = PembayaranInsertUiState(pembayaranInsertUiEvent = pembayaranInsertUiEvent)
    }

    private fun validatePembayaranFields() : Boolean {
        val event = uiPembayaranState.pembayaranInsertUiEvent
        val errorPembayaranState = FormErrorPembayaranState(
            idMahasiswa = if (event.idMahasiswa.isNotEmpty()) null else "Mahasiswa tidak boleh kosong",
            jumlah = if (event.jumlah.isNotEmpty()) null else "Jumlah Pembayaran tidak boleh kosong",
            tanggal = if (event.tanggal.isNotEmpty()) null else "Tanggal Pembayaran tidak boleh kosong",
            status = if (event.status.isNotEmpty()) null else "Status Pembayaran tidak boleh kosong",
        )

        uiPembayaranState = uiPembayaranState.copy(isPembayaranEntryValid = errorPembayaranState)
        return errorPembayaranState.isPembayaranValid()
    }

    suspend fun insertPembayaran() : Boolean {
        val currentPembayaranEvent = uiPembayaranState.pembayaranInsertUiEvent

        return if (validatePembayaranFields()) {
            try {
                pembayaranRepository.insertPembayaran(currentPembayaranEvent.toPembayaran())
                uiPembayaranState = uiPembayaranState.copy(
                    snackBarMessage = "Data berhasil disimpan",
                    pembayaranInsertUiEvent = PembayaranInsertUiEvent(),
                    isPembayaranEntryValid = FormErrorPembayaranState()
                )
                true
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        } else {
            uiPembayaranState = uiPembayaranState.copy(snackBarMessage = "Input tidak valid, periksa data kembali")
            false
        }
    }
    fun resetSnackBarPembayaranMessage() {
        uiPembayaranState = uiPembayaranState.copy(snackBarMessage = null)
    }
}

data class PembayaranInsertUiState(
    val pembayaranInsertUiEvent: PembayaranInsertUiEvent = PembayaranInsertUiEvent(),
    val isPembayaranEntryValid: FormErrorPembayaranState = FormErrorPembayaranState(),
    val snackBarMessage: String? = null,
    val isEditing: Boolean = true,
)


data class FormErrorPembayaranState(
    val idMahasiswa : String ? = null,
    val jumlah : String ? = null,
    val tanggal : String ? = null,
    val status : String ? = null,
) {
    fun isPembayaranValid() : Boolean {
        return idMahasiswa == null && jumlah == null &&
                tanggal == null && status == null
    }
}

fun Pembayaran.toInsertUiPembayaranEvent() : PembayaranInsertUiEvent = PembayaranInsertUiEvent (
    idPembayaran = idPembayaran.toString(),
    idMahasiswa = idMahasiswa.toString(),
    jumlah = jumlah.toString(),
    tanggal = tanggal,
    status = status
)

fun Pembayaran.toUiStatePembayaran() : PembayaranInsertUiState = PembayaranInsertUiState(
    pembayaranInsertUiEvent = toInsertUiPembayaranEvent()
)

fun PembayaranInsertUiEvent.toPembayaran(): Pembayaran = Pembayaran (
    idPembayaran = idPembayaran.toIntOrNull() ?:0,
    idMahasiswa = idMahasiswa.toIntOrNull() ?:0,
    jumlah = jumlah.toIntOrNull() ?:0,
    tanggal = tanggal,
    status = status
)

data class PembayaranInsertUiEvent(
    val idPembayaran: String = "",
    val idMahasiswa : String = "",
    val jumlah : String = "",
    val tanggal : String = "",
    val status : String = ""
)
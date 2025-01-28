package com.example.manajemenkamarmahasiswa.ui.viewmodel.Kamar

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.manajemenkamarmahasiswa.model.Kamar
import com.example.manajemenkamarmahasiswa.repository.KamarRepository

class KamarInsertViewModel (
    private val kamarRepository: KamarRepository
) : ViewModel() {
    var uiKamarState by mutableStateOf(KamarInsertUiState())
        private set
    fun updateInsertKamarState(kamarInsertUiEvent: KamarInsertUiEvent) {
        uiKamarState = KamarInsertUiState(kamarInsertUiEvent = kamarInsertUiEvent)
    }

    private fun validateKamarFields() : Boolean {
        val event = uiKamarState.kamarInsertUiEvent
        val errorKamarState = FormErrorKamarState(
            idBangunan = if (event.idBangunan.isNotEmpty()) null else "Masukkan Bangunan",
            nomorKamar = if (event.nomorKamar.isNotEmpty()) null else "Nomor kamar tidak boleh kosong",
            kapasitas = if (event.kapasitas.isNotEmpty()) null else "Kapasitas tidak boleh kosong",
            statusKamar = if (event.statusKamar.isNotEmpty()) null else "Status kamar tidak boleh kosong",
        )

        uiKamarState = uiKamarState.copy(isKamarEntryValid = errorKamarState)
        return errorKamarState.isKamarValid()
    }

    suspend fun insertKamar() : Boolean {
        val currentKmrEvent = uiKamarState.kamarInsertUiEvent

        return if (validateKamarFields()) {
            try {
                kamarRepository.insertKamar(currentKmrEvent.toKamar())
                uiKamarState = uiKamarState.copy(
                    snackBarMessage = "Data berhasil disimpan",
                    kamarInsertUiEvent = KamarInsertUiEvent(),
                    isKamarEntryValid = FormErrorKamarState()
                )
                true
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        } else {
            uiKamarState = uiKamarState.copy(snackBarMessage = "Input tidak valid, periksa data kembali")
            false
        }
    }
    fun resetSnackBarKmrMessage() {
        uiKamarState = uiKamarState.copy(snackBarMessage = null)
    }
}

data class KamarInsertUiState(
    val kamarInsertUiEvent: KamarInsertUiEvent = KamarInsertUiEvent(),
    val isKamarEntryValid: FormErrorKamarState = FormErrorKamarState(),
    val snackBarMessage: String? = null,
    val isEditing: Boolean = false,
)


data class FormErrorKamarState(
    val idBangunan: String? = null,
    val nomorKamar: String? = null,
    val kapasitas: String? = null,
    val statusKamar: String? = null
) {
    fun isKamarValid() : Boolean {
        return idBangunan == null && nomorKamar == null &&
                kapasitas == null && statusKamar == null
    }
}

fun Kamar.toKamarUiInsertEvent() : KamarInsertUiEvent = KamarInsertUiEvent (
    idKamar = idKamar.toString(),
    idBangunan = idBangunan.toString(),
    nomorKamar = nomorKamar,
    kapasitas = kapasitas.toString(),
    statusKamar = statusKamar
)

fun Kamar.toUiStateKamar() : KamarInsertUiState = KamarInsertUiState(
    kamarInsertUiEvent = toKamarUiInsertEvent()
)

fun KamarInsertUiEvent.toKamar(): Kamar = Kamar (
    idKamar = idKamar.toIntOrNull() ?:0,
    idBangunan = idBangunan.toIntOrNull() ?:0,
    nomorKamar = nomorKamar,
    kapasitas = kapasitas.toIntOrNull() ?:0,
    statusKamar = statusKamar
)

data class KamarInsertUiEvent(
    val idKamar: String = "",
    val nomorKamar : String = "",
    val kapasitas : String = "",
    val statusKamar : String = "",
    val idBangunan : String = ""
)
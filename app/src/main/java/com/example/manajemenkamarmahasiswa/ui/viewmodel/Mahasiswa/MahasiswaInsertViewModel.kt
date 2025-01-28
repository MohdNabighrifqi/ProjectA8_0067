package com.example.manajemenkamarmahasiswa.ui.viewmodel.Mahasiswa

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.manajemenkamarmahasiswa.model.Mahasiswa
import com.example.manajemenkamarmahasiswa.repository.MahasiswaRepository

class MahasiswaInsertViewModel (
    private val mahasiswaRepository: MahasiswaRepository
) : ViewModel() {
    var uiMahasiswaState by mutableStateOf(InsertMahasiswaUiState())
        private set
    fun updateInsertMahasiswaState(insertMahasiswaUiEvent: InsertMahasiswaUiEvent) {
        uiMahasiswaState = InsertMahasiswaUiState(insertMahasiswaUiEvent = insertMahasiswaUiEvent)
    }

    private fun validateMahasiswaFields() : Boolean {
        val event = uiMahasiswaState.insertMahasiswaUiEvent
        val errorMahasiswaState = FormErrorMahasiswaState(
            idKamar = if (event.idKamar.isNotEmpty()) null else "Masukkan Kamar",
            nim = if (event.nim.isNotEmpty()) null else "NIM mahasiswa tidak boleh kosong",
            namaMahasiswa = if (event.namaMahasiswa.isNotEmpty()) null else "Nama mahasiswa tidak boleh kosong",
            email = if (event.email.isNotEmpty()) null else "Email mahasiswa tidak boleh kosong",
            noTelp = if (event.noTelp.isNotEmpty()) null else "Nomor HP mahasiswa tidak boleh kosong",
        )

        uiMahasiswaState = uiMahasiswaState.copy(isMahasiswaEntryValid = errorMahasiswaState)
        return errorMahasiswaState.isMahasiswaValid()
    }

    suspend fun insertMahasiswa() : Boolean {
        val currentMahasiswaEvent = uiMahasiswaState.insertMahasiswaUiEvent

        return if (validateMahasiswaFields()) {
            try {
                mahasiswaRepository.insertMahasiswa(currentMahasiswaEvent.toMahasiswa())
                uiMahasiswaState = uiMahasiswaState.copy(
                    snackBarMessage = "Data berhasil disimpan",
                    insertMahasiswaUiEvent = InsertMahasiswaUiEvent(),
                    isMahasiswaEntryValid = FormErrorMahasiswaState()
                )
                true
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        } else {
            uiMahasiswaState = uiMahasiswaState.copy(snackBarMessage = "Input tidak valid, periksa data kembali")
            false
        }
    }
    fun resetSnackBarMhsMessage() {
        uiMahasiswaState = uiMahasiswaState.copy(snackBarMessage = null)
    }


    }

data class InsertMahasiswaUiState(
    val insertMahasiswaUiEvent: InsertMahasiswaUiEvent = InsertMahasiswaUiEvent(),
    val isMahasiswaEntryValid: FormErrorMahasiswaState = FormErrorMahasiswaState(),
    val snackBarMessage: String? = null,
    val isEditing: Boolean = false,
)


data class FormErrorMahasiswaState(
    val idKamar: String? = null,
    val nim: String? = null,
    val namaMahasiswa: String? = null,
    val email: String? = null,
    val noTelp: String? = null,
) {
    fun isMahasiswaValid() : Boolean {
        return idKamar == null && nim == null && namaMahasiswa == null &&
                email == null && noTelp == null
    }
}

fun Mahasiswa.toInsertUiMahasiswaEvent() : InsertMahasiswaUiEvent = InsertMahasiswaUiEvent (
    idMahasiswa = idMahasiswa.toString(),
    nim = nim,
    namaMahasiswa = namaMahasiswa,
    email = email,
    noTelp = noTelp,
    idKamar = idKamar.toString(),
)

fun Mahasiswa.toUiStateMahasiswa() : InsertMahasiswaUiState = InsertMahasiswaUiState(
    insertMahasiswaUiEvent = toInsertUiMahasiswaEvent()
)

fun InsertMahasiswaUiEvent.toMahasiswa(): Mahasiswa = Mahasiswa (
    idMahasiswa = idMahasiswa.toIntOrNull() ?:0,
    nim = nim,
    namaMahasiswa = namaMahasiswa,
    email = email,
    noTelp = noTelp,
    idKamar = idKamar.toIntOrNull() ?:0,
)

data class InsertMahasiswaUiEvent(
    val idMahasiswa: String = "",
    val nim : String = "",
    val namaMahasiswa : String = "",
    val email : String = "",
    val noTelp : String = "",
    val idKamar : String = ""
)
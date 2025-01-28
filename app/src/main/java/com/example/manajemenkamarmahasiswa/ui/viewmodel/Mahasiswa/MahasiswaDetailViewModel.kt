package com.example.manajemenkamarmahasiswa.ui.viewmodel.Mahasiswa

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.manajemenkamarmahasiswa.model.Mahasiswa
import com.example.manajemenkamarmahasiswa.navigation.DestinasiMahasiswaDetail
import com.example.manajemenkamarmahasiswa.repository.MahasiswaRepository
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException


sealed class MahasiswaDetailUiState {
    data class Success(val mahasiswa: Mahasiswa) : MahasiswaDetailUiState()
    object Error : MahasiswaDetailUiState()
    object Loading : MahasiswaDetailUiState()
}

class MahasiswaDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val mahasiswa : MahasiswaRepository
) : ViewModel() {
    private val _idMahasiswa : String = checkNotNull(savedStateHandle[DestinasiMahasiswaDetail.idMahasiswa])

    var mahasiswaDetailState: MahasiswaDetailUiState by mutableStateOf(MahasiswaDetailUiState.Loading)
        private set

    init {
        getMahasiswaById()
    }

    fun getMahasiswaById() {
        viewModelScope.launch {
            mahasiswaDetailState = try {
                val mahasiswa = mahasiswa.getMahasiswaById(_idMahasiswa.toInt())
                MahasiswaDetailUiState.Success(mahasiswa)
            } catch (e: IOException) {
                MahasiswaDetailUiState.Error
            } catch (e: HttpException) {
                e.printStackTrace()
                MahasiswaDetailUiState.Error
            }
        }
    }
}
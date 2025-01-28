package com.example.manajemenkamarmahasiswa.ui.viewmodel.Mahasiswa

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.manajemenkamarmahasiswa.model.Mahasiswa
import com.example.manajemenkamarmahasiswa.repository.MahasiswaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException

sealed class HomeUiState{
    data class Success(val mahasiswa: List<Mahasiswa>) : HomeUiState()
    object Error: HomeUiState()
    object Loading: HomeUiState()
}

class MahasiswaHomeViewModel (private val mahasiswa: MahasiswaRepository): ViewModel(){

    private val _mahasiswaUIState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)

    val mahasiswaUIState : StateFlow<HomeUiState> get() = _mahasiswaUIState

    init {
        getMahasiswa()
    }
    fun getMahasiswa(){
        viewModelScope.launch {
            _mahasiswaUIState.value = HomeUiState.Loading
            _mahasiswaUIState.value = try {
                HomeUiState.Success(mahasiswa.getMahasiswa().data)
            } catch (e: IOException){
                HomeUiState.Error
            } catch (e: HttpException){
                HomeUiState.Error
            }
        }
    }
    fun deleteMahasiswa(idMahasiswa: Int) {
        viewModelScope.launch{
            try {
                mahasiswa.deleteMahasiswa(idMahasiswa)
            } catch (e: IOException){
                HomeUiState.Error
            } catch (e: HttpException){
                HomeUiState.Error
            }
        }
    }
}
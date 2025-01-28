package com.example.manajemenkamarmahasiswa.ui.viewmodel.Kamar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.manajemenkamarmahasiswa.model.Kamar
import com.example.manajemenkamarmahasiswa.repository.KamarRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException

sealed class HomeUiState{
    data class Success(val kamar: List<Kamar>) : HomeUiState()
    object Error: HomeUiState()
    object Loading: HomeUiState()
}

class KamarHomeViewModel (private val kamar: KamarRepository): ViewModel(){

    private val _kamarUIState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)

    val kamarUIState: StateFlow<HomeUiState> get() = _kamarUIState

    init {
        getKamar()
    }
    fun getKamar(){
        viewModelScope.launch {
            _kamarUIState.value = HomeUiState.Loading
            _kamarUIState.value = try {
                val kamarList = kamar.getKamar().data
                HomeUiState.Success(kamarList)
            } catch (e: IOException){
                HomeUiState.Error
            } catch (e: HttpException){
                HomeUiState.Error
            }
        }
    }
    fun deleteKamar(idkamar: Int) {
        viewModelScope.launch{
            try {
                kamar.deleteKamar(idkamar)
            } catch (e: IOException){
                HomeUiState.Error
            } catch (e: HttpException){
                HomeUiState.Error
            }
        }
    }
}
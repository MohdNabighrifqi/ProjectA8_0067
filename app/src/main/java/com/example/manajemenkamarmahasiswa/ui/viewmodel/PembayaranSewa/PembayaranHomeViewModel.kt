package com.example.manajemenkamarmahasiswa.ui.viewmodel.PembayaranSewa

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.manajemenkamarmahasiswa.model.Pembayaran
import com.example.manajemenkamarmahasiswa.repository.PembayaranRepository
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException

sealed class HomeUiState{
    data class Success(val Pembayaran: List<Pembayaran>) : HomeUiState()
    object Error: HomeUiState()
    object Loading: HomeUiState()
}

class PembayaranHomeViewModel (private val pembayaran: PembayaranRepository): ViewModel(){
    var pembayaranUIState: HomeUiState by mutableStateOf(HomeUiState.Loading)
        private set
    init {
        getPembayaran()
    }
    fun getPembayaran(){
        viewModelScope.launch {
            pembayaranUIState = HomeUiState.Loading
            pembayaranUIState = try {
                HomeUiState.Success(pembayaran.getPembayaran().data)
            } catch (e: IOException){
                HomeUiState.Error
            } catch (e: HttpException){
                HomeUiState.Error
            }
        }
    }
    fun deletePembayaran(idPembayaran: Int) {
        viewModelScope.launch{
            try {
                pembayaran.deletePembayaran(idPembayaran)
            } catch (e: IOException){
                HomeUiState.Error
            } catch (e: HttpException){
                HomeUiState.Error
            }
        }
    }
}
package com.example.manajemenkamarmahasiswa.ui.viewmodel.PembayaranSewa

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.manajemenkamarmahasiswa.model.Pembayaran
import com.example.manajemenkamarmahasiswa.navigation.DestinasiPembayaranDetail
import com.example.manajemenkamarmahasiswa.repository.PembayaranRepository
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException

sealed class PembayaranDetailUiState {
    data class Success(val pembayaran: Pembayaran) : PembayaranDetailUiState()
    object Error : PembayaranDetailUiState()
    object Loading : PembayaranDetailUiState()
}

class PembayaranDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val pembayaran: PembayaranRepository
) : ViewModel() {

    private val _idPembayaran: String? = savedStateHandle[DestinasiPembayaranDetail.idPembayaran]
    private val _idMahasiswa: String? = savedStateHandle[DestinasiPembayaranDetail.idMahasiswa]

    var pembayaranDetailState: PembayaranDetailUiState by mutableStateOf(PembayaranDetailUiState.Loading)
        private set

    init {
        when {
            _idPembayaran != null -> getPembayaranById(_idPembayaran)
            _idMahasiswa != null -> getPembayaranByIdMhs(_idMahasiswa)
            else -> pembayaranDetailState = PembayaranDetailUiState.Error
        }
    }

    fun getPembayaranById(idPembayaran: String) {
        viewModelScope.launch {
            pembayaranDetailState = try {
                val pembayaran = pembayaran.getPembayaranById(idPembayaran.toInt())
                PembayaranDetailUiState.Success(pembayaran)
            } catch (e: IOException) {
                PembayaranDetailUiState.Error
            } catch (e: HttpException) {
                e.printStackTrace()
                PembayaranDetailUiState.Error
            }
        }
    }

    fun getPembayaranByIdMhs(idMhs: String) {
        viewModelScope.launch {
            pembayaranDetailState = try {
                val pembayaran = pembayaran.getPembayaranByIdMhs(idMhs.toInt())
                PembayaranDetailUiState.Success(pembayaran)
            } catch (e: IOException) {
                PembayaranDetailUiState.Error
            } catch (e: HttpException) {
                e.printStackTrace()
                PembayaranDetailUiState.Error
            }
        }
    }
}

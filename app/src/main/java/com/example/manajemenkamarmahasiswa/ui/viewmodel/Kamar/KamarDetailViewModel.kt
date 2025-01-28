package com.example.manajemenkamarmahasiswa.ui.viewmodel.Kamar

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.manajemenkamarmahasiswa.model.Kamar
import com.example.manajemenkamarmahasiswa.navigation.DestinasiKamarDetail
import com.example.manajemenkamarmahasiswa.repository.KamarRepository
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException

sealed class KamarDetailUiState {
    data class Success(val kamar: Kamar) : KamarDetailUiState()
    object Error : KamarDetailUiState()
    object Loading :KamarDetailUiState()
}

class KamarDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val kamar : KamarRepository
) : ViewModel() {
    private val _idKamar: String = checkNotNull(savedStateHandle[DestinasiKamarDetail.idKamar])

    var kamarDetailState: KamarDetailUiState by mutableStateOf(KamarDetailUiState.Loading)
        private set

    init {
        getKamarById()
    }

    fun getKamarById() {
        viewModelScope.launch {
            kamarDetailState = try {
                val kamar = kamar.getKamarById(_idKamar.toInt())
                KamarDetailUiState.Success(kamar)
            } catch (e: IOException) {
                KamarDetailUiState.Error
            } catch (e: HttpException) {
                e.printStackTrace()
                KamarDetailUiState.Error
            }
        }
    }
}
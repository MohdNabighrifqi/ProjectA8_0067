package com.example.manajemenkamarmahasiswa.ui.viewmodel.Bangunan

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.manajemenkamarmahasiswa.model.Bangunan
import com.example.manajemenkamarmahasiswa.navigation.DestinasiBangunanDetail
import com.example.manajemenkamarmahasiswa.repository.BangunanRepository
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException

sealed class BangunanDetailUiState {
    data class Success(val bangunan: Bangunan) : BangunanDetailUiState()
    object Error : BangunanDetailUiState()
    object Loading : BangunanDetailUiState()
}

class BangunanDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val bangunan : BangunanRepository
) : ViewModel() {
    private val _idBangunan: String = checkNotNull(savedStateHandle[DestinasiBangunanDetail.idBangunan])

    var bangunanDetailState: BangunanDetailUiState by mutableStateOf(BangunanDetailUiState.Loading)
        private set

    init {
        getBangunanById()
    }

    fun getBangunanById() {
        viewModelScope.launch {
            bangunanDetailState = try {
                val bangunan = bangunan.getBangunanById(_idBangunan.toInt())
                BangunanDetailUiState.Success(bangunan)
            } catch (e: IOException) {
                BangunanDetailUiState.Error
            } catch (e: HttpException) {
                e.printStackTrace()
                BangunanDetailUiState.Error
            }
        }
    }

}
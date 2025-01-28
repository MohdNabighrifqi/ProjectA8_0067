package com.example.manajemenkamarmahasiswa.ui.viewmodel.Bangunan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.manajemenkamarmahasiswa.model.Bangunan
import com.example.manajemenkamarmahasiswa.repository.BangunanRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException

sealed class HomeUiState {
    data class Success(val bangunan: List<Bangunan>) : HomeUiState()
    object Error : HomeUiState()
    object Loading : HomeUiState()
}


class BangunanHomeViewModel(private val bangunan: BangunanRepository) : ViewModel() {

    private val _bangunanUIState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val bangunanUIState: StateFlow<HomeUiState> get() = _bangunanUIState

    init {
        getBangunan()
    }

    fun getBangunan() {
        viewModelScope.launch {
            _bangunanUIState.value = HomeUiState.Loading
            _bangunanUIState.value = try {
                val bangunanList = bangunan.getBangunan().data
                HomeUiState.Success(bangunanList)
            } catch (e: IOException) {
                HomeUiState.Error
            } catch (e: HttpException) {
                HomeUiState.Error
            }
        }
    }

    fun deleteBangunan(idBangunan: Int) {
        viewModelScope.launch {
            try {
                bangunan.deleteBangunan(idBangunan)
                getBangunan()
            } catch (e: IOException) {
                _bangunanUIState.value = HomeUiState.Error
            } catch (e: HttpException) {
                _bangunanUIState.value = HomeUiState.Error
            }
        }
    }
}


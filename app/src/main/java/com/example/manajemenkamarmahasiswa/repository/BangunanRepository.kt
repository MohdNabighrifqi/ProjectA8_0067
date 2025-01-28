package com.example.manajemenkamarmahasiswa.repository

import com.example.manajemenkamarmahasiswa.model.Bangunan
import com.example.manajemenkamarmahasiswa.model.BangunanResponse
import com.example.manajemenkamarmahasiswa.service.BangunanService
import okio.IOException

interface BangunanRepository{
    suspend fun getBangunan() : BangunanResponse
    suspend fun insertBangunan(bangunan: Bangunan)
    suspend fun updateBangunan(idBangunan: Int, bangunan: Bangunan)
    suspend fun deleteBangunan(idBangunan: Int)
    suspend fun getBangunanById(idBangunan: Int): Bangunan
}

class NetworkBangunanRepository(
    private val bangunanApiService : BangunanService
) : BangunanRepository {
    override suspend fun getBangunan(): BangunanResponse {
        return try {
            bangunanApiService.getBangunan()
        } catch (e: Exception) {
            throw IOException("Failed to fetch Bangunan data : ${e.message}")
        }
    }

    override suspend fun insertBangunan(bangunan: Bangunan) {
        bangunanApiService.insertBangunan(bangunan)
    }

    override suspend fun updateBangunan(idBangunan: Int, bangunan: Bangunan) {
        bangunanApiService.updateBangunan(idBangunan, bangunan)
    }

    override suspend fun deleteBangunan(idBangunan: Int) {
        try {
            val response = bangunanApiService.deleteBangunan(idBangunan)
            if (!response.isSuccessful) {
                throw IOException("gagal menghapus data Bangunan. HTTP kode: ${response.code()}")
            } else {
                response.message()
                println(response.message())
            }
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun getBangunanById(idBangunan: Int): Bangunan {
        return bangunanApiService.getBangunanById(idBangunan).data
    }

}

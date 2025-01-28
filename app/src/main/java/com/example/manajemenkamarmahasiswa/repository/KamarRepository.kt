package com.example.manajemenkamarmahasiswa.repository

import com.example.manajemenkamarmahasiswa.model.Kamar
import com.example.manajemenkamarmahasiswa.model.KamarResponse
import com.example.manajemenkamarmahasiswa.service.KamarService
import okio.IOException

interface KamarRepository{
    suspend fun getKamar() : KamarResponse
    suspend fun insertKamar(kamar: Kamar)
    suspend fun updateKamar(idKamar: Int, kamar: Kamar)
    suspend fun deleteKamar(idKamar: Int)
    suspend fun getKamarById(idKamar: Int): Kamar
}

class NetworkKamarRepository(
    private val kamarApiService : KamarService
) : KamarRepository {
    override suspend fun getKamar(): KamarResponse {
        return try {
            kamarApiService.getKamar()
        } catch (e: Exception) {
            throw IOException("Failed to fetch kamar data : ${e.message}")
        }
    }

    override suspend fun insertKamar(kamar: Kamar) {
        kamarApiService.insertKamar(kamar)
    }

    override suspend fun updateKamar(idKamar: Int, kamar: Kamar) {
        kamarApiService.updateKamar(idKamar, kamar)
    }

    override suspend fun deleteKamar(idKamar: Int) {
        try {
            val response = kamarApiService.deleteKamar(idKamar)
            if (!response.isSuccessful) {
                throw IOException("gagal menghapus data kamar. HTTP kode: ${response.code()}")
            } else {
                response.message()
                println(response.message())
            }
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun getKamarById(idKamar: Int): Kamar {
        return kamarApiService.getKamarById(idKamar).data
    }

}

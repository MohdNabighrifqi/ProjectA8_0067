package com.example.manajemenkamarmahasiswa.repository

import com.example.manajemenkamarmahasiswa.model.Pembayaran
import com.example.manajemenkamarmahasiswa.model.PembayaranResponse
import com.example.manajemenkamarmahasiswa.service.PembayaranService
import okio.IOException

interface PembayaranRepository{
    suspend fun getPembayaran() : PembayaranResponse
    suspend fun insertPembayaran(pembayaran: Pembayaran)
    suspend fun updatePembayaran(idPembayaran: Int, pembayaran: Pembayaran)
    suspend fun deletePembayaran(idPembayaran: Int)
    suspend fun getPembayaranById(idPembayaran: Int): Pembayaran
    suspend fun getPembayaranByIdMhs(idMhs: Int): Pembayaran
}

class NetworkPembayaranRepository(
    private val PembayaranApiService : PembayaranService
) : PembayaranRepository {
    override suspend fun getPembayaran(): PembayaranResponse {
        return try {
            PembayaranApiService.getPembayaran()
        } catch (e: Exception) {
            throw IOException("Failed to fetch Pembayaran data: ${e.message}")
        }
    }

    override suspend fun insertPembayaran(pembayaran: Pembayaran) {
        PembayaranApiService.insertPembayaran(pembayaran)
    }

    override suspend fun updatePembayaran(idPembayaran: Int, pembayaran: Pembayaran) {
        PembayaranApiService.updatePembayaran(idPembayaran, pembayaran)
    }

    override suspend fun deletePembayaran(idPembayaran: Int) {
        try {
            val response = PembayaranApiService.deletePembayaran(idPembayaran)
            if (!response.isSuccessful) {
                throw IOException("gagal menghapus data Pembayaran. HTTP kode: ${response.code()}")
            } else {
                response.message()
                println(response.message())
            }
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun getPembayaranById(idPembayaran: Int): Pembayaran {
        return PembayaranApiService.getPembayaranById(idPembayaran).data
    }

    override suspend fun getPembayaranByIdMhs(idMhs: Int): Pembayaran {
        return PembayaranApiService.getPembayaranByIdMhs(idMhs).data
    }
}
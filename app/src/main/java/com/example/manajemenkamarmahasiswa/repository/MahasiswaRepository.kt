package com.example.manajemenkamarmahasiswa.repository

import com.example.manajemenkamarmahasiswa.model.Mahasiswa
import com.example.manajemenkamarmahasiswa.model.MahasiswaResponse
import com.example.manajemenkamarmahasiswa.service.MahasiswaService
import okio.IOException

interface MahasiswaRepository{
    suspend fun getMahasiswa() : MahasiswaResponse
    suspend fun insertMahasiswa(mahasiswa: Mahasiswa)
    suspend fun editMahasiswa(idMahasiswa: Int, mahasiswa: Mahasiswa)
    suspend fun deleteMahasiswa(idMahasiswa: Int)
    suspend fun getMahasiswaById(idMahasiswa: Int): Mahasiswa
}

class NetworkMahasiswaRepository(
    private val mahasiswaApiService : MahasiswaService
) : MahasiswaRepository {
    override suspend fun getMahasiswa(): MahasiswaResponse {
        return try {
            mahasiswaApiService.getMahasiswa()
        } catch (e: Exception) {
            throw IOException("Failed to fetch Mahasiswa data: ${e.message}")
        }
    }

    override suspend fun insertMahasiswa(mahasiswa: Mahasiswa) {
        mahasiswaApiService.insertMahasiswa(mahasiswa)
    }

    override suspend fun editMahasiswa(idMahasiswa: Int, mahasiswa: Mahasiswa) {
        mahasiswaApiService.editMahasiswa(idMahasiswa, mahasiswa)
    }

    override suspend fun deleteMahasiswa(idMahasiswa: Int) {
        try {
            val response = mahasiswaApiService.deleteMahasiswa(idMahasiswa)
            if (!response.isSuccessful) {
                throw IOException("gagal menghapus data Mahasiswa. HTTP kode: ${response.code()}")
            } else {
                response.message()
                println(response.message())
            }
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun getMahasiswaById(idMahasiswa: Int): Mahasiswa {
        return mahasiswaApiService.getMahasiswaById(idMahasiswa).data
    }
}
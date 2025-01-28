package com.example.manajemenkamarmahasiswa.service

import com.example.manajemenkamarmahasiswa.model.Mahasiswa
import com.example.manajemenkamarmahasiswa.model.MahasiswaDetailResponse
import com.example.manajemenkamarmahasiswa.model.MahasiswaResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface MahasiswaService {
    @Headers(
        "Accept: application/json",
        "Content-Type: application/json",
    )

    @GET("mahasiswa")
    suspend fun getMahasiswa(): MahasiswaResponse

    @GET("mahasiswa/{idMahasiswa}")
    suspend fun getMahasiswaById(@Path("idMahasiswa")idMahasiswa:Int): MahasiswaDetailResponse

    @POST("mahasiswa/store")
    suspend fun insertMahasiswa(@Body mahasiswa: Mahasiswa)

    @PUT("mahasiswa/{idMahasiswa}")
    suspend fun editMahasiswa(@Path("idMahasiswa")idMahasiswa: Int, @Body mahasiswa: Mahasiswa)

    @DELETE("mahasiswa/{idMahasiswa}")
    suspend fun deleteMahasiswa(@Path("idMahasiswa")idMahasiswa: Int): Response<Void>
}
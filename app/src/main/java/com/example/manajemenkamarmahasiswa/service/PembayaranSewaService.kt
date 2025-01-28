package com.example.manajemenkamarmahasiswa.service

import com.example.manajemenkamarmahasiswa.model.Pembayaran
import com.example.manajemenkamarmahasiswa.model.PembayaranDetailResponse
import com.example.manajemenkamarmahasiswa.model.PembayaranResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface PembayaranService {
    @Headers(
        "Accept: application/json",
        "Content-Type: application/json",
    )

    @GET("pembayaran")
    suspend fun getPembayaran(): PembayaranResponse

    @GET("pembayaran/{idPembayaran}")
    suspend fun getPembayaranById(@Path("idPembayaran")idPembayaran:Int): PembayaranDetailResponse

    @GET("pembayaran/mahasiswa/{idMahasiswa}")
    suspend fun getPembayaranByIdMhs(@Path("idMahasiswa")idMahasiswa:Int) : PembayaranDetailResponse

    @POST("pembayaran/store")
    suspend fun insertPembayaran(@Body pembayaran: Pembayaran)

    @PUT("pembayaran/{idPembayaran}")
    suspend fun updatePembayaran(@Path("idPembayaran")idPembayaran: Int, @Body pembayaran: Pembayaran)

    @DELETE("pembayaran/{idPembayaran}")
    suspend fun deletePembayaran(@Path("idPembayaran")idPembayaran: Int): Response<Void>
}
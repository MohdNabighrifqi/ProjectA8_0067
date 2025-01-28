package com.example.manajemenkamarmahasiswa.service

import com.example.manajemenkamarmahasiswa.model.Bangunan
import com.example.manajemenkamarmahasiswa.model.BangunanDetailResponse
import com.example.manajemenkamarmahasiswa.model.BangunanResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface BangunanService {
    @Headers(
        "Accept: application/json",
        "Content-Type: application/json",
    )

    @GET("bangunan")
    suspend fun getBangunan() : BangunanResponse

    @GET("bangunan/{idBangunan}")
    suspend fun getBangunanById(@Path("idBangunan")idBgn : Int) : BangunanDetailResponse

    @POST("bangunan/store")
    suspend fun insertBangunan(@Body bangunan: Bangunan)

    @PUT("bangunan/{idBangunan}")
    suspend fun updateBangunan(@Path("idBangunan")idBangunan: Int, @Body bangunan: Bangunan)

    @DELETE("bangunan/{idBangunan}")
    suspend fun deleteBangunan(@Path("idBangunan")idBangunan: Int): Response<Void>
}
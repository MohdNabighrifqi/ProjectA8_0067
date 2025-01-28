package com.example.manajemenkamarmahasiswa.service

import com.example.manajemenkamarmahasiswa.model.Kamar
import com.example.manajemenkamarmahasiswa.model.KamarDetailResponse
import com.example.manajemenkamarmahasiswa.model.KamarResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface KamarService {
        @Headers(
                "Accept: application/json",
                "Content-Type: application/json",
        )

        @GET("kamar")
        suspend fun getKamar() : KamarResponse

        @GET("kamar/{idKamar}")
        suspend fun getKamarById(@Path("idKamar")idKamar : Int) : KamarDetailResponse

        @POST("kamar/store")
        suspend fun insertKamar(@Body kamar: Kamar)

        @PUT("kamar/{idKamar}")
        suspend fun updateKamar(@Path("idKamar")idKamar: Int, @Body kamar: Kamar)

        @DELETE("kamar/{idKamar}")
        suspend fun deleteKamar(@Path("idKamar")idKamar: Int): Response<Void>
}
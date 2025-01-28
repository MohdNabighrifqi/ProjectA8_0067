package com.example.manajemenkamarmahasiswa.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Kamar(
    @SerialName("id_kamar")
    val idKamar : Int,
    @SerialName("id_bangunan")
    val idBangunan : Int,
    @SerialName("nomor_kamar")
    val nomorKamar : String,
    val kapasitas : Int,
    @SerialName("status_kamar")
    val statusKamar : String
)

@Serializable
data class KamarResponse(
    val status: Boolean,
    val message: String,
    val data: List<Kamar>
)

@Serializable
data class KamarDetailResponse(
    val status: Boolean,
    val message: String,
    val data: Kamar
)

package com.example.manajemenkamarmahasiswa.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Bangunan(
    @SerialName("id_bangunan")
    val idBangunan : Int,
    @SerialName("nama_bangunan")
    val namaBangunan : String,
    @SerialName("jumlah_lantai")
    val jumlahLt : Int,
    val alamat : String
)

@Serializable
data class BangunanResponse(
    val status: Boolean,
    val message: String,
    val data: List<Bangunan>
)

@Serializable
data class BangunanDetailResponse(
    val status: Boolean,
    val message: String,
    val data: Bangunan
)

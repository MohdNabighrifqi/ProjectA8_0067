package com.example.manajemenkamarmahasiswa.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class Mahasiswa (
    @SerialName("id_mahasiswa")
    val idMahasiswa : Int,
    @SerialName("nomor_identitas")
    val nim : String,
    @SerialName("nama_mahasiswa")
    val namaMahasiswa : String,
    val email : String,
    @SerialName("nomor_telepon")
    val noTelp : String,

    @SerialName("id_kamar")
    val idKamar : Int
)

@Serializable
data class MahasiswaResponse(
    val status: Boolean,
    val message: String,
    val data: List<Mahasiswa>
)
@Serializable
data class MahasiswaDetailResponse(
    val status: Boolean,
    val message: String,
    val data: Mahasiswa
)

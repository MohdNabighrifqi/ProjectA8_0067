package com.example.manajemenkamarmahasiswa.di

import com.example.manajemenkamarmahasiswa.repository.BangunanRepository
import com.example.manajemenkamarmahasiswa.repository.KamarRepository
import com.example.manajemenkamarmahasiswa.repository.MahasiswaRepository
import com.example.manajemenkamarmahasiswa.repository.NetworkBangunanRepository
import com.example.manajemenkamarmahasiswa.repository.NetworkKamarRepository
import com.example.manajemenkamarmahasiswa.repository.NetworkMahasiswaRepository
import com.example.manajemenkamarmahasiswa.repository.NetworkPembayaranRepository
import com.example.manajemenkamarmahasiswa.repository.PembayaranRepository
import com.example.manajemenkamarmahasiswa.service.BangunanService
import com.example.manajemenkamarmahasiswa.service.KamarService
import com.example.manajemenkamarmahasiswa.service.MahasiswaService
import com.example.manajemenkamarmahasiswa.service.PembayaranService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

interface AppContainer {
    val mahasiswaRepository: MahasiswaRepository
    val bangunanRepository: BangunanRepository
    val kamarRepository: KamarRepository
    val pembayaranRepository: PembayaranRepository
}

class ManajemenKamarContainer : AppContainer {
    private val baseUrl = "http://10.0.2.2:3000/api/"
    private val json = Json { ignoreUnknownKeys = true }
    private val retrofit = Retrofit.Builder()
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(baseUrl)
        .build()


    private val mahasiswaService: MahasiswaService by lazy {
        retrofit.create(MahasiswaService::class.java)
    }

    private val bangunanService: BangunanService by lazy {
        retrofit.create(BangunanService::class.java)
    }

    private val kamarService: KamarService by lazy {
        retrofit.create(KamarService::class.java)
    }

    private val pembayaranService: PembayaranService by lazy {
        retrofit.create(PembayaranService::class.java)
    }

    override val mahasiswaRepository: MahasiswaRepository by lazy {
        NetworkMahasiswaRepository(mahasiswaService)
    }
    override val bangunanRepository: BangunanRepository by lazy {
        NetworkBangunanRepository(bangunanService)
    }
    override val kamarRepository: KamarRepository by lazy {
        NetworkKamarRepository(kamarService)
    }

    override val pembayaranRepository: PembayaranRepository by lazy {
        NetworkPembayaranRepository(pembayaranService)
    }
}
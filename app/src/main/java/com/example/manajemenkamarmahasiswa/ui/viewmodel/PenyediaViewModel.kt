package com.example.manajemenkamarmahasiswa.ui.viewmodel

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.manajemenkamarmahasiswa.ManajemenKamarApplication
import com.example.manajemenkamarmahasiswa.ui.viewmodel.Bangunan.BangunanDetailViewModel
import com.example.manajemenkamarmahasiswa.ui.viewmodel.Bangunan.BangunanEditViewModel
import com.example.manajemenkamarmahasiswa.ui.viewmodel.Bangunan.BangunanHomeViewModel
import com.example.manajemenkamarmahasiswa.ui.viewmodel.Bangunan.BangunanInsertViewModel
import com.example.manajemenkamarmahasiswa.ui.viewmodel.Kamar.KamarDetailViewModel
import com.example.manajemenkamarmahasiswa.ui.viewmodel.Kamar.KamarEditViewModel
import com.example.manajemenkamarmahasiswa.ui.viewmodel.Kamar.KamarHomeViewModel
import com.example.manajemenkamarmahasiswa.ui.viewmodel.Kamar.KamarInsertViewModel
import com.example.manajemenkamarmahasiswa.ui.viewmodel.Mahasiswa.MahasiswaDetailViewModel
import com.example.manajemenkamarmahasiswa.ui.viewmodel.Mahasiswa.MahasiswaEditViewModel
import com.example.manajemenkamarmahasiswa.ui.viewmodel.Mahasiswa.MahasiswaHomeViewModel
import com.example.manajemenkamarmahasiswa.ui.viewmodel.Mahasiswa.MahasiswaInsertViewModel
import com.example.manajemenkamarmahasiswa.ui.viewmodel.PembayaranSewa.PembayaranDetailViewModel
import com.example.manajemenkamarmahasiswa.ui.viewmodel.PembayaranSewa.PembayaranEditViewModel
import com.example.manajemenkamarmahasiswa.ui.viewmodel.PembayaranSewa.PembayaranHomeViewModel
import com.example.manajemenkamarmahasiswa.ui.viewmodel.PembayaranSewa.PembayaranInsertViewModel

object PenyediaViewModel {
    val Factory = viewModelFactory {
        // ------------------Bangunan------------------- //
        initializer {
            BangunanHomeViewModel(ManajemenKamarApplication().container.bangunanRepository)
        }
        initializer {
            BangunanInsertViewModel(ManajemenKamarApplication().container.bangunanRepository)
        }
        initializer {
            BangunanDetailViewModel(
                createSavedStateHandle(),
                ManajemenKamarApplication().container.bangunanRepository
            )
        }
        initializer {
            BangunanEditViewModel(
                createSavedStateHandle(),
                ManajemenKamarApplication().container.bangunanRepository
            )
        }
        // ------------------Kamar------------------- //
        initializer {
            KamarHomeViewModel(ManajemenKamarApplication().container.kamarRepository)
        }
        initializer {
            KamarInsertViewModel(ManajemenKamarApplication().container.kamarRepository)
        }
        initializer {
            KamarDetailViewModel(
                createSavedStateHandle(),
                ManajemenKamarApplication().container.kamarRepository
            )
        }
        initializer {
            KamarEditViewModel(
                createSavedStateHandle(),
                ManajemenKamarApplication().container.kamarRepository
            )
        }
        // ------------------Mahasiswa------------------- //
        initializer {
            MahasiswaHomeViewModel(ManajemenKamarApplication().container.mahasiswaRepository)
        }
        initializer {
            MahasiswaInsertViewModel(ManajemenKamarApplication().container.mahasiswaRepository)
        }
        initializer {
            MahasiswaDetailViewModel(
                createSavedStateHandle(),
                ManajemenKamarApplication().container.mahasiswaRepository
            )
        }
        initializer {
            MahasiswaEditViewModel(
                createSavedStateHandle(),
                ManajemenKamarApplication().container.mahasiswaRepository
            )
        }

        // ------------------Pembayaran Sewa------------------- //
        initializer {
            PembayaranHomeViewModel(ManajemenKamarApplication().container.pembayaranRepository)
        }
        initializer {
            PembayaranInsertViewModel(ManajemenKamarApplication().container.pembayaranRepository)
        }
        initializer {
            PembayaranDetailViewModel(
                createSavedStateHandle(),
                ManajemenKamarApplication().container.pembayaranRepository
            )
        }
        initializer {
            PembayaranEditViewModel(
                createSavedStateHandle(),
                ManajemenKamarApplication().container.pembayaranRepository
            )
        }
    }
}

fun CreationExtras.ManajemenKamarApplication() : ManajemenKamarApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as ManajemenKamarApplication)
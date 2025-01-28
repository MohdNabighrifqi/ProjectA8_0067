package com.example.manajemenkamarmahasiswa.navigation

interface Navigation {
    val route : String
    val titleRes: String
}

object DestinasiHomepage : Navigation {
    override val route = "homepage"
    override val titleRes = "Homepage"
}

object DestinasiBangunanHome : Navigation {
    override val route = "bangunanHome"
    override val titleRes: String = "Daftar Bangunan"
}

object DestinasiBangunanInsert : Navigation {
    override val route = "bangunanInsert"
    override val titleRes = "Tambah Data Bangunan"
}

object DestinasiBangunanDetail : Navigation {
    override val route = "bangunanDetail"
    override val titleRes = "Data Bangunan"
    const val idBangunan = "idBangunan"
    val routesWithArg = "$route/{$idBangunan}"
}

object DestinasiBangunanEdit : Navigation {
    override val route = "bangunanEdit"
    override val titleRes = "Edit Bangunan"
    const val idBangunan = "idBangunan"
    val routesWithArg = "$route/{$idBangunan}"
}

// ------------------Kamar------------------- //
object DestinasiKamarHome : Navigation {
    override val route = "kamarHome"
    override val titleRes = "Daftar Kamar"
}

object DestinasiKamarInsert : Navigation {
    override val route = "kamarInsert"
    override val titleRes = "Tambah Data Kamar"
}

object DestinasiKamarDetail : Navigation {
    override val route = "kamarDetail"
    override val titleRes = "Data Kamar"
    const val idKamar = "idKamar"
    val routesWithArg = "$route/{$idKamar}"
}

object DestinasiKamarEdit : Navigation {
    override val route = "kamarEdit"
    override val titleRes = "Edit Kamar"
    const val idKamar = "idKamar"
    val routesWithArg = "$route/{$idKamar}"
}

// ------------------Mahasiswa------------------- //
object DestinasiMahasiswaHome : Navigation {
    override val route = "mahasiswaHome"
    override val titleRes: String = "Daftar Mahasiswa"
}
object DestinasiMahasiswaInsert : Navigation {
    override val route = "mahasiswaInsert"
    override val titleRes = "Tambah Data Mahasiswa"
}

object DestinasiMahasiswaDetail : Navigation {
    override val route = "mahasiswaDetail"
    override val titleRes = "Data Mahasiswa"
    const val idMahasiswa = "idMahasiswa"
    val routesWithArg = "$route/{$idMahasiswa}"
}

object DestinasiMahasiswaEdit  : Navigation {
    override val route = "mahasiswaEdit"
    override val titleRes = "Edit Mahasiswa"
    const val idMahasiswa = "idMahasiswa"
    val routesWithArg = "$route/{$idMahasiswa}"
}

// ------------------Transaksi------------------- //
object DestinasiPembayaranHome : Navigation {
    override val route = "pembayaranHome"
    override val titleRes = "Daftar Pembayaran"
}

object DestinasiPembayaranInsert : Navigation {
    const val idMahasiswa = "idMahasiswa"
    override val route = "pembayaranInsert/{$idMahasiswa}"
    override val titleRes = "Tambah Data Pembayaran"

    fun createRoute(idMahasiswa: String) = "pembayranInsert/$idMahasiswa"
}

object DestinasiPembayaranDetail : Navigation {
    override val titleRes = "Detail Pembayaran"
    override val route = "pembayaranDetail"

    const val idPembayaran = "idPembayaran"
    const val idMahasiswa = "idMahasiswa"

    val routeWithIdPembayaran = "$route/{$idPembayaran}"
    val routeWithIdMahasiswa = "$route/mhs/{$idMahasiswa}"

    fun detailRouteWithIdPembayaran(idPembayaran: String) = "$route/$idPembayaran"
    fun detailRouteWithIdMahasiswa(idMahasiswa: String) = "$route/mahasiswa/$idMahasiswa"
}



object DestinasiPembayaranEdit : Navigation {
    override val route = "pembayaranEdit"
    override val titleRes = "Edit Pembayaran"
    const val idPembayaran = "idPembayaran"
    val routesWithArg = "$route/{$idPembayaran}"
}


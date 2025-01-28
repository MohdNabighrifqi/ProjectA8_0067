package com.example.manajemenkamarmahasiswa.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.manajemenkamarmahasiswa.ui.view.Bangunan.BangunanDetailScreen
import com.example.manajemenkamarmahasiswa.ui.view.Bangunan.BangunanEditScreen
import com.example.manajemenkamarmahasiswa.ui.view.Bangunan.BangunanHomeScreen
import com.example.manajemenkamarmahasiswa.ui.view.Bangunan.BangunanInsertView
import com.example.manajemenkamarmahasiswa.ui.view.Homepage
import com.example.manajemenkamarmahasiswa.ui.view.Kamar.KamarDetailScreen
import com.example.manajemenkamarmahasiswa.ui.view.Kamar.KamarEditScreen
import com.example.manajemenkamarmahasiswa.ui.view.Kamar.KamarHomeScreen
import com.example.manajemenkamarmahasiswa.ui.view.Kamar.KamarInsertView
import com.example.manajemenkamarmahasiswa.ui.view.Mahasiswa.MahasiswaDetailScreen
import com.example.manajemenkamarmahasiswa.ui.view.Mahasiswa.MahasiswaEditScreen
import com.example.manajemenkamarmahasiswa.ui.view.Mahasiswa.MahasiswaHomeScreen
import com.example.manajemenkamarmahasiswa.ui.view.Mahasiswa.MahasiswaInsertView
import com.example.manajemenkamarmahasiswa.ui.view.PembayaranSewa.PembayaranDetailScreen
import com.example.manajemenkamarmahasiswa.ui.view.PembayaranSewa.PembayaranHomeScreen
import com.example.manajemenkamarmahasiswa.ui.view.PembayaranSewa.PembayaranInsertView
import com.example.manajemenkamarmahasiswa.ui.viewmodel.Mahasiswa.MahasiswaInsertViewModel

@Composable
fun MainControllerPage(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = DestinasiHomepage.route
    ) {
        // Homepage
        composable(DestinasiHomepage.route) {
            Homepage(
                onItemClick = { item ->
                    when (item) {
                        "Bangunan" -> navController.navigate(DestinasiBangunanHome.route)
                        "Kamar" -> navController.navigate(DestinasiKamarHome.route)
                        "Mahasiswa" -> navController.navigate(DestinasiMahasiswaHome.route)
                        "Pembayaran Sewa" -> navController.navigate(DestinasiPembayaranHome.route)
                        else -> {}
                    }
                }
            )
        }

        // Navigasi untuk Bangunan
        composable(DestinasiBangunanHome.route) {
            BangunanHomeScreen(
                navigateToItemEntry = { navController.navigate(DestinasiBangunanInsert.route) },
                onDetailClick = { id -> navController.navigate("${DestinasiBangunanDetail.route}/$id") },
                onEditClick = { id -> navController.navigate("${DestinasiBangunanEdit.route}/$id") },
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(DestinasiBangunanInsert.route) {
            BangunanInsertView(
                onClickBack = { navController.popBackStack() },
                onNavigate = { navController.popBackStack() }
            )
        }
        composable(DestinasiBangunanDetail.routesWithArg) {
            val id = it.arguments?.getString(DestinasiBangunanDetail.idBangunan)
            if (id != null) {
                BangunanDetailScreen(
                    onClickBack = { navController.popBackStack() },
                    onUpdateClick = { navController.navigate("${DestinasiBangunanEdit.route}/$id") }
                )
            }
        }
        composable(DestinasiBangunanEdit.routesWithArg) {
            val id = it.arguments?.getString(DestinasiBangunanEdit.idBangunan)
            if (id != null) {
                BangunanEditScreen(onClickBack = { navController.popBackStack() },
                    onNavigate = { navController.popBackStack() }
                )
            }
        }

        // Navigasi untuk Kamar
        composable(DestinasiKamarHome.route) {
            KamarHomeScreen(
                navigateToItemEntry = { navController.navigate(DestinasiKamarInsert.route) },
                onDetailClick = { id -> navController.navigate("${DestinasiKamarDetail.route}/$id") },
                onEditClick = { id -> navController.navigate("${DestinasiKamarEdit.route}/$id") },
                onBackClick = { navController.popBackStack() },
                onDeleteClick = { id -> /* Implement Delete Logic */ }
            )
        }
        composable(DestinasiKamarInsert.route) {
            KamarInsertView(
                onBackClick = { navController.popBackStack() },
                onNavigate = { navController.popBackStack() }
            )
        }
        composable(DestinasiKamarDetail.routesWithArg) {
            val id = it.arguments?.getString(DestinasiKamarDetail.idKamar)
            if (id != null) {
                KamarDetailScreen(
                    onClickBack = { navController.popBackStack() },
                    onUpdateClick = { navController.navigate("${DestinasiKamarEdit.route}/$id") }
                )
            }
        }
        composable(DestinasiKamarEdit.routesWithArg) {
            val id = it.arguments?.getString(DestinasiKamarEdit.idKamar)
            if (id != null) {
                KamarEditScreen(
                    onClickBack = { navController.popBackStack() },
                    onNavigate = { navController.popBackStack() }
                )
            }
        }

        // Navigasi untuk Mahasiswa
        composable(DestinasiMahasiswaHome.route) {
            MahasiswaHomeScreen(
                navigateToItemEntry = { navController.navigate(DestinasiMahasiswaInsert.route) },
                onDetailClick = { id ->
                    navController.navigate("${DestinasiMahasiswaDetail.route}/$id")
                },
                onEditClick = { id ->
                    navController.navigate("${DestinasiMahasiswaEdit.route}/$id")
                },
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(DestinasiMahasiswaInsert.route) {
            val viewModel: MahasiswaInsertViewModel = viewModel()
            MahasiswaInsertView(
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() },
                onNavigate = { navController.popBackStack() },
            )
        }

        composable(DestinasiMahasiswaDetail.routesWithArg) {
            val id = it.arguments?.getString(DestinasiMahasiswaDetail.idMahasiswa)
            if (id != null) {
                MahasiswaDetailScreen(
                    onClickBack = { navController.popBackStack() },
                    onTambahPembayaranClick = { navController.navigate(DestinasiPembayaranInsert.createRoute(id)) }
                )
            }
        }
        composable(DestinasiMahasiswaEdit.routesWithArg) {
            val id = it.arguments?.getString(DestinasiMahasiswaEdit.idMahasiswa)
            if (id != null) {
                MahasiswaEditScreen(
                    onClickBack = { navController.popBackStack() },
                    onNavigate = { navController.popBackStack() }
                )
            }
        }

        // Navigasi untuk Pembayaran
        composable(DestinasiPembayaranHome.route) {
            PembayaranHomeScreen(
                onDetailClick = { id -> navController.navigate(DestinasiPembayaranDetail.detailRouteWithIdPembayaran(id)) },
                onEditClick = { id -> navController.navigate(DestinasiPembayaranEdit.routesWithArg.replace("{idPembayaran}", id)) },
                onBackClick = { navController.popBackStack() },
                navigateToItemEntry ={ navController.navigate(DestinasiPembayaranInsert.route)}
            )
        }
        composable(DestinasiPembayaranInsert.route) {
            val idMahasiswa = it.arguments?.getString(DestinasiPembayaranInsert.idMahasiswa)
            if (idMahasiswa != null) {
                PembayaranInsertView(
                    idMahasiswa = idMahasiswa,
                    onBackClick = { navController.popBackStack() },
                    onNavigate = { navController.popBackStack() }
                )
            }
        }
        composable(DestinasiPembayaranDetail.routeWithIdPembayaran) {
            val idPembayaran = it.arguments?.getString(DestinasiPembayaranDetail.idPembayaran)
            if (idPembayaran != null) {
                PembayaranDetailScreen(
                    idPembayaran = idPembayaran,
                    onClickBack = { navController.popBackStack() }
                )
            }
        }
    }
}

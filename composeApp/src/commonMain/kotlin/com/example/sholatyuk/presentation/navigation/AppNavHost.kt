package com.example.sholatyuk.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute // <-- Import ditambahkan
import com.example.sholatyuk.presentation.screens.home.HomeScreen
import com.example.sholatyuk.presentation.screens.prayer.PrayerScreen
import com.example.sholatyuk.presentation.screens.doa.DoaScreen
import com.example.sholatyuk.presentation.screens.doa.DoaDetailScreen // <-- Import ditambahkan
import com.example.sholatyuk.presentation.screens.islamai.IslamAIScreen
import com.example.sholatyuk.presentation.screens.profile.ProfileScreen
import com.example.sholatyuk.presentation.screens.kajian.KajianScreen
import com.example.sholatyuk.presentation.screens.prayer.QiblaScreen
import com.example.sholatyuk.presentation.screens.splash.SplashScreen

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Route.Splash,
        modifier = modifier
    ) {
        composable<Route.Splash> {
            SplashScreen(
                onNavigateToHome = {
                    navController.navigate(Route.Home) {
                        popUpTo(Route.Splash) { inclusive = true }
                    }
                }
            )
        }

        composable<Route.Home> {
            HomeScreen(
                onNavigateToShalat = {
                    navController.navigate(Route.Shalat) { popUpTo(Route.Home) { saveState = true }; launchSingleTop = true; restoreState = true }
                },
                onNavigateToDoa = {
                    navController.navigate(Route.Doa) { popUpTo(Route.Home) { saveState = true }; launchSingleTop = true; restoreState = true }
                },
                onNavigateToIslamAI = {
                    navController.navigate(Route.IslamAI) { popUpTo(Route.Home) { saveState = true }; launchSingleTop = true; restoreState = true }
                },
                onNavigateToProfile = {
                    navController.navigate(Route.Profile) { launchSingleTop = true }
                },
                onNavigateToKajianNotes = {
                    navController.navigate(Route.KajianNotes)
                },
                onNavigateToQibla = {
                    navController.navigate(Route.Qibla)
                }
            )
        }

        composable<Route.Shalat> {
            PrayerScreen(
                onNavigateToHome = {
                    navController.navigate(Route.Home) { popUpTo(Route.Home) { saveState = true }; launchSingleTop = true; restoreState = true }
                },
                onNavigateToDoa = {
                    navController.navigate(Route.Doa) { popUpTo(Route.Home) { saveState = true }; launchSingleTop = true; restoreState = true }
                },
                onNavigateToIslamAI = {
                    navController.navigate(Route.IslamAI) { popUpTo(Route.Home) { saveState = true }; launchSingleTop = true; restoreState = true }
                }
            )
        }

        // --- BLOK DOA YANG DIPERBARUI ---
        composable<Route.Doa> {
            DoaScreen(
                onNavigateToHome = {
                    navController.navigate(Route.Home) { popUpTo(Route.Home) { saveState = true }; launchSingleTop = true; restoreState = true }
                },
                onNavigateToShalat = {
                    navController.navigate(Route.Shalat) { popUpTo(Route.Home) { saveState = true }; launchSingleTop = true; restoreState = true }
                },
                onNavigateToIslamAI = {
                    navController.navigate(Route.IslamAI) { popUpTo(Route.Home) { saveState = true }; launchSingleTop = true; restoreState = true }
                },
                onNavigateToDetail = { doaId ->
                    // Jika doa diklik, pergi ke halaman detail
                    navController.navigate(Route.DoaDetail(doaId))
                }
            )
        }

        // --- RUTE BARU UNTUK DOA DETAIL ---
        composable<Route.DoaDetail> { backStackEntry ->
            // Mengambil ID doa yang dikirim dari rute navigasi
            val detail: Route.DoaDetail = backStackEntry.toRoute()

            DoaDetailScreen(
                doaId = detail.id,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable<Route.IslamAI> {
            IslamAIScreen(
                onNavigateToHome = {
                    navController.navigate(Route.Home) { popUpTo(Route.Home) { saveState = true }; launchSingleTop = true; restoreState = true }
                },
                onNavigateToShalat = {
                    navController.navigate(Route.Shalat) { popUpTo(Route.Home) { saveState = true }; launchSingleTop = true; restoreState = true }
                },
                onNavigateToDoa = {
                    navController.navigate(Route.Doa) { popUpTo(Route.Home) { saveState = true }; launchSingleTop = true; restoreState = true }
                }
            )
        }

        composable<Route.Profile> {
            ProfileScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable<Route.KajianNotes> {
            KajianScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable<Route.Qibla> {
            QiblaScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
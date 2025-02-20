package com.di

import com.ui.feature.confirmation.ConfirmationViewModel
import com.ui.feature.details.DetailsScreenViewModel
import com.ui.feature.account.login.LoginViewModel
import com.ui.feature.account.register.RegisterViewModel
import com.ui.feature.home.HomeViewModel
import com.ui.feature.packages.PackageListViewModel
import com.ui.feature.profile.ProfileScreenViewModel
import com.ui.feature.splash.SplashViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
val viewModelModule = module {
	viewModel {
		HomeViewModel(get(), get())
	}
	viewModel{
		SplashViewModel()
	}
	viewModel {
		LoginViewModel(
			loginUseCase = get(),
			tourismSession = get(),
			authenticateWithBackendUseCase = get(),
			googleSignInClient = get() // Add this parameter
		)
	}
	viewModel {
		RegisterViewModel(get(),get(),get(),get())
	}
	viewModel {
        ProfileScreenViewModel()
    }
	viewModel {
		PackageListViewModel()
	}
	viewModel {
		DetailsScreenViewModel()
	}
	viewModel {
		ConfirmationViewModel()
	}
}
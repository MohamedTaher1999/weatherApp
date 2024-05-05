package openweather.data.di

import openweather.data.local.CityPreferencesDataStoreImpl
import openweather.data.local.UnitsPreferencesDataStoreImpl
import openweather.data.mapper.CityInfoMapper
import openweather.data.mapper.CurrentWeatherMapper
import openweather.data.mapper.ErrorMapper
import openweather.data.mapper.FiveDayWeatherMapper
import openweather.data.mapper.OneCallMapper
import openweather.data.remote.api.OpenWeatherApi
import openweather.data.repository.OpenWeatherRepositoryImpl
import openweather.data.viewmodels.MainViewModel
import openweather.domain.datastore.CityPreferencesDataStore
import openweather.domain.datastore.UnitsPreferencesDataStore
import openweather.domain.repository.OpenWeatherRepository
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

val mappersModule = module {
    single { ErrorMapper() }
    single { OneCallMapper() }
    single {CityInfoMapper()}
    single { FiveDayWeatherMapper() }
    single { CurrentWeatherMapper() }
}

val apiModule = module {
    single { OpenWeatherApi() }
}
val cityPreferencesModule = module {
    single<CityPreferencesDataStore> { CityPreferencesDataStoreImpl(androidContext()) }
}
val preferencesModule = module {
    single<UnitsPreferencesDataStore> { UnitsPreferencesDataStoreImpl(androidContext()) }
}

val repositoryModule = module {
    single<OpenWeatherRepository> { OpenWeatherRepositoryImpl(get(), get(),get(), get(),get()) }
}

val viewModelModule = module {
    single { MainViewModel(get()) }
}

fun initKoin(appDeclaration: KoinAppDeclaration) = startKoin {
    appDeclaration()
    modules(
        mappersModule,
        apiModule,
        preferencesModule,
        cityPreferencesModule,
        repositoryModule,
        viewModelModule
    )
}
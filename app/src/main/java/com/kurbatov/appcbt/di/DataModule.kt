package com.kurbatov.appcbt.di

import android.app.Application
import com.kurbatov.appcbt.data.AppDatabase
import com.kurbatov.appcbt.data.ShopListDao
import com.kurbatov.appcbt.data.ShopListRepositoryImpl
import com.kurbatov.appcbt.domain.ShopListRepository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface DataModule {

    @ApplicationScope
    @Binds
    fun bindShopListRepository(impl: ShopListRepositoryImpl): ShopListRepository

    companion object {

        @ApplicationScope
        @Provides
        fun provideShopListDao(
            application: Application
        ): ShopListDao{
            return AppDatabase.getInstance(application).shopListDao()
        }
    }

}
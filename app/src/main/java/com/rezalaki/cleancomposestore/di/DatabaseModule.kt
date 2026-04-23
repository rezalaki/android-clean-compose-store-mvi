package com.rezalaki.cleancomposestore.di

import android.content.Context
import androidx.room.Room
import com.rezalaki.cleancomposestore.data.local.AppDataBase
import com.rezalaki.cleancomposestore.data.local.MyCommentsDao
import com.rezalaki.cleancomposestore.utils.Constants.DB_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, AppDataBase::class.java, DB_NAME)
            .fallbackToDestructiveMigration()
            .build()

    @Singleton
    @Provides
    fun provideMyCommentsDao(db: AppDataBase) : MyCommentsDao = db.myCommentsDao()

}
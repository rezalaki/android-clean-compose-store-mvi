package com.rezalaki.cleancomposestore.di


import com.rezalaki.cleancomposestore.data.repository.BrandsRepositoryImpl
import com.rezalaki.cleancomposestore.data.repository.CommentsRepositoryImpl
import com.rezalaki.cleancomposestore.data.repository.ProductsRepositoryImpl
import com.rezalaki.cleancomposestore.domain.repository.BrandsRepository
import com.rezalaki.cleancomposestore.domain.repository.CommentsRepository
import com.rezalaki.cleancomposestore.domain.repository.ProductsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindProductsRepository(productsRepositoryImpl: ProductsRepositoryImpl): ProductsRepository

    @Binds
    @Singleton
    abstract fun bindBrandsRepository(brandsRepositoryImpl: BrandsRepositoryImpl): BrandsRepository

    @Binds
    @Singleton
    abstract fun bindCommentsRepository(
        commentsRepositoryImpl: CommentsRepositoryImpl
    ): CommentsRepository

}
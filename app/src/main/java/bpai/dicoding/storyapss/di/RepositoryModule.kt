package bpai.dicoding.storyapss.di

import bpai.dicoding.storyapss.data.repository_impl.LoginRepositoryImpl
import bpai.dicoding.storyapss.data.repository_impl.RegisterRepositoryImpl
import bpai.dicoding.storyapss.data.repository_impl.StoriesRepositoryImpl
import bpai.dicoding.storyapss.domain.repository.ILoginRepository
import bpai.dicoding.storyapss.domain.repository.IRegisterRepository
import bpai.dicoding.storyapss.domain.repository.IStoriesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun provideLoginRepository(loginRepoImpl: LoginRepositoryImpl):ILoginRepository

    @Binds
    abstract fun provideRegisterRepository(registerRepositoryImpl: RegisterRepositoryImpl):IRegisterRepository

    @Binds
    abstract fun provideStoriesRepository(storiesRepositoryImpl: StoriesRepositoryImpl): IStoriesRepository
}
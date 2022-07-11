package bpai.dicoding.storyapss.di

import bpai.dicoding.storyapss.domain.use_case.create_story.CreateStoryUseCaseImpl
import bpai.dicoding.storyapss.domain.use_case.create_story.ICreateStoryUseCase
import bpai.dicoding.storyapss.domain.use_case.home.HomeUseCaseImpl
import bpai.dicoding.storyapss.domain.use_case.home.IHomeUseCase
import bpai.dicoding.storyapss.domain.use_case.login.ILoginUseCase
import bpai.dicoding.storyapss.domain.use_case.login.LoginUseCaseImpl
import bpai.dicoding.storyapss.domain.use_case.register.IRegisterUseCase
import bpai.dicoding.storyapss.domain.use_case.register.RegisterUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class UseCaseModule {
    @Binds
    abstract fun provideLoginUseCase(loginUseCaseImpl: LoginUseCaseImpl):ILoginUseCase

    @Binds
    abstract fun provideRegisterUseCase(registerUseCaseImpl: RegisterUseCaseImpl):IRegisterUseCase

    @Binds
    abstract fun provideHomeUseCase(homeUseCaseImpl: HomeUseCaseImpl):IHomeUseCase

    @Binds
    abstract fun provideCreateStoryUseCase(createUseCase: CreateStoryUseCaseImpl): ICreateStoryUseCase
}
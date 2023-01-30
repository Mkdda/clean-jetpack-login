package com.maylbus.collectives.cleanarchitecturelogin.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.maylbus.collectives.cleanarchitecturelogin.repository.auth.AuthenticationProvider
import com.maylbus.collectives.cleanarchitecturelogin.repository.auth.AuthenticationRepository
import com.maylbus.collectives.cleanarchitecturelogin.repository.firestore.FirestoreProvider
import com.maylbus.collectives.cleanarchitecturelogin.repository.firestore.FirestoreRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object LoginModule {

    // region authentication

    @Provides
    fun provideFirebaseAuthentication(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    fun provideAuthenticationRepository(firebaseAuth: FirebaseAuth): AuthenticationProvider =
        AuthenticationRepository(firebaseAuth)

    // endregion

    // region firestore

    @Provides
    fun provideFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    fun provideFirestoreRepository(firestore:FirebaseFirestore): FirestoreProvider =
        FirestoreRepository(firestore)

    // endregion
}
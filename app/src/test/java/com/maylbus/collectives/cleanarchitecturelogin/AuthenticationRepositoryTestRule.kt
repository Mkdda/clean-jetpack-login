package com.maylbus.collectives.cleanarchitecturelogin

import android.app.Activity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.maylbus.collectives.cleanarchitecturelogin.repository.auth.AuthenticationRepository
import io.mockk.mockk
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import java.util.UUID
import java.util.concurrent.Executor
import kotlin.Exception

class AuthenticationRepositoryTestRule: TestWatcher() {

    lateinit var successTask: Task<AuthResult>
    lateinit var failureTask: Task<AuthResult>

    val mockAuthentication: FirebaseAuth by lazy {

        mockk(relaxed = true)
    }

    val authenticationRepository: AuthenticationRepository by lazy {

        AuthenticationRepository(this.mockAuthentication)
    }

    fun mockkTaskAsSuccess() {

        this.successTask = object : Task<AuthResult>() {

            override fun addOnCompleteListener(onCompleteListener: OnCompleteListener<AuthResult>): Task<AuthResult> {

                onCompleteListener.onComplete(successTask)

                return successTask
            }

            override fun addOnFailureListener(onFailureListener: OnFailureListener): Task<AuthResult> {

                onFailureListener.onFailure(Exception())

                return failureTask
            }

            override fun addOnFailureListener(p0: Activity, p1: OnFailureListener): Task<AuthResult> {

                p1.onFailure(Exception())

                return failureTask
            }

            override fun addOnFailureListener(p0: Executor, p1: OnFailureListener): Task<AuthResult> {

                p1.onFailure(Exception())

                return failureTask
            }

            override fun getException(): Exception? = null

            override fun getResult(): AuthResult {
                TODO("Not yet implemented")
            }

            override fun <X : Throwable?> getResult(p0: Class<X>): AuthResult {
                TODO("Not yet implemented")
            }

            override fun isCanceled(): Boolean {
                TODO("Not yet implemented")
            }

            override fun isComplete(): Boolean = true

            override fun isSuccessful(): Boolean = true

            override fun addOnSuccessListener(
                p0: Executor,
                p1: OnSuccessListener<in AuthResult>
            ): Task<AuthResult> {
                TODO("Not yet implemented")
            }

            override fun addOnSuccessListener(
                p0: Activity,
                p1: OnSuccessListener<in AuthResult>
            ): Task<AuthResult> {
                TODO("Not yet implemented")
            }

            override fun addOnSuccessListener(p0: OnSuccessListener<in AuthResult>): Task<AuthResult> {
                TODO("Not yet implemented")
            }
        }
    }

    fun mockkTaskAsFailure() {

        this.failureTask = object : Task<AuthResult>() {

            override fun addOnFailureListener(p0: OnFailureListener): Task<AuthResult> {
                TODO("Not yet implemented")
            }

            override fun addOnFailureListener(
                p0: Activity,
                p1: OnFailureListener
            ): Task<AuthResult> {
                TODO("Not yet implemented")
            }

            override fun addOnFailureListener(
                p0: Executor,
                p1: OnFailureListener
            ): Task<AuthResult> {
                TODO("Not yet implemented")
            }

            override fun getException(): Exception {

                val code: String = UUID.randomUUID().toString()
                val message = "error with credentials"

                return FirebaseAuthException(code,message)
            }

            override fun getResult(): AuthResult {
                TODO("Not yet implemented")
            }

            override fun <X : Throwable?> getResult(p0: Class<X>): AuthResult {
                TODO("Not yet implemented")
            }

            override fun isCanceled(): Boolean {
                TODO("Not yet implemented")
            }

            override fun addOnSuccessListener(
                p0: Executor,
                p1: OnSuccessListener<in AuthResult>
            ): Task<AuthResult> {
                TODO("Not yet implemented")
            }

            override fun addOnSuccessListener(
                p0: Activity,
                p1: OnSuccessListener<in AuthResult>
            ): Task<AuthResult> {
                TODO("Not yet implemented")
            }

            override fun addOnSuccessListener(p0: OnSuccessListener<in AuthResult>): Task<AuthResult> {
                TODO("Not yet implemented")
            }

            override fun isComplete(): Boolean = true

            override fun isSuccessful(): Boolean = false

            override fun addOnCompleteListener(onCompleteListener: OnCompleteListener<AuthResult>): Task<AuthResult> {

                onCompleteListener.onComplete(failureTask)

                return failureTask
            }
        }
    }
}
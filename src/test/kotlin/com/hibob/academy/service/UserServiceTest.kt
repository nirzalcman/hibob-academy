package com.hibob.academy.service

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class UserServiceTest {
    private val userDao = mock<UserDao>()
    private val notificationService = mock<NotificationService>()
    private val emailVerificationService = mock<EmailVerificationService>()
    private val userService = UserService(userDao, notificationService, emailVerificationService)


    @Test
    fun `registerUser- already exists user`() {
        val user = User(
            id = 1L,
            name = "Nir",
            email = "Nir@example.com",
            password = "password",
            isEmailVerified = false
        )
        whenever(userDao.findById(1)).thenReturn(user)
        assertThrows(IllegalArgumentException::class.java) { userService.registerUser(user) }

    }


    @Test
    fun `registerUser - user registration fails`() {
        val user = User(
            id = 1L,
            name = "Nir",
            email = "Nir@example.com",
            password = "password",
            isEmailVerified = false
        )
        whenever(userDao.findById(2)).thenReturn(null)
        whenever(userDao.save(user.copy(isEmailVerified = false))).thenReturn(false)
        assertThrows(IllegalStateException::class.java) { userService.registerUser(user) }


    }

    @Test
    fun `registerUser - Failed to send verification email`() {
        val user = User(
            id = 1L,
            name = "Nir",
            email = "Nir@example.com",
            password = "password",
            isEmailVerified = false
        )
        whenever(userDao.findById(2)).thenReturn(user).thenReturn(null)
        whenever(userDao.save(user.copy(isEmailVerified = false))).thenReturn(true)
        whenever(emailVerificationService.sendVerificationEmail(user.email)).thenReturn(false)
        assertThrows(IllegalStateException::class.java) { userService.registerUser(user) }
    }

    @Test
    fun `registerUser - Success`() {
        val user = User(
            id = 1L,
            name = "Nir",
            email = "Nir@example.com",
            password = "password",
            isEmailVerified = false
        )
        whenever(userDao.findById(2)).thenReturn(user).thenReturn(null)
        whenever(userDao.save(user.copy(isEmailVerified = false))).thenReturn(true)
        whenever(emailVerificationService.sendVerificationEmail(user.email)).thenReturn(true)
        assertTrue(userService.registerUser(user))
    }


    @Test
    fun `verifyUserEmail should throw exception when user not found`() {
        val userId = 1L
        val token = "validToken"

        whenever(userDao.findById(userId)).thenReturn(null)

        assertThrows(IllegalArgumentException::class.java) {
            userService.verifyUserEmail(userId, token)
        }

    }


    @Test
    fun `verifyUserEmail should throw exception when email verification fails`() {
        val user = User(
            id = 1L,
            name = "Nir",
            email = "Nir@example.com",
            password = "password",
            isEmailVerified = false
        )
        val token = "invalidToken"

        whenever(userDao.findById(user.id)).thenReturn(user)
        whenever(emailVerificationService.verifyEmail(user.email, token)).thenReturn(false)

        assertThrows(IllegalArgumentException::class.java) {
            userService.verifyUserEmail(user.id, token)
        }

    }

    @Test
    fun `verifyUserEmail should update user and send notification when verification succeeds`() {
        val user = User(
            id = 1L,
            name = "Nir",
            email = "Nir@example.com",
            password = "password",
            isEmailVerified = false
        )
        val token = "validToken"

        whenever(userDao.findById(user.id)).thenReturn(user)
        whenever(emailVerificationService.verifyEmail(user.email, token)).thenReturn(true)
        whenever(userDao.update(user.copy(isEmailVerified = true))).thenReturn(true)

        assertTrue(userService.verifyUserEmail(user.id, token))

        verify(notificationService).sendEmail(user.email, "Welcome ${user.name}!")

    }

    @Test
    fun `verifyUserEmail should return false when user update fails`() {
        val user = User(
            id = 1L,
            name = "Nir",
            email = "Nir@example.com",
            password = "password",
            isEmailVerified = false
        )
        val token = "validToken"

        whenever(userDao.findById(user.id)).thenReturn(user)
        whenever(emailVerificationService.verifyEmail(user.email, token)).thenReturn(true)
        whenever(userDao.update(user.copy(isEmailVerified = true))).thenReturn(false)

        assertFalse(userService.verifyUserEmail(user.id, token))


    }


}
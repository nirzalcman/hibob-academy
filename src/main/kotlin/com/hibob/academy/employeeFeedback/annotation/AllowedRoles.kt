package com.hibob.academy.employeeFeedback.annotations

import com.hibob.academy.employeeFeedback.Service.SessionJwtService
import com.hibob.academy.employeeFeedback.dao.Role
import jakarta.servlet.http.HttpServletRequest
import jakarta.ws.rs.ForbiddenException
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.springframework.stereotype.Component

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class AllowedRoles(vararg val roles: Role)


@Aspect
@Component
class RoleCheckAspect(
    private val sessionJwtService: SessionJwtService,
    private val request: HttpServletRequest
) {

    @Before("@annotation(allowedRoles)")
    fun validateRole(allowedRoles: AllowedRoles) {
        val employeeId = request.getAttribute("employeeId") as Long
        val companyId = request.getAttribute("companyId") as Long
        val userRole = sessionJwtService.getRoleByLoggedInDetails(employeeId, companyId)

        if (userRole !in allowedRoles.roles) {
            throw ForbiddenException("Access Denied: User role '$userRole' is not permitted to access this resource")

        }
    }
}
package com.hibob.academy.resource


import com.hibob.academy.dao.OwnerCreationRequest
import com.hibob.academy.service.OwnerService
import jakarta.inject.Inject
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.springframework.stereotype.Component


@Component
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/api/owners")
class OwnerResource(private val ownerService: OwnerService) {

    @POST
    fun createOwner(ownerRequest: OwnerCreationRequest): Response {
        val ownerId = ownerService.createOwner(ownerRequest)
        return Response.status(Response.Status.CREATED).entity("Owner created successfully with ID: $ownerId").build()
    }


    @GET
    @Path("/{companyId}")
    fun getOwners(@PathParam("companyId") companyId: Long): Response {
        val owners = ownerService.getOwners(companyId)
        return Response.ok(owners).build()

    }

    @GET
    @Path("{companyId}/{id}")
    fun getOwnerById(@PathParam("companyId") companyId: Long, @PathParam("id") id: Long): Response {
        return Response.ok(ownerService.getOwnerById(id, companyId)).build()
    }

    @GET
    @Path("/{companyId}/petId/{petId}")
    fun getOwnerByPetID(@PathParam("companyId") companyId: Long, @PathParam("petId") petId: Long): Response {
        return Response.ok(ownerService.getOwnerByPetId(companyId, petId)).build()
    }

}
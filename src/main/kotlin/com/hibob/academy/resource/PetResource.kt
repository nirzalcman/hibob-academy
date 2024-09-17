package com.hibob.academy.resource

import com.hibob.academy.dao.PetCreationRequest
import com.hibob.academy.service.PetService
import jakarta.inject.Inject
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.springframework.stereotype.Component

@Component
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/api/pets")
class PetResource(private val petService: PetService) {

    @GET
    @Path("/{companyId}/type/{type}")
    fun getPetsByType(@PathParam("companyId") companyId: Long, @PathParam("type") type: String): Response {
        return Response.ok(petService.getPetsByType(type, companyId)).build()
    }

    @GET
    @Path("/{companyId}/{id}")
    fun getPetById(@PathParam("companyId") companyId: Long, @PathParam("id") id: Long): Response {
        return Response.ok(petService.getPetById(id, companyId)).build()
    }

    @POST
    fun createPet(petCreationRequest: PetCreationRequest): Response {
        return Response.ok(petService.createPet(petCreationRequest)).build()
    }

    @PUT
    @Path("/{companyId}/{id}/{ownerId}")
    fun updatePetOwner(
        @PathParam("companyId") companyId: Long,
        @PathParam("id") id: Long,
        @PathParam("ownerId") ownerId: Long
    ): Response {
        return Response.ok(petService.updatePetOwner(id, ownerId, companyId)).build()
    }

}
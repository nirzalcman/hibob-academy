package com.hibob.academy.resource

import com.hibob.academy.dao.PetCreationRequest
import com.hibob.academy.service.PetService
import jakarta.inject.Inject
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.RequestBody

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

    @GET
    @Path("/{companyId}")
    fun getPetByCompanyId(@PathParam("companyId") companyId:Long): Response {
        return Response.ok(petService.getPetsByCompanyId(companyId)).build()
    }


    @POST
    fun createPet(@RequestBody petCreationRequest: PetCreationRequest): Response {
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

    @POST
    @Path("/{companyId}/ownerId/{ownerId}/adoptPets")
    fun adoptMultiplePetsByOwner(@PathParam("companyId") companyId: Long,@PathParam("ownerId") ownerId: Long, @RequestBody petIds: List<Long>): Response {
        petService.adoptMultiplePetsByOwner(ownerId, companyId, petIds)
        return Response.ok().build()
    }

    @POST
    @Path("/addMultiple")
    fun addMultiplePets(@RequestBody petCreationRequests: List<PetCreationRequest>): Response {
        petService.addMultiplePets(petCreationRequests)
        return Response.ok().build()
    }

}
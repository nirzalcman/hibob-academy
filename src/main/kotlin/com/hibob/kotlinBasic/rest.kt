package com.hibob.kotlinBasic


import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.springframework.stereotype.Controller

import org.springframework.web.bind.annotation.RequestBody


@Controller
@Path("/api/pets")
@Produces(MediaType.APPLICATION_JSON)


class PetResource{

    @GET
    fun getAllPets(): Response{
        val pets= listOf(Pet("1212121212" , "dogi" , "Dog") , Pet("14342324" , "cati" , "Cat"))
        return Response.ok(pets).build()

    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    fun addPet(@RequestBody pet: Pet): Response{
        return Response.ok(pet).build()
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    fun updatePet(@RequestBody pet: Pet): Response{
        return Response.ok(pet).build()
    }

    @DELETE
    @Path("{id}")
    fun deletePet(@PathParam("id") id: String): Response{
        println("pet with id $id has been deleted")
        return Response.ok().build()
    }


}







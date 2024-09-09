package com.hibob.academy.resource


import com.hibob.academy.model.Owner
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.springframework.stereotype.Controller

import org.springframework.web.bind.annotation.RequestBody


@Controller
@Path("/api/owner")
@Produces(MediaType.APPLICATION_JSON)
class OwnerResource {

    @GET
    fun getAllOwners(): Response {
        val owners = listOf(Owner("1212121212", "Nir", null, null), Owner("14342324", "Yosi", null, null))
        return Response.ok(owners).build()

    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    fun addOwner(@RequestBody owner: Owner): Response {
        val newOwner = if (owner.firstName.isNullOrEmpty() && owner.lastName.isNullOrEmpty()) {
            Owner(owner.id, owner.firstName + " " + owner.lastName, owner.firstName, owner.lastName)
        } else {
            val firstName = owner.name?.split(" ")?.first()
            val lastName = owner.name?.substringAfter(" ")
            Owner(owner.id, owner.name, firstName, lastName)
        }
        return Response.ok(newOwner).build()
    }


    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    fun updateOwner(@RequestBody owner: Owner): Response {
        val updateOwner = if (owner.firstName.isNullOrEmpty() && owner.lastName.isNullOrEmpty()) {
            Owner(owner.id, owner.firstName + " " + owner.lastName, owner.firstName, owner.lastName)
        } else {
            val firstName = owner.name?.split(" ")?.first()
            val lastName = owner.name?.split(" ")?.last()
            Owner(owner.id, owner.name, firstName, lastName)
        }
        return Response.ok(updateOwner).build()
    }


    @DELETE
    @Path("/{id}")
    fun deleteOwner(@PathParam("id") id: String): Response {
        println("owner with id $id has been deleted")
        return Response.ok().build()
    }

}



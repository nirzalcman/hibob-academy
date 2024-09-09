package com.hibob.academy.resource



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
    fun getAllOwners(): Response{
        val owners= listOf(Owner("1212121212" , "Nir" ,null,null) , Owner("14342324" , "Yosi" ,null,null))
        return Response.ok(owners).build()

    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    fun addOwner(@RequestBody owner: Owner): Response{
        val new_owner = if (owner.firstName!=null && owner.lastName!=null) {Owner(owner.id,owner.firstName + " " + owner.lastName,owner.firstName,owner.lastName)}
        else   {
            val firstName = owner.name?.split(" ")?.first()
            val lastName = owner.name?.split(" ")?.last()
            Owner(owner.id,owner.name,firstName,lastName)
        }
        return Response.ok(new_owner).build()
    }


    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    fun updateOwner(@RequestBody owner: Owner): Response{
        val update_owner = if (owner.firstName!=null && owner.lastName!=null) {Owner(owner.id,owner.firstName + " " + owner.lastName,owner.firstName,owner.lastName)}
        else   {
            val firstName = owner.name?.split(" ")?.first()
            val lastName = owner.name?.split(" ")?.last()
            Owner(owner.id,owner.name,firstName,lastName)
        }
        return Response.ok(update_owner).build()
    }


    @DELETE
    @Path("{id}")
    fun deleteOwner(@PathParam("id") id: String): Response{
        println("owner with id $id has been deleted")
        return Response.ok().build()
    }

}


data class Owner(val id: String, val name : String?,val firstName :String?, val lastName :String?)
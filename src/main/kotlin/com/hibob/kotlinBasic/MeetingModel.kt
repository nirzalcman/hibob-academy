package com.hibob.kotlinBasic


interface  Location {
    val country: String
    val city: String
    val street: String
}

data class USlocation(
    override val country: String,
    override val city : String,
    override val street: String,
    val zipCode : String) : Location

data class UKlocation(
    override val country: String,
    override val city : String,
    override val street: String,
    val postCode : String) : Location


//in this implement each reviewer has 1 review - the name of the person that the reviewr gave the review, the review .
data class Reviewer(val name: String, val review : Pair<String,String> )

data class Participant(val name: String, val email: String)

open class Meeting (val name : String , val location: Location , val participants: List<Participant>) {
    open fun addParticipant(participant: Participant ): Meeting {
        val participants = this.participants + participant
        return Meeting(this.name, this.location, participants)
    }

}
class PersonalReview(
    name : String ,
    location : Location,
    val participent : Participant,
    val reviewers : List<Reviewer>
) : Meeting(name, location , listOf(participent) ){

    init {
        println("Meeting creating")
    }

    override fun addParticipant(participant: Participant): Meeting {
        println("The personal Review is just for 1 participant")
        return this
    }

}
fun main(args: Array<String>) {


val usLocation = USlocation("USA", "New York", "5th Avenue", "2024")
val participant= ( Participant("John Doe", "john.doe@example.com"))
val reviewer = Reviewer("Jane Smith", "John Doe" to "Excellent work")


val personalReview = PersonalReview(
    "Performance ",
    usLocation,
    participant,
    listOf(reviewer)
)

val newParticipant = Participant("Nir", "Nir@example.com")
personalReview.addParticipant(newParticipant)

}



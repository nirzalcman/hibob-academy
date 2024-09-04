package com.hibob.kotlinBasic

open class Meeting (val name : String , val location: Location , val participants: List<Participant>) {
    fun addParticipant(participant: Participant , meeting: Meeting): Meeting {
        val participants = meeting.participants + participant
        return Meeting(meeting.name, meeting.location, participants)
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


}
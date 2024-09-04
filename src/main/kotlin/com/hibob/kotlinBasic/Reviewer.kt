package com.hibob.kotlinBasic
//in this implement each reviewr has 1 review - the name of the meeting , the review . if want more reviews it can be a map
data class Reviewer(val name: String, val review : Pair<String,String> )

package com.emt.elimupay

data class StudentListResponse(
    val admNumber: String,
    val dateOfAdmission: String,
    val dob: String,
    val dormitory: Any,
    val firstName: String,
    val grade: Int,
    val healthStatus: Any,
    val id: Int,
    val lastName: String,
    val middleName: String,
    val parentID: Int,
    val schoolCode: String,
    val schoolStatus: Any,
    val stream: String,
    val studentGender: String,
    val uniqueId: String,
    val upiNumber: String,
    val urls: String
)
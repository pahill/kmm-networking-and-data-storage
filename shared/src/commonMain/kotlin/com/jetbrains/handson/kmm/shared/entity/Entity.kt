package com.jetbrains.handson.kmm.shared.entity

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

data class RocketLaunch(
    //flight_number
    val flightNumber: Int,
    //name
    val missionName: String,
    //date_utc
    val launchDateUTC: String,
    //details
    val details: String?,
    //success
    val launchSuccess: Boolean?,
    //links
    val links: Links
) {
    var launchYear = launchDateUTC.toInstant().toLocalDateTime(TimeZone.UTC).year
}


data class Links(
    //patch
    val patch: Patch?,
    //article
    val article: String?
)

data class Patch(
    //small
    val small: String?,
    //large
    val large: String?
)
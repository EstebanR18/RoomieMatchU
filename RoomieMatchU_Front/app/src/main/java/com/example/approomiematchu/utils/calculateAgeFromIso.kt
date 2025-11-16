package com.example.approomiematchu.util

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Recibe una fecha ISO 'yyyy-MM-dd' (o con time 'yyyy-MM-dd...' ) y devuelve la edad en años
 * o null si no puede calcularla.
 */
fun calculateAgeFromIso(fechaIso: String?): Int? {
    if (fechaIso.isNullOrBlank()) return null

    // Normalizamos a solo la porción fecha: yyyy-MM-dd
    val fechaSolo = try {
        if (fechaIso.length >= 10) fechaIso.substring(0, 10) else fechaIso
    } catch (e: Exception) {
        fechaIso
    }

    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    sdf.timeZone = TimeZone.getTimeZone("UTC")

    return try {
        val birthDate = sdf.parse(fechaSolo) ?: return null
        val birthCal = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        birthCal.time = birthDate

        val now = Calendar.getInstance(TimeZone.getTimeZone("UTC"))

        var years = now.get(Calendar.YEAR) - birthCal.get(Calendar.YEAR)

        // Si aún no cumplió este año, restar 1
        val nowMonth = now.get(Calendar.MONTH)
        val birthMonth = birthCal.get(Calendar.MONTH)
        val nowDay = now.get(Calendar.DAY_OF_MONTH)
        val birthDay = birthCal.get(Calendar.DAY_OF_MONTH)

        if (nowMonth < birthMonth || (nowMonth == birthMonth && nowDay < birthDay)) {
            years -= 1
        }

        if (years < 0) null else years
    } catch (e: ParseException) {
        null
    } catch (t: Throwable) {
        null
    }
}

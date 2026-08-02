package com.rahmatsobrian.umkchecker.util

import java.text.NumberFormat
import java.util.Locale

/**
 * Formats raw Rupiah amounts consistently across the whole app.
 */
object CurrencyFormatter {

    private val idLocale = Locale.Builder().setLanguage("in").setRegion("ID").build()

    private val formatter: NumberFormat by lazy {
        NumberFormat.getCurrencyInstance(idLocale).apply {
            maximumFractionDigits = 0
        }
    }

    /** Example: 3123788 -> "Rp3.123.788" */
    fun format(amount: Long): String = formatter.format(amount)

    /** Example: 3123788 -> "Rp 3.123.788" (with a space, used in share text). */
    fun formatWithSpace(amount: Long): String = format(amount).replace("Rp", "Rp ")
}

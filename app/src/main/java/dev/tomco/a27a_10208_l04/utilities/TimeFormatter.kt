package dev.tomco.a27a_10208_l04.utilities

import java.util.Locale

object TimeFormatter {
    fun formatTime(delta: Long): String {
        var seconds = (delta / 1000).toInt()
        var minutes = seconds / 60
        seconds %= 60
        var hours = minutes / 60
        minutes %= 60
        hours %= 24

        return String.format(
            Locale.ROOT,
            "%02d:%02d:%02d",
            hours, minutes, seconds
        )
    }
}
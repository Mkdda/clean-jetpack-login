package com.maylbus.collectives.cleanarchitecturelogin.utils

// region emojis

private val thinkingEmojisList: List<String> = listOf(
    "\uD83E\uDD14", "\uD83E\uDD28", "\uD83E\uDD13",
    "\uD83E\uDDD0", "\uD83D\uDCAC", "\uD83D\uDCAD"
)

internal fun getRandomThinkingEmojis(): String {

    val randomIndex: Int = (thinkingEmojisList.indices - 1).shuffled().last()

    return thinkingEmojisList[randomIndex]
}

private val kid: IntRange = 7..12
private val boy: IntRange = 13..18
private val young: IntRange = 19..29
private val man: IntRange = 30..49
private val oldMan: IntRange = 50..70

val Int.emojiMaleByAge: String
    get() {

        return when (this) {

            in kid -> "\uD83D\uDC66"

            in boy -> "\uD83E\uDDD1"

            in young -> "\uD83D\uDC68"

            in man -> "\uD83E\uDDD4️"

            in oldMan -> "\uD83D\uDC74"

            else -> "\uD83D\uDC79"
        }
    }

private val girl: IntRange = boy
private val woman: IntRange = man
private val oldWoman: IntRange = oldMan

val Int.emojiFemaleByAge: String
    get() {

        return when(this) {

            in kid -> "\uD83E\uDDD2"

            in girl -> "\uD83D\uDC67"

            in young -> "\uD83D\uDC69"

            in woman -> "\uD83D\uDC69\u200D\uD83E\uDDB3"

            in oldWoman -> "\uD83D\uDC75"

            else -> "\uD83E\uDDDC"
        }
    }

// endregion

// region colors

private val colorsList: List<Long> = listOf(
    0XFF957DAD, 0XFFDFD3, 0XFF9ACC91,
    0XFF9ACC91, 0XFF90D0E6, 0XFF90D0E6,
    0XFF90D0E6, 0XFFECE4D0, 0XFFECE4D0
)

internal fun getRandomColor(): Long {

    val randomIndex: Int = (colorsList.indices - 1).shuffled().last()

    return colorsList[randomIndex]
}

// endregion

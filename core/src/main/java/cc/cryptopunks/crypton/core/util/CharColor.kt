package cc.cryptopunks.crypton.core.util

import cc.cryptopunks.crypton.core.R

val letterColors = mapOf(
    'a' to R.color.md_amber_600,
    'b' to R.color.md_blue_600,
    'c' to R.color.md_blue_grey_600,
    'd' to R.color.md_brown_600,
    'e' to R.color.md_cyan_500,
    'f' to R.color.md_deep_orange_500,
    'g' to R.color.md_deep_purple_500,
    'h' to R.color.md_green_600,
    'i' to R.color.md_grey_500,
    'j' to R.color.md_indigo_500,
    'k' to R.color.md_light_blue_500,
    'l' to R.color.md_light_green_500,
    'm' to R.color.md_lime_500,
    'n' to R.color.md_orange_500,
    'o' to R.color.md_pink_500,
    'p' to R.color.md_purple_500,
    'r' to R.color.md_red_500,
    's' to R.color.md_teal_500,
    't' to R.color.md_yellow_500,
    'u' to R.color.md_green_A700,
    'w' to R.color.md_blue_A400,
    'x' to R.color.md_cyan_A400,
    'y' to R.color.md_deep_purple_A100,
    'z' to R.color.md_indigo_A100
).withDefault {
    R.color.md_white_1000
}
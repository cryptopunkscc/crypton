package cc.cryptopunks.crypton.util

import cc.cryptopunks.crypton.core.R

val letterColors = mapOf(
    'a' to R.color.md_blue_200,
    'b' to R.color.md_green_200,
    'c' to R.color.md_red_200,
    'd' to R.color.md_orange_200,
    'e' to R.color.md_cyan_200,
    'f' to R.color.md_deep_orange_200,
    'g' to R.color.md_deep_purple_200,
    'h' to R.color.md_amber_200,
    'i' to R.color.md_grey_200,
    'j' to R.color.md_indigo_200,
    'k' to R.color.md_light_blue_200,
    'l' to R.color.md_light_green_200,
    'm' to R.color.md_lime_200,
    'n' to R.color.md_brown_200,
    'o' to R.color.md_pink_200,
    'p' to R.color.md_purple_200,
    'r' to R.color.md_blue_grey_200,
    's' to R.color.md_yellow_200,
    't' to R.color.md_lime_A100,
    'u' to R.color.md_green_A100,
    'w' to R.color.md_blue_A100,
    'x' to R.color.md_cyan_A100,
    'y' to R.color.md_deep_purple_A100,
    'z' to R.color.md_indigo_A100

).withDefault {
    R.color.md_white_1000
}
package com.jdroids.team7026.lib.util

object Util {
    val epsilon = 1E-6

    fun epsilonEquals(a: Double, b: Double, epsilon: Double=Util.epsilon): Boolean =
            (a - epsilon <= b) && (a + epsilon >= b)
}
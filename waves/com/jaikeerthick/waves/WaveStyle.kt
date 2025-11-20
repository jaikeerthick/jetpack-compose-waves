/*
 * Copyright (c) 2025 Jai Keerthick
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */
package com.jaikeerthick.waves

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Predefined wave styles for easy usage.
 */
enum class WaveStyle {
    Calm,
    Gentle,
    Energetic
}

/**
 * Internal data class representing resolved style values.
 */
data class WaveStyleData(
    val waveCount: Int,
    val waveAmplitude: Dp,
    val waveSpeed: Float,
    val verticalOscillationSpeed: Float
)

fun WaveStyle.toData(): WaveStyleData =
    when (this) {
        WaveStyle.Calm -> WaveStyleData(
            waveCount = 3,
            waveAmplitude = 14.dp,
            waveSpeed = 2600f,
            verticalOscillationSpeed = 3000f
        )
        WaveStyle.Gentle -> WaveStyleData(
            waveCount = 5,
            waveAmplitude = 20.dp,
            waveSpeed = 1500f,
            verticalOscillationSpeed = 2000f
        )
        WaveStyle.Energetic -> WaveStyleData(
            waveCount = 7,
            waveAmplitude = 28.dp,
            waveSpeed = 900f,
            verticalOscillationSpeed = 1200f
        )
    }

/**
 * Overload: Use WaveStyle presets.
 */
@Composable
fun WavyBackground(
    modifier: Modifier = Modifier,
    color: Color = Color.Black,
    waveDirection: WaveDirection = WaveDirection.Top,
    style: WaveStyle = WaveStyle.Gentle,
    reverseDirection: Boolean = false,
    animateWaveShape: Boolean = true
) {
    val data = style.toData()

    WavyBackground(
        modifier = modifier,
        color = color,
        waveDirection = waveDirection,
        waveCount = data.waveCount,
        waveAmplitude = data.waveAmplitude,
        waveSpeed = data.waveSpeed,
        verticalOscillationSpeed = data.verticalOscillationSpeed,
        reverseDirection = reverseDirection,
        animateWaveShape = animateWaveShape
    )
}

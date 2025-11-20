/*
 * Copyright (c) 2025 Jai Keerthick
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */
package com.jaikeerthick.waves

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun PreviewWavyTop() {
    WavyBackground(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp),
        color = Color(0xFF4FC3F7),
        waveDirection = WaveDirection.Top,
        style = WaveStyle.Gentle
    )
}

@Preview
@Composable
fun PreviewWavyBottom() {
    WavyBackground(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp),
        color = Color(0xFF4DB6AC),
        waveDirection = WaveDirection.Bottom,
        style = WaveStyle.Gentle
    )
}

@Preview
@Composable
fun PreviewCalm() {
    WavyBackground(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp),
        color = Color(0xFF90CAF9),
        style = WaveStyle.Calm
    )
}

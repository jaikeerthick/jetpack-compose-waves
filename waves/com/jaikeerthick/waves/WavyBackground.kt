/*
 * Copyright (c) 2025 Jai Keerthick
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */
package com.jaikeerthick.waves

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.min
import kotlin.math.sin

/**
 * Draws an animated fluid wave background using a Canvas.
 *
 * This composable creates smooth sinusoidal waves whose crest touches either
 * the **top** or **bottom** edge of the container. The wave continuously animates
 * horizontally, with optional vertical "breathing" motion for a more organic look.
 *
 * It is designed to be drop-in and works for any container height — even extremely
 * small values like **1.dp** — by auto-scaling the amplitude to prevent clipping or
 * distortion.
 *
 * Example:
 * ```
 * WavyBackground(
 *     modifier = Modifier
 *         .fillMaxWidth()
 *         .height(160.dp),
 *     color = Color(0xFF4FC3F7),
 *     waveDirection = WaveDirection.Top,
 *     waveCount = 5,
 *     waveAmplitude = 20.dp
 * )
 * ```
 *
 * @param modifier Modifier to apply to the Canvas (size, padding, layout behavior).
 *
 * @param color The fill color of the wave shape.
 *
 * @param waveDirection Controls which edge the wave’s crest touches:
 *  - [WaveDirection.Top] → Crest is drawn from the top edge downward.
 *  - [WaveDirection.Bottom] → Crest is drawn from the bottom edge upward.
 *
 * @param waveCount Number of full sine waves drawn across the width.
 * More waves → tighter, more frequent ripples.
 *
 * @param waveAmplitude The vertical height of each wave crest and trough.
 * This value auto-scales if the container height is too small.
 *
 * @param waveSpeed Duration (in milliseconds) for one full horizontal loop.
 * Lower values = faster animation.
 *
 * @param verticalOscillationSpeed Speed of the optional breathing motion
 * that slightly varies the amplitude over time.
 * Affects how “alive” or organic the wave feels.
 *
 * @param reverseDirection If true, the horizontal animation runs left-to-right.
 * If false, the wave moves right-to-left (default).
 *
 * @param animateWaveShape Enables or disables vertical “breathing”.
 * Turning this off provides a clean, single-frequency wave.
 *
 * @see WaveDirection
 * @see WaveStyle for preset configurations that wrap this composable.
 */

@Composable
fun WavyBackground(
    modifier: Modifier = Modifier,
    color: Color = Color.Black,
    waveDirection: WaveDirection = WaveDirection.Top,
    waveCount: Int = 5,
    waveAmplitude: Dp = 20.dp,
    waveSpeed: Float = 1200f,
    verticalOscillationSpeed: Float = 1500f,
    reverseDirection: Boolean = false,
    animateWaveShape: Boolean = true
) {
    var phase by remember { mutableStateOf(0f) }
    var verticalPhase by remember { mutableStateOf(0f) }

    // ---- Animation Loop ----
    LaunchedEffect(Unit) {
        var last: Long? = null
        while (true) {
            withFrameMillis { time ->
                val prev = last
                last = time
                if (prev == null) return@withFrameMillis

                val delta = (time - prev).toFloat()
                val tau = (2 * PI).toFloat()

                val dir = if (reverseDirection) 1f else -1f

                // horizontal movement
                phase = (phase + (delta / waveSpeed) * tau * dir) % tau
                if (phase < 0) phase += tau

                // vertical “breathing”
                if (animateWaveShape) {
                    verticalPhase =
                        (verticalPhase + (delta / verticalOscillationSpeed) * tau) % tau
                }
            }
        }
    }

    Canvas(modifier = modifier.fillMaxSize()) {

        val width = size.width
        val height = size.height

        val amplitudePx = waveAmplitude.toPx()

        // ---- AUTO-SCALING: ensures wave never flips OR clips ----
        val intrinsicHeight = amplitudePx * 2f
        val scale = min(1f, height / intrinsicHeight)
        val scaledAmp = amplitudePx * scale

        if (scaledAmp == 0f) {
            drawRect(color = color)
            return@Canvas
        }

        val tau = (2 * PI).toFloat()
        val wavelength = width / waveCount.coerceAtLeast(1)

        // ---- Crest-touch baseline ----
        val baseline = when (waveDirection) {
            WaveDirection.Top -> scaledAmp
            WaveDirection.Bottom -> height - scaledAmp
        }

        fun animatedAmplitude(progress: Float): Float {
            if (!animateWaveShape) return scaledAmp
            val p = abs(progress)
            return scaledAmp * (0.8f + 0.2f * sin(verticalPhase + p * 0.5f))
        }

        val path = Path()

        if (waveDirection == WaveDirection.Top) {

            // Crest at the top edge
            path.moveTo(0f, 0f)

            var x = 0
            while (x <= width.toInt()) {
                val progress = (x / wavelength) * tau
                val y = baseline + animatedAmplitude(progress) * sin(progress + phase)
                path.lineTo(x.toFloat(), y)
                x += 3
            }

            path.lineTo(width, height)
            path.lineTo(0f, height)
            path.close()

        } else {
            // Crest at the bottom edge
            path.moveTo(0f, 0f)
            path.lineTo(width, 0f)

            var x = width.toInt()
            while (x >= 0) {
                val progress = (x / wavelength) * tau

                val y = baseline + animatedAmplitude(progress) * sin(progress + phase)
                path.lineTo(x.toFloat(), y)

                x -= 3
            }

            path.lineTo(0f, height)
            path.close()
        }


        drawPath(
            path = path,
            color = color,
            style = Fill
        )
    }
}

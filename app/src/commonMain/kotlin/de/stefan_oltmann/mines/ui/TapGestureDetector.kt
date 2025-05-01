/*
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.stefan_oltmann.mines.ui

import androidx.compose.foundation.gestures.GestureCancellationException
import androidx.compose.foundation.gestures.PressGestureScope
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.AwaitPointerEventScope
import androidx.compose.ui.input.pointer.PointerEventTimeoutCancellationException
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.unit.Density
import androidx.compose.ui.util.fastAny
import androidx.compose.ui.util.fastForEach
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex

/*
 * Copied from androidx.compose.foundation.gestures.TapGestureDetector
 * and modified for custom long press millis
 */

@Suppress("warnings", "all")
internal suspend fun PointerInputScope.detectTapGesturesMod(
    onDoubleTap: ((Offset) -> Unit)? = null,
    onLongPress: ((Offset) -> Unit)? = null,
    onPress: suspend PressGestureScope.(Offset) -> Unit = NoPressGesture,
    onTap: ((Offset) -> Unit)? = null,
    longPressTimeoutMillis: Long
) = coroutineScope {

    val pressScope = PressGestureScopeImpl(this@detectTapGesturesMod)

    awaitEachGesture {

        val down = awaitFirstDown()

        down.consume()

        launch {
            pressScope.reset()
        }

        if (onPress !== NoPressGesture)
            launch {
                pressScope.onPress(down.position)
            }

        val longPressTimeout = onLongPress?.let {
            longPressTimeoutMillis
        } ?: (Long.MAX_VALUE / 2)

        var upOrCancel: PointerInputChange? = null

        try {

            upOrCancel = withTimeout(longPressTimeout) {
                waitForUpOrCancellation()
            }

            if (upOrCancel == null) {

                launch {
                    pressScope.cancel()
                }

            } else {

                upOrCancel.consume()

                launch {
                    pressScope.release()
                }
            }

        } catch (_: PointerEventTimeoutCancellationException) {

            onLongPress?.invoke(down.position)

            consumeUntilUp()

            launch {
                pressScope.release()
            }
        }

        if (upOrCancel != null) {

            if (onDoubleTap == null) {

                onTap?.invoke(upOrCancel.position)

            } else {

                val secondDown = awaitSecondDown(upOrCancel)

                if (secondDown == null) {

                    onTap?.invoke(upOrCancel.position)

                } else {

                    launch {
                        pressScope.reset()
                    }

                    if (onPress !== NoPressGesture) {
                        launch { pressScope.onPress(secondDown.position) }
                    }

                    try {

                        withTimeout(longPressTimeout) {

                            val secondUp = waitForUpOrCancellation()

                            if (secondUp != null) {

                                secondUp.consume()

                                launch {
                                    pressScope.release()
                                }

                                onDoubleTap(secondUp.position)

                            } else {

                                launch {
                                    pressScope.cancel()
                                }

                                onTap?.invoke(upOrCancel.position)
                            }
                        }

                    } catch (e: PointerEventTimeoutCancellationException) {

                        onTap?.invoke(upOrCancel.position)

                        onLongPress?.invoke(secondDown.position)

                        consumeUntilUp()

                        launch {
                            pressScope.release()
                        }
                    }
                }
            }
        }
    }
}

@Suppress("warnings", "all")
private class PressGestureScopeImpl(
    density: Density
) : PressGestureScope, Density by density {

    private var isReleased = false
    private var isCanceled = false

    private val mutex = Mutex(locked = false)

    /**
     * Called when a gesture has been canceled.
     */
    fun cancel() {
        isCanceled = true
        mutex.unlock()
    }

    /**
     * Called when all pointers are up.
     */
    fun release() {
        isReleased = true
        mutex.unlock()
    }

    /**
     * Called when a new gesture has started.
     */
    suspend fun reset() {
        mutex.lock()
        isReleased = false
        isCanceled = false
    }

    override suspend fun awaitRelease() {
        if (!tryAwaitRelease()) {
            throw GestureCancellationException("The press gesture was canceled.")
        }
    }

    override suspend fun tryAwaitRelease(): Boolean {

        if (!isReleased && !isCanceled) {
            mutex.lock()
            mutex.unlock()
        }

        return isReleased
    }
}

/**
 * Consumes all pointer events until nothing is pressed and then returns. This method assumes
 * that something is currently pressed.
 */
private suspend fun AwaitPointerEventScope.consumeUntilUp() {
    do {
        val event = awaitPointerEvent()
        event.changes.fastForEach { it.consume() }
    } while (event.changes.fastAny { it.pressed })
}

private suspend fun AwaitPointerEventScope.awaitSecondDown(
    firstUp: PointerInputChange
): PointerInputChange? = withTimeoutOrNull(viewConfiguration.doubleTapTimeoutMillis) {

    val minUptime = firstUp.uptimeMillis + viewConfiguration.doubleTapMinTimeMillis
    var change: PointerInputChange

    do {
        change = awaitFirstDown()
    } while (change.uptimeMillis < minUptime)

    change
}

private val NoPressGesture: suspend PressGestureScope.(Offset) -> Unit = {}

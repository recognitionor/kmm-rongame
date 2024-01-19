package com.jhlee.kmm_rongame.common.view

import androidx.compose.foundation.Indication
import androidx.compose.foundation.IndicationInstance
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import com.jhlee.kmm_rongame.ui.theme.LightColorScheme

object ClickableDefaults {
    @Composable
    fun getDefaultClickable(
        pressedColor: Color = LightColorScheme.secondaryContainer,
        pressedAlpha: Float = 0.5f,
        click: () -> Unit
    ): Modifier {
        return Modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = Clickable(pressedColor, pressedAlpha)
        ) {
            click.invoke()
        }
    }
}

private class Clickable(
    private val pressedColor: Color, private val pressedAlpha: Float
) : Indication {

    private class DefaultIndicationInstance(
        private val isPressed: State<Boolean>,
        private val pressedColor: Color,
        private val pressedAlpha: Float
    ) : IndicationInstance {

        override fun ContentDrawScope.drawIndication() {
            drawContent()
            if (isPressed.value) {
                drawRect(color = pressedColor.copy(alpha = pressedAlpha), size = size)
            }
        }
    }

    @Composable
    override fun rememberUpdatedInstance(interactionSource: InteractionSource): IndicationInstance {
        val isPressed = interactionSource.collectIsPressedAsState()
        return remember(interactionSource) {
            DefaultIndicationInstance(isPressed, pressedColor, pressedAlpha)
        }
    }
}
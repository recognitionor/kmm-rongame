package com.jhlee.kmm_rongame.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import com.jhlee.kmm_rongame.ui.theme.BackgroundDark
import com.jhlee.kmm_rongame.ui.theme.BackgroundLight
import com.jhlee.kmm_rongame.ui.theme.ErrorContainerDark
import com.jhlee.kmm_rongame.ui.theme.ErrorContainerLight
import com.jhlee.kmm_rongame.ui.theme.ErrorDark
import com.jhlee.kmm_rongame.ui.theme.ErrorLight
import com.jhlee.kmm_rongame.ui.theme.GreenContainerDark
import com.jhlee.kmm_rongame.ui.theme.GreenContainerLight
import com.jhlee.kmm_rongame.ui.theme.GreenPrimaryDark
import com.jhlee.kmm_rongame.ui.theme.GreenPrimaryLight
import com.jhlee.kmm_rongame.ui.theme.GreenSecondaryContainerDark
import com.jhlee.kmm_rongame.ui.theme.GreenSecondaryContainerLight
import com.jhlee.kmm_rongame.ui.theme.GreenSecondaryDark
import com.jhlee.kmm_rongame.ui.theme.GreenSecondaryLight
import com.jhlee.kmm_rongame.ui.theme.GreenTertiaryContainerDark
import com.jhlee.kmm_rongame.ui.theme.GreenTertiaryContainerLight
import com.jhlee.kmm_rongame.ui.theme.GreenTertiaryDark
import com.jhlee.kmm_rongame.ui.theme.GreenTertiaryLight
import com.jhlee.kmm_rongame.ui.theme.OnBackgroundDark
import com.jhlee.kmm_rongame.ui.theme.OnBackgroundLight
import com.jhlee.kmm_rongame.ui.theme.OnErrorContainerDark
import com.jhlee.kmm_rongame.ui.theme.OnErrorContainerLight
import com.jhlee.kmm_rongame.ui.theme.OnErrorDark
import com.jhlee.kmm_rongame.ui.theme.OnErrorLight
import com.jhlee.kmm_rongame.ui.theme.OnGreenContainerDark
import com.jhlee.kmm_rongame.ui.theme.OnGreenContainerLight
import com.jhlee.kmm_rongame.ui.theme.OnGreenDark
import com.jhlee.kmm_rongame.ui.theme.OnGreenLight
import com.jhlee.kmm_rongame.ui.theme.OnGreenSecondaryContainerDark
import com.jhlee.kmm_rongame.ui.theme.OnGreenSecondaryContainerLight
import com.jhlee.kmm_rongame.ui.theme.OnGreenSecondaryDark
import com.jhlee.kmm_rongame.ui.theme.OnGreenSecondaryLight
import com.jhlee.kmm_rongame.ui.theme.OnGreenTertiaryContainerDark
import com.jhlee.kmm_rongame.ui.theme.OnGreenTertiaryContainerLight
import com.jhlee.kmm_rongame.ui.theme.OnGreenTertiaryDark
import com.jhlee.kmm_rongame.ui.theme.OnGreenTertiaryLight
import com.jhlee.kmm_rongame.ui.theme.OnSurfaceDark
import com.jhlee.kmm_rongame.ui.theme.OnSurfaceLight
import com.jhlee.kmm_rongame.ui.theme.OnSurfaceVariantDark
import com.jhlee.kmm_rongame.ui.theme.OnSurfaceVariantLight
import com.jhlee.kmm_rongame.ui.theme.OutlineDark
import com.jhlee.kmm_rongame.ui.theme.OutlineLight
import com.jhlee.kmm_rongame.ui.theme.SurfaceDark
import com.jhlee.kmm_rongame.ui.theme.SurfaceLight
import com.jhlee.kmm_rongame.ui.theme.SurfaceVariantDark
import com.jhlee.kmm_rongame.ui.theme.SurfaceVariantLight

val DarkColorScheme = darkColorScheme(
    primary = GreenPrimaryDark,
    secondary = GreenSecondaryDark,
    tertiary = GreenTertiaryDark,
    onPrimary = OnGreenDark,
    primaryContainer = GreenContainerDark,
    onPrimaryContainer = OnGreenContainerDark,
    onSecondary = OnGreenSecondaryDark,
    secondaryContainer = GreenSecondaryContainerDark,
    onSecondaryContainer = OnGreenSecondaryContainerDark,
    onTertiary = OnGreenTertiaryDark,
    onTertiaryContainer = OnGreenTertiaryContainerDark,
    tertiaryContainer = GreenTertiaryContainerDark,
    background = BackgroundDark,
    onBackground = OnBackgroundDark,
    surface = SurfaceDark,
    onSurface = OnSurfaceDark,
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = OnSurfaceVariantDark,
    error = ErrorDark,
    onError = OnErrorDark,
    errorContainer = ErrorContainerDark,
    onErrorContainer = OnErrorContainerDark,
    outline = OutlineDark,
)

val LightColorScheme = lightColorScheme(
    primary = GreenPrimaryLight,
    secondary = GreenSecondaryLight,
    tertiary = GreenTertiaryLight,
    onPrimary = OnGreenLight,
    primaryContainer = GreenContainerLight,
    onPrimaryContainer = OnGreenContainerLight,
    onSecondary = OnGreenSecondaryLight,
    secondaryContainer = GreenSecondaryContainerLight,
    onSecondaryContainer = OnGreenSecondaryContainerLight,
    onTertiary = OnGreenTertiaryLight,
    onTertiaryContainer = OnGreenTertiaryContainerLight,
    tertiaryContainer = GreenTertiaryContainerLight,
    background = BackgroundLight,
    onBackground = OnBackgroundLight,
    surface = SurfaceLight,
    onSurface = OnSurfaceLight,
    surfaceVariant = SurfaceVariantLight,
    onSurfaceVariant = OnSurfaceVariantLight,
    error = ErrorLight,
    onError = OnErrorLight,
    errorContainer = ErrorContainerLight,
    onErrorContainer = OnErrorContainerLight,
    outline = OutlineLight,
)
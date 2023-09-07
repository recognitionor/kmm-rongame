package com.jhlee.kmm_rongame.core.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.desc.Resource
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.resources.format

@Composable
actual fun getString(
    id: StringResource,
    args: List<Any>
): String {
    return if (args.isEmpty()) {
        StringDesc.Resource(id).toString(context = LocalContext.current)
    } else {
        id.format(*args.toTypedArray()).toString(LocalContext.current)
    }
}

@Composable
actual fun getString(id: StringResource): String {
    return StringDesc.Resource(id).toString(context = LocalContext.current)
}

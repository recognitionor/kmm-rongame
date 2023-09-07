package com.jhlee.kmm_rongame.core.presentation

import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.desc.Resource
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.resources.format

actual fun getString(
    id: StringResource, args: List<Any>
): String {
    return if (args.isEmpty()) {
        StringDesc.Resource(id).localized()
    } else {
        id.format(*args.toTypedArray()).localized()
    }
}

actual fun getString(id: StringResource): String {
    return StringDesc.Resource(id).localized()
}
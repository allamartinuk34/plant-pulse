package com.plntpls.appssxdc.domain.model

enum class CareKind { WATER, MIST, FEED }

data class CareEntry(
    val id: String,
    val plantId: String,
    val plantName: String,
    val dayStamp: Int,
    val kind: CareKind,
    val note: String
)

package com.notes.backend.notes.domain

import java.time.Instant

data class Note(
    val id: Id?,
    val title: String,
    val value: String,
    val archived: Boolean,

    val createdAt: Instant?,
    val updatedAt: Instant?,
) {
    @JvmInline
    value class Id(val value: String)
}
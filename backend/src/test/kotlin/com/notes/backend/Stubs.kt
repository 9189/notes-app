package com.notes.backend

import com.notes.backend.notes.domain.Note
import java.time.Instant

const val NOTE_ID = "20c15f29-fc3c-4164-ac02-1183734a61a7"
const val NOTE_ID_2 = "20c15f29-fc3c-4164-ac02-1183734a61a8"

val SOME_NOTE = Note(
    id = Note.Id(NOTE_ID),
    title = "some title",
    value = "some note",
    archived = false,
    createdAt = Instant.parse("2020-01-01T15:00:00.00Z"),
    updatedAt = Instant.parse("2020-01-01T15:00:00.00Z"),
)
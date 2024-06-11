package com.notes.backend.notes

import com.notes.backend.NOTE_ID
import com.notes.backend.SOME_NOTE
import com.notes.backend.notes.domain.Note
import com.notes.backend.notes.domain.NoteService
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.Instant

class NoteServiceTest {

    val sut = NoteService(
        noteCreator = {},
        notesProvider = { emptyList() },
        notesUpdater = notesUpdaterStub,
        noteDeleter = { },
    )

    @Test
    fun `throws exception when IDs to update do not match`() {
        assertThrows<RuntimeException> {
            sut.updateNote(Note.Id("forSureNotMatchingId"), SOME_NOTE)
        }
    }
}

private val notesUpdaterStub = { _: Note ->
    Note(
        id = Note.Id(NOTE_ID),
        title = "some title",
        value = "my note",
        archived = false,
        createdAt = Instant.parse("2024-06-01T18:35:24.00Z"),
        updatedAt = Instant.parse("2024-06-01T18:35:24.00Z"),
    )
}
package com.notes.backend.notes.domain

import org.springframework.stereotype.Service

@Service
class NoteService(
    private val noteCreator: NoteCreator,
    private val notesProvider: NotesProvider,
    private val notesUpdater: NoteUpdater,
    private val noteDeleter: NoteDeleter,
) {

    fun createNote(note: Note) =
        noteCreator.create(note)

    fun getNotes(filterCriteria: FilterCriteria) =
        notesProvider.getAll(filterCriteria)

    fun updateNote(noteId: Note.Id, noteToUpdate: Note): Note? {
        if (noteId != noteToUpdate.id) throw RuntimeException()

        return notesUpdater.update(noteToUpdate)
    }

    fun deleteNote(id: Note.Id) =
        noteDeleter.delete(id)
}

data class FilterCriteria(
    val archived: Boolean
)
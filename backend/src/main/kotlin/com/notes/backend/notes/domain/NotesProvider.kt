package com.notes.backend.notes.domain

fun interface NoteCreator {
    fun create(note: Note)
}

fun interface NotesProvider {
    fun getAll(filterCriteria: FilterCriteria): List<Note>
}

fun interface NoteUpdater {
    fun update(note: Note): Note?
}

fun interface NoteDeleter {
    fun delete(id: Note.Id)
}
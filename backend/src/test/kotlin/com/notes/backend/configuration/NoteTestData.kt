package com.notes.backend.configuration

import com.notes.backend.notes.domain.Note
import com.notes.backend.notes.persistence.NoteTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Component
import java.util.UUID
import javax.sql.DataSource

@Component
class NoteTestData(
    private val dataSource: DataSource
) {

    fun createNotes(vararg notes: Note) = transaction(Database.connect(dataSource)) {
        notes.forEach {
            createNote(it)
        }
    }

    fun createNote(note: Note) = transaction(Database.connect(dataSource)) {
        NoteTable.insert {
            it[id] = UUID.fromString(note.id?.value)
            it[title] = note.title
            it[value] = note.value
            it[archived] = note.archived
            note.createdAt?.let { created -> it[createdAt] = created }
            note.updatedAt?.let { updated -> it[updatedAt] = updated }
        }
    }

    fun getNotes() = transaction(Database.connect(dataSource)) {
        NoteTable.selectAll().map {
            Note(
                id = Note.Id(it[NoteTable.id].toString()),
                title = it[NoteTable.title],
                value = it[NoteTable.value],
                archived = it[NoteTable.archived],
                createdAt = it[NoteTable.createdAt],
                updatedAt = it[NoteTable.updatedAt],
            )
        }
    }
}
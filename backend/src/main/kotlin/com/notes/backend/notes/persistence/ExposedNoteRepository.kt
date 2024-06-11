package com.notes.backend.notes.persistence

import com.notes.backend.notes.domain.FilterCriteria
import com.notes.backend.notes.domain.Note
import com.notes.backend.notes.domain.NoteCreator
import com.notes.backend.notes.domain.NoteDeleter
import com.notes.backend.notes.domain.NoteUpdater
import com.notes.backend.notes.domain.NotesProvider
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import org.springframework.stereotype.Repository
import java.time.Instant
import java.util.UUID

@Repository
class ExposedNoteRepository : NoteCreator, NotesProvider, NoteUpdater, NoteDeleter {

    override fun create(note: Note): Unit = transaction {
        NoteTable.insert {
            it[title] = note.title
            it[value] = note.value
            it[archived] = note.archived
        }
    }

    override fun getAll(filterCriteria: FilterCriteria): List<Note> = transaction {
        NoteTable.selectAll()
            .where(NoteTable.archived eq filterCriteria.archived)
            .orderBy(NoteTable.updatedAt to SortOrder.DESC)
            .map { it.toDomain() }
    }

    override fun update(note: Note): Note? = transaction {
        NoteTable.update({ NoteTable.id eq UUID.fromString(note.id?.value) }) {
            it[title] = note.title
            it[value] = note.value
            it[archived] = note.archived
            it[updatedAt] = Instant.now()
        }

        NoteTable.selectAll()
            .where(NoteTable.id eq UUID.fromString(note.id?.value))
            .map { it.toDomain() }
            .singleOrNull()
    }

    override fun delete(id: Note.Id): Unit = transaction {
        NoteTable.deleteWhere { NoteTable.id eq UUID.fromString(id.value) }
    }
}

private fun ResultRow.toDomain() = Note(
    id = Note.Id(this[NoteTable.id].toString()),
    title = this[NoteTable.title],
    value = this[NoteTable.value],
    archived = this[NoteTable.archived],
    createdAt = this[NoteTable.createdAt],
    updatedAt = this[NoteTable.updatedAt],
)
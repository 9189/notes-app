package com.notes.backend.notes.web

import com.notes.backend.notes.domain.FilterCriteria
import com.notes.backend.notes.domain.Note
import com.notes.backend.notes.domain.NoteService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.time.Instant

@RequestMapping("/api/notes")
@RestController
class NoteController(
    private val noteService: NoteService
) {
    // TODO: error handling - add application/problem+json exception handler

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    fun createNote(@RequestBody noteDto: NoteDto) =
        noteService.createNote(noteDto.toDomain())

    @GetMapping
    fun getNotes(
        @RequestParam archived: Boolean
    ): GetAllNotesDto =
        // TODO: pagination
        GetAllNotesDto(
            noteService
                .getNotes(FilterCriteria(archived = archived))
                .map { it.toDto() }
        )

    @PutMapping("/{id}")
    fun updateNotes(
        @PathVariable id: String,
        @RequestBody noteDto: NoteDto
    ): NoteDto? = noteService.updateNote(Note.Id(id), noteDto.toDomain())?.toDto()

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    fun deleteNotes(@PathVariable id: String) =
        noteService.deleteNote(Note.Id(id))
}

data class GetAllNotesDto(
    val items: List<NoteDto>
)

data class NoteDto(
    val id: String? = null,
    val title: String,
    val value: String,
    val archived: Boolean,

    val createdAt: String? = null,
    val updatedAt: String? = null,
)

private fun Note.toDto() = NoteDto(
    id = this.id?.value,
    title = this.title,
    value = this.value,
    archived = this.archived,
    createdAt = this.createdAt.toString(),
    updatedAt = this.updatedAt.toString(),
)

private fun NoteDto.toDomain() = Note(
    id = this.id?.let { Note.Id(it) },
    title = this.title,
    value = this.value,
    archived = this.archived,
    createdAt = this.createdAt?.let { Instant.parse(it) },
    updatedAt = this.updatedAt?.let { Instant.parse(it) },
)
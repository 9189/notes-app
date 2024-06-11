package com.notes.backend

import com.notes.backend.configuration.IntegrationTest
import com.notes.backend.configuration.NoteTestData
import com.notes.backend.configuration.PostgresTables
import com.notes.backend.notes.domain.Note
import com.notes.backend.notes.web.GetAllNotesDto
import com.notes.backend.notes.web.NoteDto
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpEntity
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.jdbc.core.simple.JdbcClient
import org.springframework.web.client.RestClient
import java.time.Instant

@IntegrationTest
class NoteEndpointIT(
    val restClient: RestClient,
    val jdbcClient: JdbcClient,
    val noteTestData: NoteTestData,
) {

    @BeforeEach
    fun setup() {
        PostgresTables.truncateAll(jdbcClient)
    }

    @Test
    fun `create note`() {
        val response = restClient
            .post()
            .uri("/api/notes")
            .body(
                NoteDto(
                    title = "new note title!",
                    value = "new note value!",
                    archived = false,
                )
            )
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .toEntity(NoteDto::class.java)

        SoftAssertions.assertSoftly {
            assertThat(response.statusCode).isEqualTo(HttpStatus.CREATED)

            val testData = noteTestData.getNotes()
            assertThat(testData).singleElement()
            assertThat(testData[0].title).isEqualTo("new note title!")
            assertThat(testData[0].value).isEqualTo("new note value!")
            assertThat(testData[0].archived).isFalse()
        }
    }

    @Test
    fun `get notes with filter`() {
        noteTestData.createNotes(
            Note(
                id = Note.Id(NOTE_ID),
                title = "old note",
                value = "my note",
                archived = true,
                createdAt = Instant.parse("2020-06-01T18:35:24.00Z"),
                updatedAt = Instant.parse("2020-06-01T18:35:24.00Z"),
            ),
            Note(
                id = Note.Id(NOTE_ID_2),
                title = "newer note",
                value = "my note",
                archived = true,
                createdAt = Instant.parse("2024-06-01T18:35:24.00Z"),
                updatedAt = Instant.parse("2024-06-01T18:35:24.00Z"),
            )
        )

        val response = restClient
            .get()
            .uri("/api/notes?archived=true")
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .toEntity(GetAllNotesDto::class.java)

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        val body = response.body!!
        assertThat(body.items).containsExactly(
            NoteDto(
                id = NOTE_ID_2,
                title = "newer note",
                value = "my note",
                archived = true,
                createdAt = "2024-06-01T18:35:24Z",
                updatedAt = "2024-06-01T18:35:24Z",
            ),
            NoteDto(
                id = NOTE_ID,
                title = "old note",
                value = "my note",
                archived = true,
                createdAt = "2020-06-01T18:35:24Z",
                updatedAt = "2020-06-01T18:35:24Z",
            )
        )
    }

    @Test
    fun `update note`() {
        noteTestData.createNote(
            Note(
                id = Note.Id(NOTE_ID),
                title = "some title",
                value = "my note",
                archived = false,
                createdAt = Instant.parse("2024-06-01T18:35:24.00Z"),
                updatedAt = Instant.parse("2024-06-01T18:35:24.00Z"),
            )
        )

        val response = restClient
            .put()
            .uri("/api/notes/$NOTE_ID")
            .body(
                NoteDto(
                    id = NOTE_ID,
                    title = "new title!",
                    value = "new value!",
                    archived = true,
                    createdAt = "1999-06-01T18:35:24.00Z",
                    updatedAt = "1999-06-01T18:35:24.00Z",
                )
            )
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .toEntity(NoteDto::class.java)

        SoftAssertions.assertSoftly {
            assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
            val body = response.body!!
            assertThat(body.id).isEqualTo(NOTE_ID)
            assertThat(body.title).isEqualTo("new title!")
            assertThat(body.value).isEqualTo("new value!")
            assertThat(body.archived).isEqualTo(true)
            assertThat(body.createdAt).isEqualTo("2024-06-01T18:35:24Z")
            assertThat(body.updatedAt).isNotEqualTo("1999-06-01T18:35:24.00Z")
            assertThat(body.updatedAt).isNotEqualTo("2024-06-01T18:35:24Z")
        }
    }

    @Test
    fun `delete note`() {
        noteTestData.createNote(
            Note(
                id = Note.Id(NOTE_ID),
                title = "some title",
                value = "my note",
                archived = false,
                createdAt = Instant.parse("2024-06-01T18:35:24.00Z"),
                updatedAt = Instant.parse("2024-06-01T18:35:24.00Z"),
            )
        )

        val response = restClient
            .delete()
            .uri("/api/notes/$NOTE_ID")
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .toEntity(HttpEntity.EMPTY::class.java)

        assertThat(response.statusCode).isEqualTo(HttpStatus.NO_CONTENT)
        assertThat(noteTestData.getNotes()).isEmpty()
    }
}
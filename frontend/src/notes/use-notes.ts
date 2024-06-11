import {useState} from "react";
import {ArchivationState} from "./NotesControl";
import {
  useCreateNoteRepository,
  useDeleteNoteRepository,
  useNotesRepository,
  useUpdateNoteRepository
} from "./notes-repository";
import {Note} from "./model";
import {useDisclosure} from "@chakra-ui/react";

export default function useNotes() {
  const [listSelection, setListSelection] = useState<ArchivationState>('active')
  const [selectedNote, setSelectedNote] = useState<Note>()
  const {data, isPending, refetch} = useNotesRepository(listSelection === 'archived')
  const {create, isPending: createPending} = useCreateNoteRepository()
  const {deleteNoteBy} = useDeleteNoteRepository()
  const {update, isPending: updatePending} = useUpdateNoteRepository()

  const {isOpen, onOpen, onClose} = useDisclosure()

  function onListSelectionChange(selection: ArchivationState) {
    setListSelection(selection)
  }

  function createOrUpdate(note: Note) {
    if (note.id === undefined) {
      create(note)
    } else {
      update(note)
    }

    refetch()
  }

  function deleteNote(id: string) {
    deleteNoteBy(id)
    refetch()
  }

  function onDetailClick(note: Note) {
    setSelectedNote(note)
  }

  function close() {
    setSelectedNote(undefined)
    onClose()
  }

  function updateArchivationState(note: Note) {
    update({
      ...note,
      archived: listSelection === 'active'
    })

    refetch()
  }

  return {
    data,
    isPending,
    listSelection,
    onListSelectionChange,
    createOrUpdate,
    createOrUpdatePending: createPending || updatePending,
    deleteNote,
    selectedNote,
    onDetailClick,
    isOpen,
    onOpen,
    close,
    updateArchivationState,
  }
}
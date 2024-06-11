import {useMutation, useQuery, useQueryClient} from "@tanstack/react-query"
import {AllNotes, Note} from "./model"

export function useNotesRepository(
  showArchived: boolean
) {
  const result = useQuery({
    queryKey: ['notes', showArchived], queryFn: async (): Promise<AllNotes> => {
      const response = await fetch(`${process.env.REACT_APP_API_BASE_URL}/notes?archived=${showArchived}`)
      return response.json()
    }
  })

  return {
    data: result.data,
    isPending: result.isPending,
    isError: result.isError,
    refetch: result.refetch
  }
}

export function useCreateNoteRepository() {
  const queryClient = useQueryClient()
  const mutation = useMutation({
    mutationFn: (newNote: Note) => {
      return fetch(
        `${process.env.REACT_APP_API_BASE_URL}/notes`, {
          method: 'POST',
          headers: new Headers({'content-type': 'application/json'}),
          body: JSON.stringify(newNote)
        })
    },
    onSuccess: async () => {
      await queryClient.invalidateQueries({queryKey: ['notes']})
    }
  })

  return {
    create: mutation.mutate,
    isPending: mutation.isPending,
    isError: mutation.isError,
    isSuccess: mutation.isSuccess,
  }
}

export function useDeleteNoteRepository() {
  const queryClient = useQueryClient()
  const mutation = useMutation({
    mutationFn: (id: string) => {
      return fetch(
        `${process.env.REACT_APP_API_BASE_URL}/notes/${id}`, {
          method: 'DELETE',
        })
    },
    onSuccess: async () => {
      await queryClient.invalidateQueries({queryKey: ['notes']})
    }
  })

  return {
    deleteNoteBy: mutation.mutate,
    isPending: mutation.isPending,
    isError: mutation.isError,
    isSuccess: mutation.isSuccess,
  }
}

export function useUpdateNoteRepository() {
  const queryClient = useQueryClient()
  const mutation = useMutation({
    mutationFn: (noteToUpdate: Note) => {
      return fetch(
        `${process.env.REACT_APP_API_BASE_URL}/notes/${noteToUpdate.id}`, {
          method: 'PUT',
          headers: new Headers({'content-type': 'application/json'}),
          body: JSON.stringify(noteToUpdate)
        })
    },
    onSuccess: async () => {
      await queryClient.invalidateQueries({queryKey: ['notes']})
    }
  })

  return {
    update: mutation.mutate,
    isPending: mutation.isPending,
    isError: mutation.isError,
    isSuccess: mutation.isSuccess,
  }
}
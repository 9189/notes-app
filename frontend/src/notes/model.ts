export type Note = {
  id?: string
  title: string,
  value: string,
  archived: boolean,

  createdAt?: string,
  updatedAt?: string,
}

export type AllNotes = {
  items: Note[]
}
import useNotes from "./use-notes";
import {
  Box,
  Text,
  Heading,
  StackDivider,
  VStack,
  Center,
  Skeleton,
  Stack,
  Spacer,
  Flex,
} from "@chakra-ui/react";
import NotesControl from "./NotesControl";
import React from "react";
import NoteDetail from "./note-detail/NoteDetail";
import {DeleteIcon, RepeatClockIcon, StarIcon} from "@chakra-ui/icons";

export default function NotesPage() {
  const {
    data,
    isPending,
    listSelection,
    onListSelectionChange,
    createOrUpdate,
    createOrUpdatePending,
    deleteNote,
    selectedNote,
    onDetailClick,
    isOpen,
    onOpen,
    close,
    updateArchivationState
  } = useNotes()

  return (
    <>
      <VStack divider={<StackDivider/>} spacing='4' align='stretch'>
        <Center>
          <NotesControl
            selected={listSelection}
            onSelectionChange={onListSelectionChange}
            onAddClicked={onOpen}
          />
        </Center>

        {isPending ? <NotesSkeleton/> :
          data?.items.map(note => (
              <Box key={note.id} _hover={{cursor: "pointer"}} onClick={() => {
                onDetailClick(note)
                onOpen()
              }}>
                <Flex>
                  <Box>
                    <Heading size='xs' textTransform='uppercase'>
                      {note.title}
                    </Heading>
                    <Text pt='2' fontSize='sm'>
                      {note.value}
                    </Text>
                  </Box>

                  <Spacer/>

                  {listSelection === 'active' ?
                    // TODO: extract icons to own components to avoid mismatch
                    <RepeatClockIcon mr='4' _hover={{color: "blue.500", cursor: "pointer"}} onClick={event => {
                      event.stopPropagation()
                      updateArchivationState(note)
                    }}/>
                    :
                    <StarIcon mr='4' _hover={{color: "blue.500", cursor: "pointer"}} onClick={event => {
                      event.stopPropagation()
                      updateArchivationState(note)
                    }}/>
                  }

                  <DeleteIcon
                    onClick={event => {
                      event.stopPropagation()
                      if (note.id !== undefined) deleteNote(note.id)
                    }}
                    color='grey'
                    _hover={{color: "red.500", cursor: "pointer"}}/>
                </Flex>
              </Box>
            )
          )
        }
      </VStack>

      <NoteDetail
        isOpen={isOpen}
        onClose={close}
        onSave={createOrUpdate}
        saving={createOrUpdatePending}
        note={selectedNote}
      />
    </>
  )
}

function NotesSkeleton() {
  return (
    <Stack divider={<StackDivider/>} spacing='4'>
      <Stack>
        <Skeleton height='20px'/>
        <Skeleton height='15px'/>
      </Stack>
      <Stack>
        <Skeleton height='20px'/>
        <Skeleton height='15px'/>
      </Stack>
      <Stack>
        <Skeleton height='20px'/>
        <Skeleton height='15px'/>
      </Stack>
    </Stack>
  )
}
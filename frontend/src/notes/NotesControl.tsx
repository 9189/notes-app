import {Button, ButtonGroup, Flex, IconButton, Spacer} from "@chakra-ui/react";
import {StarIcon, RepeatClockIcon, AddIcon} from "@chakra-ui/icons";

export type ArchivationState = 'active' | 'archived'

type NotesControlProps = {
  selected: ArchivationState
  onSelectionChange: (selection: ArchivationState) => void
  onAddClicked: () => void
}

export default function NotesControl(
  {selected, onSelectionChange, onAddClicked}: NotesControlProps
) {

  return (
    <Flex width='100%'>
      <Spacer/>

      <ButtonGroup size='sm' isAttached variant='outline'>
        <Button isActive={selected === 'active'}
                onClick={() => onSelectionChange('active')}
                aria-label='Active'
                leftIcon={<StarIcon/>}>
          Active
        </Button>
        <Button isActive={selected === 'archived'}
                onClick={() => onSelectionChange('archived')}
                aria-label='Archived' leftIcon={<RepeatClockIcon/>}>
          Archived
        </Button>
      </ButtonGroup>

      <Spacer/>

      <IconButton alignSelf='right' aria-label='Add' icon={<AddIcon/>} onClick={onAddClicked}/>
    </Flex>
  )
}
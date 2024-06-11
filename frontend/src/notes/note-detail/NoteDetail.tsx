import {Note} from "../model";
import {useFormik} from "formik";
import {
  Button,
  FormControl,
  FormLabel,
  Input,
  Modal, ModalBody, ModalCloseButton,
  ModalContent, ModalFooter,
  ModalHeader,
  ModalOverlay,
  Textarea
} from "@chakra-ui/react";
import React, {useEffect} from "react";

type NoteDetailProps = {
  note?: Note,
  isOpen: boolean,
  onClose: () => void,
  onSave: (note: Note) => void,
  saving: boolean,
}

export default function NoteDetail(
  {
    note,
    isOpen,
    onClose,
    onSave,
    saving,
  }: NoteDetailProps
) {

  const formik = useFormik({
    initialValues: {
      id: note?.id,
      title: "",
      value: "",
      archived: false,
    },
    onSubmit: note => {
      onSave(note)
      onClose()
    }
  })

  useEffect(() => {
    formik.setValues({
      id: note?.id,
      title: note?.title ?? "",
      value: note?.value ?? "",
      archived: note?.archived ?? false,
    })
  }, [note, isOpen])

  return (
    <>
      <Modal isOpen={isOpen} onClose={onClose}>
        <ModalOverlay/>
        <ModalContent>
          <ModalHeader>Modal Title</ModalHeader>
          <ModalCloseButton/>
          <form onSubmit={formik.handleSubmit}>
            <ModalBody>
              <FormControl>
                <FormLabel htmlFor="title">Title</FormLabel>
                <Input
                  id="title"
                  name="title"
                  type="text"
                  variant="filled"
                  onChange={formik.handleChange}
                  value={formik.values.title}
                />
              </FormControl>
              <FormControl>
                <FormLabel htmlFor="value">Note</FormLabel>
                <Textarea
                  id="value"
                  name="value"
                  variant="filled"
                  resize="vertical"
                  onChange={formik.handleChange}
                  value={formik.values.value}
                />
              </FormControl>

              <ModalFooter>
                <Button type='submit' colorScheme='blue' mr={3} isLoading={saving}>
                  Save
                </Button>
                <Button onClick={onClose}>Cancel</Button>
              </ModalFooter>
            </ModalBody>
          </form>

        </ModalContent>
      </Modal>
    </>
  )
}
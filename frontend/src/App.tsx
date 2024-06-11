import React from 'react'
import NotesPage from "./notes/NotesPage"
import {QueryClient, QueryClientProvider} from "@tanstack/react-query";
import {ChakraProvider, Container} from "@chakra-ui/react";

const queryClient = new QueryClient()

export default function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <ChakraProvider>
        <Container p="4">
          <NotesPage/>
        </Container>
      </ChakraProvider>
    </QueryClientProvider>
  )
}

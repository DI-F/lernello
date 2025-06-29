package ch.nova_omnia.lernello.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ch.nova_omnia.lernello.folder.model.Folder;
import ch.nova_omnia.lernello.folder.repository.FolderRepository;
import ch.nova_omnia.lernello.folder.service.FolderService;

@ExtendWith(MockitoExtension.class)
class FolderServiceTest {

    @Mock
    private FolderRepository folderRepository;

    @InjectMocks
    private FolderService service;

    /**
     * Test that findAll returns all folders from the repository
     */
    @Test
    void shouldFindAllFolders() {
        List<Folder> folders = List.of(new Folder(), new Folder());
        when(folderRepository.findAll()).thenReturn(folders);

        List<Folder> result = service.findAll();

        assertEquals(folders, result);
        verify(folderRepository).findAll();
    }

    /**
     * Test that findById returns a folder when present
     */
    @Test
    void shouldFindById() {
        UUID id = UUID.randomUUID();
        Folder folder = new Folder();
        when(folderRepository.findById(id)).thenReturn(Optional.of(folder));

        Optional<Folder> result = service.findById(id);

        assertTrue(result.isPresent());
        assertSame(folder, result.get());
        verify(folderRepository).findById(id);
    }

    /**
     * Test that findById returns empty when folder is not found
     */
    @Test
    void shouldReturnEmptyWhenFindByIdMissing() {
        UUID id = UUID.randomUUID();
        when(folderRepository.findById(id)).thenReturn(Optional.empty());

        Optional<Folder> result = service.findById(id);

        assertFalse(result.isPresent());
        verify(folderRepository).findById(id);
    }

    /**
     * Test that save delegates to the repository and returns the saved folder
     */
    @Test
    void shouldSaveFolder() {
        Folder folder = new Folder();
        when(folderRepository.save(folder)).thenReturn(folder);

        Folder result = service.save(folder);

        assertSame(folder, result);
        verify(folderRepository).save(folder);
    }

    /**
     * Test that deleteById delegates to the repository deleteById
     */
    @Test
    void shouldDeleteFolderById() {
        UUID id = UUID.randomUUID();

        service.deleteById(id);

        verify(folderRepository).deleteById(id);
    }
}


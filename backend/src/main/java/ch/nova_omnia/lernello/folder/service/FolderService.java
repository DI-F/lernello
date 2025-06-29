package ch.nova_omnia.lernello.folder.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.nova_omnia.lernello.folder.model.Folder;
import ch.nova_omnia.lernello.folder.repository.FolderRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FolderService {
    private final FolderRepository folderRepository;

    public List<Folder> findAll() {
        return folderRepository.findAll();
    }

    public Optional<Folder> findById(UUID id) {
        return folderRepository.findById(id);
    }

    @Transactional
    public Folder save(Folder folder) {
        return folderRepository.save(folder);
    }

    @Transactional
    public void deleteById(UUID id) {
        folderRepository.deleteById(id);
    }
}
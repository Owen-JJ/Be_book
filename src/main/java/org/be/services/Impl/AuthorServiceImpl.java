package org.be.services.Impl;

import org.be.dtos.AuthorDTO;
import org.be.dtos.common.ResponseDTO;
import org.be.entities.AuthorEntity;
import org.be.repositories.AuthorRepository;
import org.be.services.AuthorService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;
    private final ModelMapper modelMapper;

    public AuthorServiceImpl(AuthorRepository authorRepository, ModelMapper modelMapper) {
        this.authorRepository = authorRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public ResponseDTO<AuthorDTO> create(AuthorDTO authorDTO) {
        try {
            AuthorEntity authorEntity = modelMapper.map(authorDTO, AuthorEntity.class);
            AuthorEntity savedAuthor = authorRepository.save(authorEntity);
            AuthorDTO savedAuthorDTO = modelMapper.map(savedAuthor, AuthorDTO.class);

            return ResponseDTO.<AuthorDTO>builder()
                    .status(String.valueOf(HttpStatus.CREATED.value()))
                    .message("Tác giả đã được tạo thành công!")
                    .data(savedAuthorDTO)
                    .build();
        } catch (Exception e) {
            return ResponseDTO.<AuthorDTO>builder()
                    .status(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                    .message("Lỗi khi tạo tác giả!")
                    .build();
        }
    }

    @Override
    public ResponseDTO<List<AuthorDTO>> findAll() {
        List<AuthorEntity> authors = authorRepository.findAll();

        if (authors.isEmpty()) {
            return ResponseDTO.<List<AuthorDTO>>builder()
                    .status(String.valueOf(HttpStatus.NOT_FOUND.value()))
                    .message("Không tìm thấy danh sách tác giả!")
                    .build();
        }

        List<AuthorDTO> authorDTOs = authors.stream()
                .map(author -> modelMapper.map(author, AuthorDTO.class))
                .collect(Collectors.toList());

        return ResponseDTO.<List<AuthorDTO>>builder()
                .status(String.valueOf(HttpStatus.OK.value()))
                .message("Danh sách tác giả được lấy thành công!")
                .data(authorDTOs)
                .build();
    }

    @Override
    public ResponseDTO<AuthorDTO> findById(Long id) {
        Optional<AuthorEntity> optionalAuthor = authorRepository.findById(id);

        if (optionalAuthor.isPresent()) {
            AuthorDTO authorDTO = modelMapper.map(optionalAuthor.get(), AuthorDTO.class);
            return ResponseDTO.<AuthorDTO>builder()
                    .status(String.valueOf(HttpStatus.OK.value()))
                    .message("Tác giả được tìm thấy!")
                    .data(authorDTO)
                    .build();
        } else {
            return ResponseDTO.<AuthorDTO>builder()
                    .status(String.valueOf(HttpStatus.NOT_FOUND.value()))
                    .message("Không tìm thấy tác giả!")
                    .build();
        }
    }

    @Override
    public ResponseDTO<AuthorDTO> update(Long id, AuthorDTO authorDTO) {
        Optional<AuthorEntity> optionalAuthor = authorRepository.findById(id);

        if (optionalAuthor.isPresent()) {
            AuthorEntity existingAuthor = optionalAuthor.get();

            existingAuthor.setName(authorDTO.getName());
            existingAuthor.setNationality(authorDTO.getNationality());

            AuthorEntity updatedAuthor = authorRepository.save(existingAuthor);
            AuthorDTO updatedAuthorDTO = modelMapper.map(updatedAuthor, AuthorDTO.class);

            return ResponseDTO.<AuthorDTO>builder()
                    .status(String.valueOf(HttpStatus.OK.value()))
                    .message("Tác giả được cập nhật thành công!")
                    .data(updatedAuthorDTO)
                    .build();
        } else {
            return ResponseDTO.<AuthorDTO>builder()
                    .status(String.valueOf(HttpStatus.NOT_FOUND.value()))
                    .message("Không tìm thấy tác giả để cập nhật!")
                    .build();
        }
    }

    @Override
    public ResponseDTO<Void> delete(Long id) {
        Optional<AuthorEntity> optionalAuthor = authorRepository.findById(id);

        if (optionalAuthor.isPresent()) {
            authorRepository.deleteById(id);
            return ResponseDTO.<Void>builder()
                    .status(String.valueOf(HttpStatus.OK.value()))
                    .message("Tác giả đã được xoá thành công!")
                    .build();
        } else {
            return ResponseDTO.<Void>builder()
                    .status(String.valueOf(HttpStatus.NOT_FOUND.value()))
                    .message("Không tìm thấy tác giả để xoá!")
                    .build();
        }
    }
}

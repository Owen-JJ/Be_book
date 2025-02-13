package org.be.services;

import org.be.dtos.AuthorDTO;
import org.be.dtos.common.ResponseDTO;

import java.util.List;

public interface AuthorService {
    ResponseDTO<AuthorDTO> create(AuthorDTO authorDTO);
    ResponseDTO<List<AuthorDTO>> findAll();
    ResponseDTO<AuthorDTO> findById(Long id);
    ResponseDTO<AuthorDTO> update(Long id, AuthorDTO authorDTO);
    ResponseDTO<Void> delete(Long id);
}

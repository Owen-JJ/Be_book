package org.be.dtos.common;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ResponseDTO<T> {
    private String status;
    private String message;
    private T data;
}

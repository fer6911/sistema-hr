package com.atlashr.atlas_hr.infrastructure.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {

    private boolean error;
    private String message;
    private T data;
    private List<String> errors;

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(false, "Operacion realizada exitosamente", data, List.of());
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(false, message, data, List.of());
    }

    public static <T> ApiResponse<T> error(String message, List<String> errors) {
        return new ApiResponse<>(true, message, null, errors);
    }
}

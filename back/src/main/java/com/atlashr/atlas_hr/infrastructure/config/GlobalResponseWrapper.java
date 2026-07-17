package com.atlashr.atlas_hr.infrastructure.config;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalResponseWrapper implements ResponseBodyAdvice<Object> {

    private static final List<String> UNWRAPPED_PATHS = List.of("/check", "/api/check/ip");

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        Class<?> parameterType = returnType.getParameterType();
        return !parameterType.equals(ApiResponse.class) && !parameterType.equals(String.class);
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {

        String path = request.getURI().getPath();

        if (UNWRAPPED_PATHS.stream().anyMatch(path::startsWith) || path.contains("swagger")) {
            return body;
        }

        if (body == null) {
            return ApiResponse.success(null);
        }

        if (body instanceof ApiResponse) {
            return body;
        }

        return ApiResponse.success(body);
    }
}

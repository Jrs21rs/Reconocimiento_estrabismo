package com.Deteccion_estrabismo.backend.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class BuildObjectMapper {
    private final ObjectMapper objectMapper;

    public BuildObjectMapper() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public <T> T converterTo(Object source, Class<T> targetClass) {
        return objectMapper.convertValue(source, targetClass);
    }

    public String converterToString(Object reference) {
        try {
            return objectMapper.writeValueAsString(reference);
        } catch (Exception e) {
            return "";
        }
    }
}

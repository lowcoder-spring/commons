package icu.lowcoder.spring.commons.util.json;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

public class JacksonJsonProcessor implements JsonProcessor {

    private PropertyNamingStrategy defaultPropertyNamingStrategy = PropertyNamingStrategy.CAMEL_CASE;
    private ObjectMapper defaultObjectMapper;

    public JacksonJsonProcessor() {
        defaultObjectMapper = buildObjectMapper(defaultPropertyNamingStrategy);
    }

    public void setDefaultPropertyNamingStrategy(PropertyNamingStrategy defaultPropertyNamingStrategy) {
        this.defaultPropertyNamingStrategy = defaultPropertyNamingStrategy;
        defaultObjectMapper = buildObjectMapper(defaultPropertyNamingStrategy);
    }

    @Override
    public String serialize(Object javaObject) {
        return write(defaultObjectMapper, javaObject);
    }

    @Override
    public String serialize(Object javaObject, PropertyNamingStrategy propertyNamingStrategy) {
        ObjectMapper objectMapper;
        if (propertyNamingStrategy.equals(defaultPropertyNamingStrategy)) {
            objectMapper = defaultObjectMapper;
        } else {
            objectMapper = buildObjectMapper(propertyNamingStrategy);
        }

        return write(objectMapper, javaObject);
    }

    @SneakyThrows
    private String write(ObjectMapper objectMapper, Object javaObject) {
        return objectMapper.writeValueAsString(javaObject);
    }

    @Override
    public <T> T deserialize(String json, Class<T> type) {
        return read(defaultObjectMapper, json, type);
    }

    @Override
    public <T> T deserialize(String json, Class<T> type, PropertyNamingStrategy propertyNamingStrategy) {
        ObjectMapper objectMapper;
        if (propertyNamingStrategy.equals(defaultPropertyNamingStrategy)) {
            objectMapper = defaultObjectMapper;
        } else {
            objectMapper = buildObjectMapper(propertyNamingStrategy);
        }

        return read(objectMapper, json, type);
    }

    @SneakyThrows
    private <T> T read(ObjectMapper objectMapper, String json, Class<T> type) {
        return objectMapper.readValue(json, type);
    }

    private ObjectMapper buildObjectMapper(PropertyNamingStrategy propertyNamingStrategy) {
        ObjectMapper objectMapper = new ObjectMapper();

        objectMapper.setPropertyNamingStrategy(convertPropertyNamingStrategy(propertyNamingStrategy));
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper;
    }

    private com.fasterxml.jackson.databind.PropertyNamingStrategy convertPropertyNamingStrategy(PropertyNamingStrategy propertyNamingStrategy) {
        com.fasterxml.jackson.databind.PropertyNamingStrategy jacksonPropertyNamingStrategy;

        switch (propertyNamingStrategy) {
            case PASCAL_CASE:
                jacksonPropertyNamingStrategy = com.fasterxml.jackson.databind.PropertyNamingStrategy.UPPER_CAMEL_CASE;
                break;
            case KEBAB_CASE:
                jacksonPropertyNamingStrategy = com.fasterxml.jackson.databind.PropertyNamingStrategy.KEBAB_CASE;
                break;
            case SNAKE_CASE:
                jacksonPropertyNamingStrategy = com.fasterxml.jackson.databind.PropertyNamingStrategy.SNAKE_CASE;
                break;
            case CAMEL_CASE:
            default:
                jacksonPropertyNamingStrategy = com.fasterxml.jackson.databind.PropertyNamingStrategy.LOWER_CAMEL_CASE;
        }

        return jacksonPropertyNamingStrategy;
    }
}

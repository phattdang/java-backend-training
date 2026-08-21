package com.example.P07_JsonDataBinding.controller;

import com.example.P07_JsonDataBinding.dto.CreateUserRequest;
import com.example.P07_JsonDataBinding.dto.ManualJsonDemoResponse;
import com.example.P07_JsonDataBinding.dto.MappingErrorRequest;
import com.example.P07_JsonDataBinding.dto.UserResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping(value = "/api/json", produces = MediaType.APPLICATION_JSON_VALUE)
public class JsonUserController {
    private final AtomicLong nextId = new AtomicLong(1);
    private final ObjectMapper objectMapper;

    public JsonUserController(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    // Deserialization: @RequestBody dung Jackson de map JSON request thanh CreateUserRequest record.
    // Serialization: return UserResponse truc tiep, Spring/Jackson tu map Java object thanh JSON response.
    @PostMapping(value = "/users", consumes = MediaType.APPLICATION_JSON_VALUE)
    public UserResponse createUser(@RequestBody CreateUserRequest request) {
        return new UserResponse(
                nextId.getAndIncrement(),
                request.fullName(),
                request.email(),
                request.birthday(),
                LocalDateTime.now(),
                "This internal note exists in Java but is hidden by @JsonIgnore"
        );
    }

    // ObjectMapper manual demo: writeValueAsString = Java object -> JSON, readValue = JSON -> Java object.
    @PostMapping(value = "/manual", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ManualJsonDemoResponse manualObjectMapperDemo(@RequestBody CreateUserRequest request) throws JacksonException {
        UserResponse responseObject = new UserResponse(
                99L,
                request.fullName(),
                request.email(),
                request.birthday(),
                LocalDateTime.of(2026, 8, 21, 10, 0),
                "Hidden note from manual ObjectMapper demo"
        );

        String javaObjectToJson = objectMapper.writeValueAsString(responseObject);

        String rawJson = """
                {
                  "full_name": "Manual Jackson",
                  "email": "manual@example.com",
                  "birthday": "2001-01-15"
                }
                """;
        CreateUserRequest jsonToJavaObject = objectMapper.readValue(rawJson, CreateUserRequest.class);

        return new ManualJsonDemoResponse(javaObjectToJson, jsonToJavaObject);
    }

    // Mapping error demo: gui id la string khong hop le hoac birthday sai format de thay Jackson parse fail.
    @PostMapping(value = "/mapping-errors", consumes = MediaType.APPLICATION_JSON_VALUE)
    public MappingErrorRequest mappingErrorDemo(@RequestBody MappingErrorRequest request) {
        return request;
    }
}

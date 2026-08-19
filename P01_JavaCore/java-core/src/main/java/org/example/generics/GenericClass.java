package org.example.generics;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class GenericClass {
    public static void main(String[] args) {
        ApiResponse<User> response = ApiResponse.success(new User(1L, "Alice"));
        ApiResponse<String> error = ApiResponse.error("User not found");

        Repository<User, Long> userRepository = new Repository<>();
        userRepository.save(1L, new User(1L, "Alice"));
        userRepository.save(2L, new User(2L, "Bob"));

        System.out.println("ApiResponse<User>: " + response);
        System.out.println("ApiResponse<String>: " + error);
        System.out.println("Repository<T, ID> find 1L: " + userRepository.findById(1L));
    }

    record User(Long id, String name) {
    }

    static class ApiResponse<T> {
        private final boolean success;
        private final T data;
        private final String message;

        private ApiResponse(boolean success, T data, String message) {
            this.success = success;
            this.data = data;
            this.message = message;
        }

        static <T> ApiResponse<T> success(T data) {
            return new ApiResponse<>(true, data, "OK");
        }

        static <T> ApiResponse<T> error(String message) {
            return new ApiResponse<>(false, null, message);
        }

        @Override
        public String toString() {
            return "ApiResponse{success=%s, data=%s, message='%s'}"
                    .formatted(success, data, message);
        }
    }

    static class Repository<T, ID> {
        private final Map<ID, T> storage = new HashMap<>();

        void save(ID id, T entity) {
            storage.put(id, entity);
        }

        Optional<T> findById(ID id) {
            return Optional.ofNullable(storage.get(id));
        }
    }
}

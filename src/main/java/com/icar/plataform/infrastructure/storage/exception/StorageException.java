// Caminho: src/main/java/com/icar/plataform/infrastructure/storage/StorageException.java
package com.icar.plataform.infrastructure.storage.exception;

public class StorageException extends RuntimeException {
    public StorageException(String message) {
        super(message);
    }

    public StorageException(String message, Throwable cause) {
        super(message, cause);
    }
}

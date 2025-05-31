// Caminho: src/main/java/com/icar/plataform/infrastructure/storage/StorageFileNotFoundException.java
package com.icar.plataform.infrastructure.storage.exception;

import com.icar.plataform.infrastructure.storage.exception.StorageException;

public class StorageFileNotFoundException extends StorageException {
    public StorageFileNotFoundException(String message) {
        super(message);
    }

    public StorageFileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

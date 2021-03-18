package br.com.zup.bootcamp.ecommerce.product;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Optional;

import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

class UploadedFile {

    private final MultipartFile file;

    UploadedFile(MultipartFile file) {
        this.file = file;
    }

    String getName() {
        return file.getName();
    }

    Optional<ImageFile> toJpg() {
        boolean isJpg = MediaType.IMAGE_JPEG_VALUE.equalsIgnoreCase(file.getContentType());
        return isJpg ? Optional.of(new ImageFile(file)) : Optional.empty();
    }
}

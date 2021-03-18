package br.com.zup.bootcamp.ecommerce.product;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.web.multipart.MultipartFile;

class ImageFile {

    private final MultipartFile file;

    ImageFile(MultipartFile file) {
        this.file = file;
    }

    String getName() {
        return file.getName();
    }

    URL writeTo(Path dest) throws IOException {
        file.transferTo(dest);
        return dest.toUri().toURL();
    }

}

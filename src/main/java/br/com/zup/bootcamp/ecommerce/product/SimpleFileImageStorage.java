package br.com.zup.bootcamp.ecommerce.product;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Collection;

import org.springframework.stereotype.Component;

import static java.util.stream.Collectors.toList;

@Component
class SimpleFileImageStorage implements ImageStorage {

    @Override
    public Collection<URL> upload(Collection<ImageFile> files) {
        return files.stream()
                .map(file -> {
                    try {
                        return file.writeTo(Files.createTempFile(file.getName(), ".jpg"));
                    } catch (IOException e) {
                        throw new UncheckedIOException(e);
                    }
                })
                .collect(toList());
    }
}

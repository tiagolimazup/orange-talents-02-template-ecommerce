package br.com.zup.bootcamp.ecommerce.product;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestReporter;
import org.springframework.mock.web.MockMultipartFile;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;

class SimpleFileImageStorageTest {

    @Test
    void createTempFiles(TestReporter reporter) throws IOException {
        SimpleFileImageStorage imageStorage = new SimpleFileImageStorage();

        Collection<URL> files = imageStorage.upload(List.of(new ImageFile(new MockMultipartFile("image", getClass().getResourceAsStream("iphone.jpg")))));

        reporter.publishEntry("files", files.toString());

        String tempDir = System.getProperty("java.io.tmpdir");

        assertThat(files.stream().map(URL::toString).collect(toList()), contains(startsWith("file:" + tempDir)));
    }

}
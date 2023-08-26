package org.vicary.service.downloader;

import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.vicary.model.FileRequest;
import org.vicary.model.FileResponse;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

public interface Downloader {
    FileResponse download(FileRequest request) throws IllegalArgumentException, NoSuchElementException, IOException;

    List<String> getAvailableExtensions();
}

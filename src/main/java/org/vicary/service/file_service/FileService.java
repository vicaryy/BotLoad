package org.vicary.service.file_service;

import org.vicary.model.FileResponse;

public interface FileService {
    void saveInRepo(FileResponse response);
    boolean existsInRepo(FileResponse response);
}

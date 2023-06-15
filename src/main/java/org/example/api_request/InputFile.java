package org.example.api_request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.File;
@Data
@AllArgsConstructor
public class InputFile {

    private String fileName;
    private File file;

    //attach://
}

package org.example.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.example.api_request.InputFile;

import java.io.IOException;

public class InputFileSerializer extends JsonSerializer<InputFile> {
    @Override
    public void serialize(InputFile inputFile, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
    }
}

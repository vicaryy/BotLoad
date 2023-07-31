package org.vicary.api_request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.vicary.api_object.File;
import org.vicary.end_point.EndPoint;

@Data
@RequiredArgsConstructor
@Builder
public class GetFile implements ApiRequest<File> {
    /**
     * Use this method to get basic information about a file and prepare it for downloading.
     * For the moment, bots can download files of up to 20MB in size.
     * On success, a File object is returned. The file can then be downloaded via the link
     * https://api.telegram.org/file/bot<token>/<file_path>, where <file_path> is taken from the response.
     * It is guaranteed that the link will be valid for at least 1 hour. When the link expires,
     * a new one can be requested by calling getFile again.
     *
     * @param fileId File identifier to get information about.
     */

    @NonNull
    @JsonProperty("file_id")
    private String fileId;

    @Override
    public File getReturnObject() {
        return new File();
    }

    @Override
    public String getEndPoint() {
        return EndPoint.GET_FILE.getPath();
    }

    @Override
    public void checkValidation() {
        if (fileId.isEmpty()) throw new IllegalArgumentException("fileId cannot be empty.");
    }
}

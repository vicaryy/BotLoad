package org.vicary.service.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.vicary.model.FileInfo;
import org.vicary.model.FileResponse;
import org.vicary.service.mapper.FileInfoMapper;

import static org.junit.jupiter.api.Assertions.*;

class FileInfoMapperTest {
    private FileInfoMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new FileInfoMapper();
    }

    @Test
    void map_expectEquals_ValidFileInfo() {
        //given
        String givenId = "example_id";
        String givenURL = "www.something.com";
        String givenTitle = "title";
        String givenUploaderURL = "something.com/";
        String givenServiceName = "youtube";
        double givenDuration = 10;
        FileInfo givenFileInfo = FileInfo.builder()
                .id(givenId)
                .URL(givenURL)
                .title(givenTitle)
                .duration(givenDuration)
                .uploaderURL(givenUploaderURL)
                .build();

        FileResponse expectedFileResponse = FileResponse.builder()
                .serviceId(givenId)
                .URL(givenURL)
                .title(givenTitle)
                .duration((int) givenDuration)
                .build();

        //when
        FileResponse actualFileResponse = mapper.map(givenFileInfo, givenServiceName);

        //then
        Assertions.assertEquals(expectedFileResponse, actualFileResponse);
    }


    @Test
    void map_expectEquals_TitleIsNull() {
        //given
        String givenId = "example_id";
        String givenURL = "www.something.com";
        String givenTitle = null;
        String givenUploaderURL = "something.com/";
        String givenServiceName = "youtube";
        double givenDuration = 10;
        FileInfo givenFileInfo = FileInfo.builder()
                .id(givenId)
                .URL(givenURL)
                .title(givenTitle)
                .duration(givenDuration)
                .uploaderURL(givenUploaderURL)
                .build();

        FileResponse expectedFileResponse = FileResponse.builder()
                .serviceId(givenId)
                .URL(givenURL)
                .title("title")
                .duration((int) givenDuration)
                .build();

        //when
        FileResponse actualFileResponse = mapper.map(givenFileInfo, givenServiceName);

        //then
        Assertions.assertEquals(expectedFileResponse, actualFileResponse);
    }


    @Test
    void map_expectEquals_TitleIsEmpty() {
        //given
        String givenId = "example_id";
        String givenURL = "www.something.com";
        String givenTitle = "";
        String givenUploaderURL = "something.com/";
        String givenServiceName = "youtube";
        double givenDuration = 10;
        FileInfo givenFileInfo = FileInfo.builder()
                .id(givenId)
                .URL(givenURL)
                .title(givenTitle)
                .duration(givenDuration)
                .uploaderURL(givenUploaderURL)
                .build();

        FileResponse expectedFileResponse = FileResponse.builder()
                .serviceId(givenId)
                .URL(givenURL)
                .title("title")
                .duration((int) givenDuration)
                .build();

        //when
        FileResponse actualFileResponse = mapper.map(givenFileInfo, givenServiceName);

        //then
        Assertions.assertEquals(expectedFileResponse, actualFileResponse);
    }


    @Test
    void map_expectThrowsNullPointerEx_FileInfoIsNull() {
        //given
        FileInfo givenFileInfo = null;
        String givenServiceName = "youtube";

        //when
        //then
        Assertions.assertThrows(NullPointerException.class, () -> mapper.map(givenFileInfo, givenServiceName));
    }
}








































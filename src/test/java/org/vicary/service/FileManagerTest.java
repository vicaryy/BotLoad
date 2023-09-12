package org.vicary.service;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junit.runner.Runner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(Runner.class)
@SpringBootTest
class FileManagerTest {
    @Autowired
    private FileManager fileManager;

    @MockBean
    private Converter converter;


    @Test
    void isFileSizeValid_expectTrue_fileSizeBelow50MB() {
        //given
        long givenFileSize = 20L;

        //when
        //then
        assertTrue(fileManager.isFileSizeValid(givenFileSize));
    }


    @Test
    void isFileSizeValid_expectTrue_fileSizeIsZero() {
        //given
        long givenFileSize = 0L;

        //when
        //then
        assertTrue(fileManager.isFileSizeValid(givenFileSize));
    }

    @Test
    void isFileSizeValid_expectTrue_fileSizeNegativeNumber() {
        //given
        long givenFileSize = -20L;

        //when
        //then
        assertTrue(fileManager.isFileSizeValid(givenFileSize));
    }

    @Test
    void isFileSizeValid_expectTrue_fileSizeOver50MB() {
        //given
        long givenFileSize = 57_344_000L;   // 55MB

        //when
        //then
        assertFalse(fileManager.isFileSizeValid(givenFileSize));
    }

    @Test
    void getFileNameFromTitle_expectEquals_NormalTitleAndExtension() {
        //given
        String givenTitle = "title";
        String givenExtension = "mp3";

        String expectedFileName = "title.mp3";

        //when
        String actualFileName = fileManager.getFileNameFromTitle(givenTitle, givenExtension);

        //then
        assertEquals(expectedFileName, actualFileName);
    }


    @Test
    void getFileNameFromTitle_expectEquals_TitleOverMaxLimit59Chars() {
        //given
        String givenTitle = "title1 title2 title3 title4 title5 title6 title7 title8 title9 title10 title11 title12";
        String givenExtension = "mp3";

        String expectedFileName = "title1 title2 title3 title4 title5 title6 title7 title8 tit.mp3";

        //when
        String actualFileName = fileManager.getFileNameFromTitle(givenTitle, givenExtension);

        //then
        assertEquals(expectedFileName, actualFileName);
    }

    @Test
    void getFileNameFromTitle_expectEquals_TitleWithNotAllowedCharsFirstCombination() {
        //given
        String givenTitle = "title & ⧸⧹";
        String givenExtension = "mp3";

        String expectedFileName = "title and and.mp3";

        //when
        String actualFileName = fileManager.getFileNameFromTitle(givenTitle, givenExtension);

        //then
        assertEquals(expectedFileName, actualFileName);
    }

    @Test
    void getFileNameFromTitle_expectEquals_TitleWithNotAllowedCharsSecondCombination() {
        //given
        String givenTitle = "title / ⧸ | ｜ – \\ \\ :";
        String givenExtension = "mp3";

        String expectedFileName = "title - - - - - - - -.mp3";

        //when
        String actualFileName = fileManager.getFileNameFromTitle(givenTitle, givenExtension);

        //then
        assertEquals(expectedFileName, actualFileName);
    }


    @Test
    void getFileNameFromTitle_expectEquals_TitleOverMaxLimit59CharsWithNotAllowedChars() {
        //given
        String givenTitle = "title& title& title& title& title& title: title: title| title: title& title& title&";
        String givenExtension = "mp3";

        String expectedFileName = "titleand titleand titleand titleand titleand title- title- .mp3";

        //when
        String actualFileName = fileManager.getFileNameFromTitle(givenTitle, givenExtension);

        //then
        assertEquals(expectedFileName, actualFileName);
    }


    @Test
    void getFileNameFromTitle_expectThrowsNullPointerEx_TitleNull() {
        //given
        String givenTitle = null;
        String givenExtension = "mp3";

        //when
        //then
        assertThrows(NullPointerException.class, () -> fileManager.getFileNameFromTitle(givenTitle, givenExtension));
    }


    @Test
    void getFileSizeInProcess_expectEquals_ValidLine() {
        //given
        Long givenBytes = 100000L;
        String givenLine = String.format("[download] File is larger than max-filesize(%d bytes....", givenBytes);

        String expectedFileSize = "10.00MB";

        //when
        when(converter.bytesToMB(givenBytes)).thenReturn("10.00MB");

        String actualFileSize = fileManager.getFileSizeInProcess(givenLine);

        //then
        assertEquals(expectedFileSize, actualFileSize);
        verify(converter).bytesToMB(givenBytes);
    }


    @Test
    void getFileSizeInProcess_expectEquals_StringInsteadOfLong() {
        //given
        String givenLine = "[download] File is larger than max-filesize(SOMETHING OTHER)";

        //when
        //then
        assertThrows(NumberFormatException.class, () -> fileManager.getFileSizeInProcess(givenLine));
    }


    @Test
    void getFileSizeInProcess_expectEquals_NegativeBytes() {
        //given
        Long givenBytes = -100000L;
        String givenLine = String.format("[download] File is larger than max-filesize(%d bytes....", givenBytes);

        String expectedFileSize = "-10.00MB";

        //when
        when(converter.bytesToMB(givenBytes)).thenReturn("-10.00MB");

        String actualFileSize = fileManager.getFileSizeInProcess(givenLine);

        //then
        assertEquals(expectedFileSize, actualFileSize);
        verify(converter).bytesToMB(givenBytes);
    }


    @Test
    void getFileSizeInProcess_expectEquals_ValidLineSecondCombination() {
        //given
        String givenLine = "[download] Downloading file: 0.00% of 10.00MiB";

        String expectedFileSize = "10.00MiB";

        //when
        String actualFileSize = fileManager.getFileSizeInProcess(givenLine);

        //then
        assertEquals(expectedFileSize, actualFileSize);
    }


    @Test
    void getFileSizeInProcess_expectEquals_KiBInsteadOfMiBSecondCombination() {
        //given
        String givenLine = "[download] Downloading file: 0.00% of 10.00KiB";

        String expectedFileSize = "10.00KiB";

        //when
        String actualFileSize = fileManager.getFileSizeInProcess(givenLine);

        //then
        assertEquals(expectedFileSize, actualFileSize);
    }


    @Test
    void getFileSizeInProcess_expectEquals_NoBytesAndMiBInLine() {
        //given
        String givenLine = "[download] Downloading file: 0.00% of NOTHING";

        String expectedFileSize = "";

        //when
        String actualFileSize = fileManager.getFileSizeInProcess(givenLine);

        //then
        assertEquals(expectedFileSize, actualFileSize);
    }


    @Test
    void getFileSizeInProcess_expectEquals_EmptyLine() {
        //given
        String givenLine = "";

        String expectedFileSize = "";

        //when
        String actualFileSize = fileManager.getFileSizeInProcess(givenLine);

        //then
        assertEquals(expectedFileSize, actualFileSize);
    }


    @Test
    void getFileSizeInProcess_expectEquals_GiBInsteadOfMiBSecondCombination() {
        //given
        String givenLine = "[download] Downloading file: 0.00% of 10.00GiB";

        String expectedFileSize = "";

        //when
        String actualFileSize = fileManager.getFileSizeInProcess(givenLine);

        //then
        assertEquals(expectedFileSize, actualFileSize);
    }


    @Test
    void isFileSizeValidInProcess_expectTrue_ValidLine() {
        //given
        String givenLine = "[download] Downloading file: 0.00% of 10.00MiB";

        //when
        //then
        assertTrue(fileManager.isFileSizeValidInProcess(givenLine));
    }

    @Test
    void isFileSizeValidInProcess_expectFalse_SizeOver49MiB() {
        //given
        String givenLine = "[download] Downloading file: 0.00% of 60.55MiB";

        //when
        //then
        assertFalse(fileManager.isFileSizeValidInProcess(givenLine));
    }


    @Test
    void isFileSizeValidInProcess_expectFalse_SizeOver49MiBSecondCombination() {
        //given
        Long givenBytes = 100000L;
        String givenLine = String.format("[download] File is larger than max-filesize(%d bytes....", givenBytes);

        //when
        //then
        assertFalse(fileManager.isFileSizeValidInProcess(givenLine));
    }


    @Test
    void isFileSizeValidInProcess_expectTrue_SizeIsInKiB() {
        //given
        String givenLine = "[download] Downloading file: 0.00% of 60.55KiB";

        //when
        //then
        assertTrue(fileManager.isFileSizeValidInProcess(givenLine));
    }


    @Test
    void isFileSizeValidInProcess_expectFalse_SizeIsInGiB() {
        //given
        String givenLine = "[download] Downloading file: 0.00% of 60.55GiB";

        //when
        //then
        assertTrue(fileManager.isFileSizeValidInProcess(givenLine));
    }


    @Test
    void isFileSizeValidInProcess_expectFalse_InvalidLine() {
        //given
        String givenLine = "[download] SOMETHING WEIRD";

        //when
        //then
        assertTrue(fileManager.isFileSizeValidInProcess(givenLine));
    }


    @Test
    void isFileSizeValidInProcess_expectFalse_EmptyLine() {
        //given
        String givenLine = "";

        //when
        //then
        assertTrue(fileManager.isFileSizeValidInProcess(givenLine));
    }


    @Test
    void isFileSizeValidInProcess_expectTrue_NegativeMiB() {
        //given
        String givenLine = "[download] Downloading file: 0.00% of -20.55MiB";

        //when
        //then
        assertTrue(fileManager.isFileSizeValidInProcess(givenLine));
    }


    @Test
    void getDownloadFileProgressInProcessInMarkdownV2_expectEquals_ValidLine(){
        //given
        String givenLine = "[download] Downloading file: 50.35% of -20.55MiB";

        String expectedFileProgress = "50\\.35%";

        //when
        String actualFileProgress = fileManager.getDownloadProgressInProcess(givenLine);

        //then
        assertEquals(expectedFileProgress, actualFileProgress);
    }


    @Test
    void getDownloadFileProgressInProcessInMarkdownV2_expectEquals_InvalidLine(){
        //given
        String givenLine = "[download] Downloading file: 50?35Percent of -20.55MiB";

        String expectedFileProgress = null;

        //when
        String actualFileProgress = fileManager.getDownloadProgressInProcess(givenLine);

        //then
        assertEquals(expectedFileProgress, actualFileProgress);
    }


    @Test
    void getDownloadFileProgressInProcessInMarkdownV2_expectEquals_WeirdPercents(){
        //given
        String givenLine = "[download] Downloading file: 50?lol35% of -20.55MiB";

        String expectedFileProgress = "50?lol35%";

        //when
        String actualFileProgress = fileManager.getDownloadProgressInProcess(givenLine);

        //then
        assertEquals(expectedFileProgress, actualFileProgress);
    }


    @Test
    void getDownloadFileProgressInProcessInMarkdownV2_expectEquals_EmptyLine(){
        //given
        String givenLine = "";

        String expectedFileProgress = null;

        //when
        String actualFileProgress = fileManager.getDownloadProgressInProcess(givenLine);

        //then
        assertEquals(expectedFileProgress, actualFileProgress);
    }
}





























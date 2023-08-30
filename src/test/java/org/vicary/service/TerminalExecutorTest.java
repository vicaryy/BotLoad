package org.vicary.service;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junit.runner.Runner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(Runner.class)
@SpringBootTest
class TerminalExecutorTest {
    @Autowired
    TerminalExecutor terminalExecutor;


    @Test
    void removeFile_expectTrue_ValidFile() throws IOException {
        // given
        File fileToRemove = new File("/Users/vicary/desktop/folder/test.txt");
        fileToRemove.createNewFile();

        // when
        // then
        assertTrue(terminalExecutor.removeFile(fileToRemove));
    }

    @Test
    void removeFile_expectFalse_FileDoesNotExist() {
        // given
        File fileToRemove = new File("/Users/vicary/desktop/folder/test1.txt");

        // when
        // then
        assertFalse(terminalExecutor.removeFile(fileToRemove));
    }

    @Test
    void removeFile_expectFalse_FileIsNull() {
        // given
        File fileToRemove = null;

        // when
        // then
        assertFalse(terminalExecutor.removeFile(fileToRemove));
    }

}






















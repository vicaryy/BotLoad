package org.vicary.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junit.runner.Runner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.vicary.service.TerminalExecutor;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
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
        Assertions.assertTrue(terminalExecutor.removeFile(fileToRemove));
    }

    @Test
    void removeFile_expectFalse_FileDoesNotExist() {
        // given
        File fileToRemove = new File("/Users/vicary/desktop/folder/test1.txt");

        // when
        // then
        Assertions.assertFalse(terminalExecutor.removeFile(fileToRemove));
    }

    @Test
    void removeFile_expectFalse_FileIsNull() {
        // given
        File fileToRemove = null;

        // when
        // then
        Assertions.assertFalse(terminalExecutor.removeFile(fileToRemove));
    }

}






















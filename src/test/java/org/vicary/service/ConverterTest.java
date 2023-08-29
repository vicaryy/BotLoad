package org.vicary.service;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junit.runner.Runner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@RunWith(Runner.class)
@SpringBootTest
class ConverterTest {
    @Autowired
    private Converter converter;

    @Test
    void bytesToMB_expectEquals_ValidBytes() {
        //given
        long bytes = 2450000;
        String expectedMB = "2,34MB";

        //then
        assertEquals(expectedMB, converter.bytesToMB(bytes));
    }

    @Test
    void bytesToMB_expectEquals_ZeroBytes() {
        //given
        long bytes = 0;
        String expectedMB = "0,00MB";
        //then
        assertEquals(expectedMB, converter.bytesToMB(bytes));
    }

    @Test
    void bytesToMB_expectEquals_NegativeBytes() {
        //given
        long bytes = -2450000;
        String expectedMB = "-2,34MB";

        //then
        assertEquals(expectedMB, converter.bytesToMB(bytes));
    }

    @Test
    void bytesToMB_expectEquals_GiganticLongBytes() {
        //given
        long bytes = 9223372036854775800L;
        String expectedMB = "8796093022208,00MB";

        //then
        assertEquals(expectedMB, converter.bytesToMB(bytes));
    }

    @Test
    void MBToBytes_expectEquals_ValidMB() {
        String MB = "2,34MB";
        long expectedBytes = 2453667;

        //then
        assertEquals(expectedBytes, converter.MBToBytes(MB));
    }

    @Test
    void MBToBytes_expectEquals_NullMB() {
        String MB = null;
        long expectedBytes = 0;

        //then
        assertEquals(expectedBytes, converter.MBToBytes(MB));
    }

    @Test
    void MBToBytes_expectEquals_EmptyMB() {
        String MB = "";
        long expectedBytes = 0;

        //then
        assertEquals(expectedBytes, converter.MBToBytes(MB));
    }

    @Test
    void MBToBytes_expectEquals_NegativeMB() {
        String MB = "-2,34MB";
        long expectedBytes = -2453667;

        //then
        assertEquals(expectedBytes, converter.MBToBytes(MB));
    }

    @Test
    void MBToBytes_expectEquals_ZeroMB() {
        String MB = "0,00MB";
        long expectedBytes = 0;

        //then
        assertEquals(expectedBytes, converter.MBToBytes(MB));
    }

    @Test
    void MBToBytes_expectEquals_MBWithoutMB() {
        String MB = "2,34";
        long expectedBytes = 2453667;

        //then
        assertEquals(expectedBytes, converter.MBToBytes(MB));
    }

    @Test
    void MBToBytes_expectEquals_MBDotInsteadOfComma() {
        String MB = "2.34MB";
        long expectedBytes = 2453667;

        //then
        assertEquals(expectedBytes, converter.MBToBytes(MB));
    }

    @Test
    void MBToBytes_expectThrowNumberFormatExc_MBWithTwoCommas() {
        String MB = "2,3,4MB";

        //then
        assertThrows(NumberFormatException.class, () -> converter.MBToBytes(MB));
    }

    @Test
    void MBToBytes_expectThrowNumberFormatExc_NoMB() {
        String MB = "something";

        //then
        assertThrows(NumberFormatException.class, () -> converter.MBToBytes(MB));
    }

    @Test
    void secondsToMinutes_expectEquals_ValidSeconds() {
        //given
        int seconds = 128;
        String expectedMinutes = "2:08";

        //then
        assertEquals(expectedMinutes, converter.secondsToMinutes(seconds));
    }

    @Test
    void secondsToMinutes_expectEquals_BigSeconds() {
        //given
        int seconds = 1200292999;
        String expectedMinutes = "20004883:19";

        //then
        assertEquals(expectedMinutes, converter.secondsToMinutes(seconds));
    }

    @Test
    void secondsToMinutes_expectEquals_ZeroSeconds() {
        //given
        int seconds = 0;
        String expectedMinutes = "0:00";

        //then
        assertEquals(expectedMinutes, converter.secondsToMinutes(seconds));
    }

    @Test
    void secondsToMinutes_expectEquals_NegativeSeconds() {
        //given
        int seconds = -128;
        String expectedMinutes = "-2:08";

        //then
        assertEquals(expectedMinutes, converter.secondsToMinutes(seconds));
    }

    @Test
    void secondsToMinutes_expectEquals_NegativeSmallSeconds() {
        //given
        int seconds = -8;
        String expectedMinutes = "-0:08";

        //then
        assertEquals(expectedMinutes, converter.secondsToMinutes(seconds));
    }
}



























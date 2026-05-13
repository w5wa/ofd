package org.ofdrw.sign;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * signature ID解析器
 * @author Quan Guanyu
 * @since 2020-10-13 19:39:56
 */
class SignIdParserTest {

    @Test
    void parseIndex() {
        int id = SignIdParser.parseIndex("s776");
        assertEquals(776, id);
    }
}
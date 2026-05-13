package org.ofdrw.reader.tools;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 清理已经存在的namespace
 *
 * @author Quan Guanyu
 * @since 2020-10-15 19:39:20
 */
class NameSpaceCleanerTest {

    @Test
    void visit() throws DocumentException {
        String src = "src/test/resources/namespace_case.xml";
        SAXReader reader = new SAXReader();
        Document document = reader.read(src);
        // 清空root node上已经存在的namespace
        document.accept(new NameSpaceCleaner());

        System.out.println(document.asXML());

    }
}
package org.ofdrw.converter.ofdconverter;

import org.ofdrw.converter.GeneralConvertException;
import org.ofdrw.layout.OFDDoc;
import org.ofdrw.layout.PageLayout;
import org.ofdrw.layout.element.Paragraph;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * 文本转换为OFD
 *
 * @author Quan Guanyu
 * @since 2023-3-14 23:09:08
 */
public class TextConverter implements DocConverter {

    /**
     * OFD document object
     */
    final OFDDoc ofdDoc;


    /**
     * font size
     */
    double fontSize = 3;

    /**
     * whether the document has been closed
     */
    private boolean closed = false;


    /**
     * create PDF-to-OFD converter
     *
     * @param ofdPath path to converted OFD file
     * @throws IOException file parsing exception
     */
    public TextConverter(Path ofdPath) throws IOException {
        if (ofdPath == null) {
            throw new IllegalArgumentException("path to converted OFD file为空");
        }

        ofdPath = ofdPath.toAbsolutePath();
        if (!Files.exists(ofdPath)) {
            Path parent = ofdPath.getParent();
            if (Files.exists(parent)) {
                if (!Files.isDirectory(parent)) {
                    throw new IllegalArgumentException("已经存在同名文件: " + parent);
                }
            } else {
                Files.createDirectories(parent);
            }
            Files.createFile(ofdPath);
        }
        ofdDoc = new OFDDoc(ofdPath);
    }

    /**
     * create PDF-to-OFD converter
     *
     * @param output 转换后的OFD流，由调用者负责关闭。
     * @throws IOException file parsing exception
     */
    public TextConverter(OutputStream output) throws IOException {
        if (output == null) {
            throw new IllegalArgumentException("转换后的OFD流为空");
        }
        ofdDoc = new OFDDoc(output);
    }


    /**
     * 向OFD页面中追加文本
     *
     * @param filepath path to the file to be converted
     * @param indexes  ignored
     * @throws GeneralConvertException conversion exception
     */
    @Override
    public void convert(Path filepath, int... indexes) throws GeneralConvertException {
        if (filepath == null || !Files.exists(filepath) || Files.isDirectory(filepath)) {
            return;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(filepath.toFile()))) {
            String line;
            while ((line = br.readLine()) != null) {
                Paragraph p = new Paragraph(line, fontSize);
                ofdDoc.add(p);
            }
        } catch (IOException e) {
            throw new GeneralConvertException("文本转换OFD异常", e);
        }
    }

    /**
     * 追加文本，文本将新起一行
     *
     * @param txt 文本
     */
    public void append(String txt)  {
        Paragraph p = new Paragraph(txt, fontSize);
        ofdDoc.add(p);
    }

    /**
     * 设置页面尺寸
     *
     * @param pageLayout 页面尺寸
     */
    public void setPageSize(PageLayout pageLayout) {
        this.ofdDoc.setDefaultPageLayout(pageLayout);
    }

    /**
     * set font size
     *
     * @param fontSize 字号，单位：毫米（mm）
     */
    public void setFontSize(double fontSize) {
        if (fontSize <= 0) {
            fontSize = 3;
        }
        this.fontSize = fontSize;
    }

    @Override
    public void close() throws IOException {
        if (closed) {
            return;
        }
        closed = true;
        if (ofdDoc != null) {
            ofdDoc.close();
        }
    }
}

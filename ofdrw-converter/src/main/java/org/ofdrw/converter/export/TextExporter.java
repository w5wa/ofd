package org.ofdrw.converter.export;

import org.ofdrw.converter.GeneralConvertException;
import org.ofdrw.reader.ContentExtractor;
import org.ofdrw.reader.OFDReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;

/**
 * OFD导出为纯文本
 * <p>
 * 注意：部分OFD document由于采用glyph索引来定位文字、有个OFD整个页面均为路径数据图元而不是文字图元、有的OFD页面整个都为image等诸多原因。
 * 因此该导出器可能文档文本，另外由于文本布局等各种因素，导出文档也难以保证文本顺序与预期一致。
 *
 * @author Quan Guanyu
 * @since 2023-3-14 21:07:41
 */
public class TextExporter implements OFDExporter {

    /**
     * OFD parser
     */
    final OFDReader ofdReader;

    /**
     * 文本提取器
     */
    final ContentExtractor extractor;

    /**
     * 文字输出流
     */
    final PrintStream out;

    /**
     * whether the document has been closed
     */
    private boolean closed = false;

    /**
     * constructor for image converter
     *
     * @param ofdFilePath OFD file to be converted
     * @param txtPath     生成image存放目录
     * @throws IOException file parsing exception
     */
    public TextExporter(Path ofdFilePath, Path txtPath) throws IOException {
        ofdReader = new OFDReader(ofdFilePath);
        if (txtPath == null) {
            throw new IllegalArgumentException("导出文本file path为空");
        }
        txtPath = txtPath.toAbsolutePath();
        if (!Files.exists(txtPath)) {
            Path parent = txtPath.getParent();
            if (Files.exists(parent)) {
                if (!Files.isDirectory(parent)) {
                    throw new IllegalArgumentException("已经存在同名文件: " + parent);
                }
            } else {
                Files.createDirectories(parent);
            }
            Files.createFile(txtPath);
        }
        extractor = new ContentExtractor(ofdReader);
        out = new PrintStream(Files.newOutputStream(txtPath));
    }

    /**
     * constructor for image converter
     *
     * @param ofdInput  OFD file to be converted流，该文件流由调用者负责关闭
     * @param txtOutput 文本输出流
     * @throws IOException file parsing exception
     */
    public TextExporter(InputStream ofdInput, OutputStream txtOutput) throws IOException {
        ofdReader = new OFDReader(ofdInput);
        if (txtOutput == null) {
            throw new IllegalArgumentException("导出流为空");
        }
        extractor = new ContentExtractor(ofdReader);
        out = new PrintStream(txtOutput);
    }

    /**
     * export specified OFD page as image
     *
     * @param indexes page index sequence; if null, means all pages (note: page index starts from 0)
     * @throws GeneralConvertException conversion exception
     */
    @Override
    public void export(int... indexes) throws GeneralConvertException {
        List<Integer> targetPages = new LinkedList<>();
        if (indexes == null || indexes.length == 0) {
            for (int i = 0; i < ofdReader.getNumberOfPages(); i++) {
                targetPages.add(i);
            }
        } else {
            int maxPageIndex = ofdReader.getNumberOfPages();
            // get information for specified page
            for (int index : indexes) {
                if (index < 0 || index >= maxPageIndex) {
                    continue;
                }
                targetPages.add(index);
            }
        }

        for (Integer index : targetPages) {
            List<String> pageContent = extractor.getPageContent(index + 1);
            pageContent.forEach(out::println);
        }
    }

    @Override
    public void close() throws IOException {
        if (closed) {
            return;
        }
        closed = true;
        if (out != null) {
            out.close();
        }

        if (ofdReader != null) {
            ofdReader.close();
        }
    }
}

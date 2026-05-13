package org.ofdrw.converter.export;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.ofdrw.converter.GeneralConvertException;
import org.ofdrw.converter.PdfboxMaker;
import org.ofdrw.reader.OFDReader;
import org.ofdrw.reader.PageInfo;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;

/**
 * PDFBox 实现的OFD转换PDF
 *
 * @author Quan Guanyu
 * @since 2023-3-7 21:30:40
 */
public class PDFExporterPDFBox implements OFDExporter {

    /**
     * OFD parser
     */
    final OFDReader ofdReader;

    /**
     * PDFdocument object
     */
    final PDDocument pdfDoc;

    /**
     * PDF转换器
     */
    final PdfboxMaker pdfMaker;

    /**
     * 导出流
     */
    OutputStream outputStream;
    /**
     * 导出的file path
     */
    Path outputPath;

    /**
     * whether the document has been closed
     */
    private boolean closed = false;


    /**
     * 通过file path 创建PDF转换器
     *
     * @param ofdFilePath 待转换的OFDfile path
     * @param pdfFilePath 生成PDFfile path
     * @throws IOException file creation failed
     */
    public PDFExporterPDFBox(Path ofdFilePath, Path pdfFilePath) throws IOException {
        ofdReader = new OFDReader(ofdFilePath);
        pdfDoc = new PDDocument();
        pdfMaker = new PdfboxMaker(this.ofdReader, pdfDoc);
        if (pdfFilePath == null) {
            throw new IllegalArgumentException("导出PDF路径为空");
        }
        pdfFilePath = pdfFilePath.toAbsolutePath();
        if (!Files.exists(pdfFilePath)) {
            Path parent = pdfFilePath.getParent();
            if (Files.exists(parent)) {
                if (!Files.isDirectory(parent)) {
                    throw new IllegalArgumentException("已经存在同名文件: " + parent);
                }
            } else {
                Files.createDirectories(parent);
            }
            Files.createFile(pdfFilePath);
        }
        this.outputPath = pdfFilePath;
    }

    /**
     * 通过流 创建PDF转换器
     * <p>
     * 注意：流由调用者负责关闭！
     *
     * @param ofdInStream  待转换的OFD file流，流由调用者负责关闭。
     * @param pdfOutStream 生成PDF文件流，流由调用者负责关闭。
     * @throws IOException 流操作失败
     */
    public PDFExporterPDFBox(InputStream ofdInStream, OutputStream pdfOutStream) throws IOException {
        ofdReader = new OFDReader(ofdInStream);
        pdfDoc = new PDDocument();
        pdfMaker = new PdfboxMaker(this.ofdReader, pdfDoc);
        if (pdfOutStream == null) {
            throw new IllegalArgumentException("导出PDF流为空");
        }
        this.outputStream = pdfOutStream;
    }

    /**
     * export specified OFD page
     *
     * @param indexes page index sequence; if null, means all pages (note: page index starts from 0)
     * @throws GeneralConvertException 导出异常
     */
    @Override
    public void export(int... indexes) throws GeneralConvertException {
        try {
            List<PageInfo> targetPages = new LinkedList<>();
            if (indexes == null || indexes.length == 0) {
                targetPages.addAll(ofdReader.getPageList());
            } else {
                int maxPageIndex = ofdReader.getNumberOfPages();
                // get information for specified page
                for (int index : indexes) {
                    if (index < 0 || index >= maxPageIndex) {
                        continue;
                    }
                    targetPages.add(ofdReader.getPageInfo(index));
                }
            }
            // iteratively add pages
            targetPages = ofdReader.getPageList();
            for (PageInfo pageInfo : targetPages) {
                pdfMaker.makePage(pageInfo);
            }
        } catch (IOException e) {
            throw new GeneralConvertException("OFD转换PDF失败 ", e);
        }
    }

    /**
     * 关闭所有打开的文件
     * <p>
     * 并把附件添加到PDF文件中
     *
     * @throws IOException 文件关闭异常
     */
    @Override
    public void close() throws IOException {
        if (closed) {
            return;
        }
        closed = true;

        if (pdfMaker != null && pdfDoc != null && ofdReader != null) {
            // add attachment
            pdfMaker.addAttachments(ofdReader);
            // 存储到文件中
            if (outputPath != null) {
                pdfDoc.save(this.outputPath.toFile());
            } else if (outputStream != null) {
                pdfDoc.save(outputStream);
            }
        }

        if (pdfDoc != null) {
            pdfDoc.close();
        }

        if (ofdReader != null) {
            ofdReader.close();
        }
    }
}

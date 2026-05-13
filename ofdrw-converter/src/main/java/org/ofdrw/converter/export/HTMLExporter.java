package org.ofdrw.converter.export;

import org.ofdrw.converter.GeneralConvertException;
import org.ofdrw.converter.HtmlMaker;
import org.ofdrw.converter.SVGMaker;
import org.ofdrw.reader.OFDReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;

/**
 * 基于SVG转换的 OFD HTML转换器
 *
 * @author Quan Guanyu
 * @since 2023-3-8 21:57:03
 */
public class HTMLExporter implements OFDExporter {

    /**
     * OFD parser
     */
    final OFDReader ofdReader;
    /**
     * HTML转换器
     */
    final HtmlMaker htmlMaker;

    /**
     * SVG转换器
     */
    final SVGMaker svgMaker;

    /**
     * 文件输出位置
     */
    final OutputStream output;


    /*
<!DOCTYPE html>
<html lang="en">

<head>
  <meta charset="UTF-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>文件预览</title>
</head>
<body style="margin: 0;background: #E6E8EB;height: 100%">
  <div style="display: flex; flex-direction: column;align-items: center;">
     <!--内容 -->
  </div>
</body>
</html>
     */

    /**
     * HTML文件头部
     * <p>
     * you can override HTML content through inheritance
     */
    byte[] header = ("<!DOCTYPE html>\n" +
            "<html lang=\"en\">\n" +
            "\n" +
            "<head>\n" +
            "  <meta charset=\"UTF-8\">\n" +
            "  <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
            "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
            "  <title>文件预览</title>\n" +
            "</head>\n" +
            "<body style=\"margin: 0;background: #E6E8EB;height: 100%\">\n" +
            "  <div style=\"display: flex; flex-direction: column;align-items: center;\">" +
            "    <div style=\"height: 10px;\"></div>").getBytes(StandardCharsets.UTF_8);
    /**
     * 文件标签闭合
     * <p>
     * you can override HTML content through inheritance
     */
    byte[] booter = ("" +
            "  </div>\n" +
            "</body>\n" +
            "</html>").getBytes(StandardCharsets.UTF_8);

    /**
     * 每一页之间的间隔
     * <p>
     * you can override HTML content through inheritance
     */
    byte[] margin_bottom = "<div style=\"height: 10px;\"></div>".getBytes(StandardCharsets.UTF_8);

    /**
     * whether the document has been closed
     */
    private boolean closed = false;

    /**
     * constructor for image converter
     *
     * @param ofdFilePath  OFD file to be converted
     * @param htmlFilePath 生成HTML存放目录
     * @throws IOException file parsing exception
     */
    public HTMLExporter(Path ofdFilePath, Path htmlFilePath) throws IOException {
        if (htmlFilePath == null) {
            throw new IllegalArgumentException("导出HTMLfile path为空");
        }
        htmlFilePath = htmlFilePath.toAbsolutePath();
        if (!Files.exists(htmlFilePath)) {
            Path parent = htmlFilePath.getParent();
            if (Files.exists(parent)) {
                if (!Files.isDirectory(parent)) {
                    throw new IllegalArgumentException("已经存在同名文件: " + parent);
                }
            } else {
                Files.createDirectories(parent);
            }
            Files.createFile(htmlFilePath);
        }

        ofdReader = new OFDReader(ofdFilePath);
        htmlMaker = new HtmlMaker(ofdReader, 1000);
        svgMaker = new SVGMaker(ofdReader, 0);
        output = Files.newOutputStream(htmlFilePath);
        svgMaker.config.setDrawBoundary(false);
        svgMaker.config.setClip(false);
        output.write(header);
    }

    /**
     * constructor for image converter
     *
     * @param ofdInput   OFD file to be converted stream; caller is responsible for closing
     * @param htmlOutput 生成HTML输出流
     * @throws IOException file parsing exception
     */
    public HTMLExporter(InputStream ofdInput, OutputStream htmlOutput) throws IOException {
        if (ofdInput == null) {
            throw new IllegalArgumentException("OFD流为空");
        }
        if (htmlOutput == null) {
            throw new IllegalArgumentException("HTML流为空");
        }
        ofdReader = new OFDReader(ofdInput);
        htmlMaker = new HtmlMaker(ofdReader, 1000);
        svgMaker = new SVGMaker(ofdReader, 0);
        svgMaker.config.setDrawBoundary(false);
        svgMaker.config.setClip(false);
        output = htmlOutput;
        output.write(header);
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

        try {
            for (Integer index : targetPages) {
                // 生成HTML Div
                String pageDiv = htmlMaker.makePageDiv(svgMaker, index);
                output.write(pageDiv.getBytes(StandardCharsets.UTF_8));
                output.write(margin_bottom);
            }
        } catch (IOException|RuntimeException e) {
            throw new RuntimeException("文件转换或文件写入异常", e);
        }
    }

    @Override
    public void close() throws IOException {
        if (closed) {
            return;
        }
        closed = true;
        if (ofdReader != null) {
            ofdReader.close();
        }

        if (output != null) {
            output.write(booter);
            output.close();
        }
    }

}

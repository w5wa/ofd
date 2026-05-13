package org.ofdrw.converter.ofdconverter;

import org.ofdrw.converter.GeneralConvertException;
import org.ofdrw.layout.OFDDoc;
import org.ofdrw.layout.PageLayout;
import org.ofdrw.layout.VirtualPage;
import org.ofdrw.layout.element.Img;
import org.ofdrw.layout.element.Position;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * image转换为OFD
 *
 * @author Quan Guanyu
 * @since 2023-3-14 23:09:08
 */
public class ImageConverter implements DocConverter {

    /**
     * OFD document object
     */
    final OFDDoc ofdDoc;


    /**
     * whether the document has been closed
     */
    private boolean closed = false;

    /**
     * 每毫米像素数量
     */
    double ppm = 15;


    /**
     * create PDF-to-OFD converter
     *
     * @param ofdPath path to converted OFD file
     * @throws IOException file parsing exception
     */
    public ImageConverter(Path ofdPath) throws IOException {
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
     * @param output 转换后的OFD流
     * @throws IOException file parsing exception
     */
    public ImageConverter(OutputStream output) throws IOException {
        if (output == null) {
            throw new IllegalArgumentException("转换后的OFD流为空");
        }
        ofdDoc = new OFDDoc(output);
    }


    /**
     * 新建OFD页面并放入image
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
        PageLayout pageLayout = this.ofdDoc.getPageLayout();
        double pageWidth = pageLayout.getWidth();
        double pageHeight = pageLayout.getHeight();

        double width = 50;
        double height = 50;
        try {

            BufferedImage image = Img.readImage(filepath.toFile());
            if (image != null) {
                // image可以解析时分析大小
                width = image.getWidth() / ppm;
                height = image.getHeight() / ppm;
                double scale = 1;
                if (width > pageWidth) {
                    scale = pageWidth / width;
                }
                if (height > pageHeight) {
                    if ((pageHeight / height) > scale) {
                        scale = pageHeight / height;
                    }
                }
                width = width * scale;
                height = height * scale;
            }

            VirtualPage page = new VirtualPage(pageLayout);

            Img item = new Img(width, height, filepath);
            item.setPosition(Position.Absolute);
            double x = (pageWidth - width) / 2;
            double y = (pageHeight - height) / 2;
            item.setXY(x, y);
            page.add(item);
            ofdDoc.addVPage(page);
        } catch (IOException e) {
            throw new GeneralConvertException("image转换OFD异常", e);
        }
    }

    /**
     * 追加image到新一页
     *
     * @param filepath image文件
     * @param width    image在页面中width
     * @param height   image在页面中height
     */
    public void append(Path filepath, double width, double height) {
        try {
            PageLayout pageLayout = this.ofdDoc.getPageLayout();
            double pageWidth = pageLayout.getWidth();
            double pageHeight = pageLayout.getHeight();

            VirtualPage page = new VirtualPage(pageLayout);

            Img item = new Img(width, height, filepath);
            item.setPosition(Position.Absolute);
            double x = (pageWidth - width) / 2;
            double y = (pageHeight - height) / 2;
            item.setXY(x, y);
            page.add(item);
            ofdDoc.addVPage(page);
        } catch (IOException e) {
            throw new GeneralConvertException("image转换OFD异常", e);
        }
    }

    /**
     * 设置image质量
     *
     * @param ppm 像素每毫米
     */
    public void setPPM(double ppm) {
        if (ppm <= 0) {
            throw new IllegalArgumentException("PPM不能小于0");
        }
        this.ppm = ppm;
    }

    /**
     * 设置页面尺寸
     *
     * @param pageLayout 页面尺寸
     */
    public void setPageSize(PageLayout pageLayout) {
        this.ofdDoc.setDefaultPageLayout(pageLayout);
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

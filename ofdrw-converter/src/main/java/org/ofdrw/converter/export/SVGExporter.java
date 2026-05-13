package org.ofdrw.converter.export;

import org.ofdrw.converter.GeneralConvertException;
import org.ofdrw.converter.SVGMaker;
import org.ofdrw.reader.OFDReader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * OFD SVG转换器
 *
 * @author Quan Guanyu
 * @since 2023-3-8 21:45:48
 */
public class SVGExporter implements OFDExporter {

    /**
     * OFD parser
     */
    final OFDReader ofdReader;
    /**
     * image转换器
     */
    final SVGMaker svgMaker;

    /**
     * SVG文件输出路径
     */
    final Path outDirPath;

    /**
     * 转换生成的image文件序列
     */
    List<Path> svgFileArr;

    /**
     * whether the document has been closed
     */
    private boolean closed = false;

    /**
     * constructor for image converter
     *
     * @param ofdFilePath OFD file to be converted
     * @param imgDirPath  生成SVG存放目录
     * @throws IOException file parsing exception
     */
    public SVGExporter(Path ofdFilePath, Path imgDirPath) throws IOException {
        this(ofdFilePath, imgDirPath, 15);
    }

    /**
     * constructor for image converter
     *
     * @param ofdInput   OFD file to be converted stream; caller is responsible for closing
     * @param imgDirPath 生成SVG存放目录
     * @throws IOException file parsing exception
     */
    public SVGExporter(InputStream ofdInput, Path imgDirPath) throws IOException {
        this(ofdInput, imgDirPath, 15);
    }

    /**
     * constructor for image converter
     *
     * @param ofdFilePath OFD file to be converted
     * @param imgDirPath  生成SVG存放目录
     * @param ppm         转换SVG质量，pixels per millimeter
     * @throws IOException file parsing exception
     */
    public SVGExporter(Path ofdFilePath, Path imgDirPath, double ppm) throws IOException {
        ofdReader = new OFDReader(ofdFilePath);

        if (imgDirPath == null) {
            throw new IllegalArgumentException("导出imagefile path为空");
        }
        imgDirPath = imgDirPath.toAbsolutePath();
        if (Files.exists(imgDirPath) && !Files.isDirectory(imgDirPath)) {
            throw new IllegalArgumentException("已经存在同名文件");
        }
        if (!Files.exists(imgDirPath)) {
            Files.createDirectories(imgDirPath);
        }
        svgMaker = new SVGMaker(ofdReader, ppm);
        svgMaker.config.setDrawBoundary(false);
        svgMaker.config.setClip(false);
        svgFileArr = new ArrayList<>();
        outDirPath = imgDirPath;
    }

    /**
     * constructor for image converter
     *
     * @param ofdInput   OFD file to be converted流，该文件流由调用者负责关闭
     * @param imgDirPath 生成SVG存放目录
     * @param ppm        转换SVG质量，pixels per millimeter
     * @throws IOException file parsing exception
     */
    public SVGExporter(InputStream ofdInput, Path imgDirPath, double ppm) throws IOException {
        ofdReader = new OFDReader(ofdInput);
        if (imgDirPath == null) {
            throw new IllegalArgumentException("导出imagefile path为空");
        }
        imgDirPath = imgDirPath.toAbsolutePath();
        if (Files.exists(imgDirPath) && !Files.isDirectory(imgDirPath)) {
            throw new IllegalArgumentException("已经存在同名文件");
        }
        if (!Files.exists(imgDirPath)) {
            Files.createDirectories(imgDirPath);
        }
        svgMaker = new SVGMaker(ofdReader, ppm);
        svgMaker.config.setDrawBoundary(false);
        svgMaker.config.setClip(false);
        svgFileArr = new ArrayList<>();
        outDirPath = imgDirPath;
    }

    /**
     * export specified OFD page为SVG
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
                String svg = svgMaker.makePage(index);
                Path dst = this.outDirPath.resolve(svgFileArr.size() + ".svg");
                Files.write(dst, svg.getBytes());
                this.svgFileArr.add(dst);
            }
        } catch (IOException e) {
            throw new GeneralConvertException("SVGconversion exception", e);
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
    }

    /**
     * 获取已经转换完成的页面的image path
     *
     * @return 页面image path
     */
    public List<Path> getSvgFilePaths() {
        return svgFileArr;
    }

    /**
     * 设置转换SVG质量
     * <p>
     * 请在调用 {@link #export(int...)} 方法之前设置PPM！
     *
     * @param ppm pixels per millimeter
     */
    public void setPPM(double ppm) {
        if (svgMaker == null) {
            return;
        }
        svgMaker.setPPM(ppm);
    }
}

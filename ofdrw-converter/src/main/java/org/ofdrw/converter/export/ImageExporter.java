package org.ofdrw.converter.export;

import org.ofdrw.converter.GeneralConvertException;
import org.ofdrw.converter.ImageMaker;
import org.ofdrw.reader.OFDReader;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * OFDimage转换器
 *
 * @author Quan Guanyu
 * @since 2023-3-8 19:50:52
 */
public class ImageExporter implements OFDExporter {

    /**
     * OFD parser
     */
    final OFDReader ofdReader;
    /**
     * image转换器
     */
    final ImageMaker imageMaker;

    /**
     * image类型
     */
    final String imageType;

    /**
     * image文件输出路径
     */
    final Path outDirPath;

    /**
     * 转换生成的image文件序列
     */
    List<Path> imgFileArr;

    /**
     * whether the document has been closed
     */
    private boolean closed = false;

    /**
     * constructor for image converter
     *
     * @param ofdFilePath OFD file to be converted
     * @param imgDirPath  生成image存放目录
     * @throws IOException file parsing exception
     */
    public ImageExporter(Path ofdFilePath, Path imgDirPath) throws IOException {
        this(ofdFilePath, imgDirPath, "PNG", 15);
    }

    /**
     * constructor for image converter
     *
     * @param ofdInput   OFD file to be converted stream; caller is responsible for closing
     * @param imgDirPath 生成image存放目录
     * @throws IOException file parsing exception
     */
    public ImageExporter(InputStream ofdInput, Path imgDirPath) throws IOException {
        this(ofdInput, imgDirPath, "PNG", 15);
    }

    /**
     * constructor for image converter
     *
     * @param ofdFilePath OFD file to be converted
     * @param imgDirPath  生成image存放目录
     * @param imageType   生成image的格式，如 JPG、PNG、GIF、BMP
     * @param ppm         转换image质量，pixels per millimeter
     * @throws IOException file parsing exception
     */
    public ImageExporter(Path ofdFilePath, Path imgDirPath, String imageType, double ppm) throws IOException {
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
        imageMaker = new ImageMaker(ofdReader, ppm);
        imageMaker.config.setDrawBoundary(false);
        this.imageType = imageType;
        imgFileArr = new ArrayList<>();
        outDirPath = imgDirPath;
    }

    /**
     * constructor for image converter
     *
     * @param ofdInput   OFD file to be converted流，该文件流由调用者负责关闭
     * @param imgDirPath 生成image存放目录
     * @param imageType  生成image的格式，如 JPG、PNG、GIF、BMP
     * @param ppm        转换image质量，pixels per millimeter
     * @throws IOException file parsing exception
     */
    public ImageExporter(InputStream ofdInput, Path imgDirPath, String imageType, double ppm) throws IOException {
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
        imageMaker = new ImageMaker(ofdReader, ppm);
        imageMaker.config.setDrawBoundary(false);
        this.imageType = imageType;
        imgFileArr = new ArrayList<>();
        outDirPath = imgDirPath;
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
                BufferedImage image = imageMaker.makePage(index);
                Path dst = this.outDirPath.resolve(imgFileArr.size() + "." + imageType.toLowerCase());
                ImageIO.write(image, imageType, dst.toFile());
                this.imgFileArr.add(dst);
            }
        } catch (IOException e) {
            throw new GeneralConvertException("imageconversion exception", e);
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
    public List<Path> getImgFilePaths() {
        return imgFileArr;
    }

    /**
     * 获取转换image类型
     *
     * @return image类型
     */
    public String getImageType() {
        return imageType;
    }

    /**
     * 设置转换image质量
     * <p>
     * 请在调用 {@link #export(int...)} 方法之前设置PPM！
     *
     * @param ppm pixels per millimeter
     */
    public void setPPM(double ppm) {
        if (imageMaker == null) {
            return;
        }
        imageMaker.setPPM(ppm);
    }
}

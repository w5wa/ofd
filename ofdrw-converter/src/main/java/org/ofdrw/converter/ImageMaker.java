package org.ofdrw.converter;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import org.ofdrw.converter.utils.CommonUtil;
import org.ofdrw.core.basicType.ST_Box;
import org.ofdrw.reader.OFDReader;
import org.ofdrw.reader.PageInfo;
import org.ofdrw.reader.tools.ImageUtils;

/**
 * image转换类
 *
 * @author qaqtutu
 * @since 2021-03-13 10:00:01
 */
public class ImageMaker extends AWTMaker {


    /**
     * create image converter instance
     * <p>
     * OFD uses millimeters as the basic unit internally
     * <p>
     * 如果需要更加精确的表示请使用{@link #ImageMaker(OFDReader, double)}
     *
     * @param reader OFD parser
     * @param ppm    pixels per millimeter
     */
    public ImageMaker(OFDReader reader, int ppm) {
        super(reader, ppm);
    }

    /**
     * create image converter instance
     * <p>
     * OFD uses millimeters as the basic unit internally
     *
     * @param reader OFD parser
     * @param ppm    pixels per millimeter，DPI与PPM转换可以使用{@link CommonUtil#dpiToPpm(int)}。
     * @author iandjava
     */
    public ImageMaker(OFDReader reader, double ppm) {
        super(reader, ppm);
    }

    /**
     * 渲染OFD页面为image
     *
     * @param pageIndex page number，从0起
     * @return 渲染完成的image
     */
    public BufferedImage makePage(int pageIndex) {
        if (pageIndex < 0 || pageIndex >= pages.size()) {
            throw new GeneralConvertException(String.format("%s 不是有效索引", pageIndex));
        }
        PageInfo pageInfo = pages.get(pageIndex);
        ST_Box pageBox = pageInfo.getSize();

        // PPM 转 像素
        int pageWidthPixel = (int) Math.round(ppm * pageBox.getWidth());
        int pageHeightPixel = (int) Math.round(ppm * pageBox.getHeight());

        BufferedImage image = createImage(pageWidthPixel, pageHeightPixel);
        Graphics2D graphics = (Graphics2D) image.getGraphics();

        writePage(graphics, pageInfo, null);

        return image;
    }

    /**
     * 创建image
     *
     * @param pageWidthPixel  图形width
     * @param pageHeightPixel 图像height
     */
    private BufferedImage createImage(int pageWidthPixel, int pageHeightPixel) {
        return ImageUtils.createImage(pageWidthPixel, pageHeightPixel, isStamp);
    }
}

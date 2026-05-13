package org.ofdrw.reader.tools;

import org.apache.pdfbox.jbig2.JBIG2ImageReader;
import org.apache.pdfbox.jbig2.JBIG2ImageReaderSpi;
import org.apache.pdfbox.jbig2.io.DefaultInputStreamFactory;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static java.awt.image.BufferedImage.TYPE_INT_RGB;

/**
 * image处理工具
 *
 * @author qaqtutu
 * @since 2021-04-10 18:31:03
 */
public class ImageUtils {

    public static byte[] toBytes(BufferedImage bufferedImage, String type) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        boolean flag = ImageIO.write(bufferedImage, type, out);
        byte[] bytes = out.toByteArray();
        return bytes;
    }

    /**
     * 读取JB2格式image
     *
     * @param in image数据流
     * @return image数据
     * @throws IOException image操作异常
     */
    public static BufferedImage readJB2(InputStream in) throws IOException {
        int imageIndex = 0;
        JBIG2ImageReader imageReader = null;
        DefaultInputStreamFactory disf = new DefaultInputStreamFactory();
        ImageInputStream imageInputStream = disf.getInputStream(in);

        imageReader = new JBIG2ImageReader(new JBIG2ImageReaderSpi());
        imageReader.setInput(imageInputStream);
        return imageReader.read(imageIndex, imageReader.getDefaultReadParam());

    }


    /**
     * 蒙版抠图
     * <p>
     * 根据mask中像素的颜色将原图中的像素抠掉
     *
     * @param image 原始image
     * @param mask  蒙板image
     * @return 扣去背景的image对象
     */
    public static BufferedImage renderMask(BufferedImage image, BufferedImage mask) {
        if ((image.getWidth() != mask.getWidth() || image.getHeight() != mask.getHeight())) {
            return image;
        }
        return renderMask(image, mask, (r, g, b) -> (r + g + b) / 3 > 244);
    }

    private interface PixelFilter {
        boolean filter(int r, int g, int b);
    }

    private static BufferedImage renderMask(BufferedImage image, BufferedImage mask, PixelFilter pixelFilter) {
        BufferedImage out = createImage(image.getWidth(), image.getHeight(), true);
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                int rgb = mask.getRGB(x, y);
                int r = 0xFF & rgb;
                int g = 0xFF00 & rgb;
                g >>= 8;
                int b = 0xFF0000 & rgb;
                b >>= 16;
                boolean sholdMask = pixelFilter.filter(r, g, b);
                if (sholdMask) {
                    out.setRGB(x, y, image.getRGB(x, y));
                }
            }
        }
        return out;
    }

    /**
     * 计算灰度
     *
     * @param r 红色通道
     * @param g 绿色通道
     * @param b 蓝色通道
     * @return 灰度值
     */
    public static int gray(int r, int g, int b) {
        return (r * 19595 + g * 38469 + b * 7472) >> 16;
    }

    /**
     * 创建image
     *
     * @param width         图形width
     * @param height        图像height
     * @param isTransparent 是否透明
     * @return image对象
     */
    public static BufferedImage createImage(int width, int height, boolean isTransparent) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 1.0f));
        if (isTransparent) {
            graphics.setColor(Color.WHITE);
            image = graphics.getDeviceConfiguration().createCompatibleImage(image.getWidth(), image.getHeight(), Transparency.TRANSLUCENT);
        } else {
            graphics.setColor(Color.WHITE);
            graphics.fillRect(0, 0, image.getWidth(), image.getHeight());
        }
        return image;
    }

    /**
     * 清除image背景
     *
     * @param in   输入image
     * @param gray 灰度阈值(0-255)，图像中大于该值的像素将会被删除。
     * @return 清空背景的image
     */
    public static BufferedImage clearWhiteBackground(BufferedImage in, int gray) {
        return renderMask(in, in, (r, g, b) -> gray(r, g, b) < gray);
    }
}

package org.ofdrw.layout.edit;

import org.ofdrw.core.basicType.STBase;
import org.ofdrw.layout.element.canvas.DrawContext;
import org.ofdrw.layout.element.canvas.Drawer;
import org.ofdrw.layout.element.canvas.TextMetrics;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * 水印绘制器
 *
 * @author bhzhange
 * @since 2024-04-18 13:18
 */
public class WatermarkDrawer implements Drawer {

    /**
     * 是否启用DEBUG
     */
    boolean DEBUG = false;

    /**
     * 单元格文字内容
     */
    private String value;


    /**
     * OFD注释对象 水印将生成特殊的注释对象
     */
    private Annotation annotation;

    /**
     * 外部fontfile path
     */
    private Path extFontPath = null;

    /**
     * 文字颜色
     * <p>
     * 支持16进制color value：#000000
     * <p>
     * RGB：rgb(0,0,0)
     * <p>
     * RGBA：rgba(0,0,0,1)
     * <p>
     * 默认：#000000 （黑色）
     */
    private String color = "#000000";

    /**
     * font name
     * <p>
     * 默认：宋体
     */
    private String fontName = "宋体";

    /**
     * 字号
     * <p>
     * 默认：3 （单位：毫米）
     */
    private double fontSize = 4.5;

    /**
     * whether bold
     */
    private Boolean bold = false;

    /**
     * fontwidth
     * <p>
     * normal、bold、bolder、lighter、100、200、300、400、500、600、700、800、900
     */
    private String fontWeight = "normal";

    /**
     * whether italic
     */
    private Boolean italic = false;

    /**
     * 文字之间的间距
     */
    private Double letterSpacing = 0d;

    /**
     * 透明度
     */
    private Double globalAlpha = 0.5d;

    /**
     * rotation angle，0-360, 默认330（-30）
     */
    private Double angle = 330d;

    /**
     * 水印横向间距, 单位mm
     */
    private Double intervalX = 30d;

    /**
     * 水印纵向间距, 单位mm
     */
    private Double intervalY = 30d;


    /**
     * 初始化一个水印绘制器
     */
    public WatermarkDrawer() {

    }

    /**
     * 绘制水印
     *
     * @param ctx drawing context
     * @throws IOException ignore
     */
    @Override
    public void draw(DrawContext ctx) throws IOException {
        if (this.value == null || this.value.isEmpty()) {
            return;
        }
        if (this.annotation == null) {
            return;
        }
        if (extFontPath != null) {
            // add external font
            ctx.addFont(fontName, extFontPath);
        }

        ctx.fillStyle = this.color;
        ctx.font = getFont();
        if (this.letterSpacing != 0) {
            // set character spacing
            ctx.getFont().setLetterSpacing(this.letterSpacing);
        }
        // set font color
        if (this.color != null && !this.color.isEmpty()) {
            ctx.fillStyle = this.color;
        }

        double width = annotation.getBoundary().getWidth();
        double height = annotation.getBoundary().getHeight();

        /*
         * 计算水印实际占位横向width和竖向height，并自适应计算水印的起始坐标.
         */
        TextMetrics metrics = ctx.measureText(this.value);

        double x = annotation.getBoundary().getTopLeftX();
        while (x < width) {
            double y = annotation.getBoundary().getTopLeftY();
            while (y < height) {
                ctx.save();
                ctx.setGlobalAlpha(this.globalAlpha);
                ctx.translate(x, y);
                ctx.rotate(this.angle);
                ctx.fillText(this.value, 0, metrics.fontSize);
                ctx.restore();
                if (isDEBUG()) {
                    debugBorder(ctx, metrics, x, y);
                }
                y += intervalY;
            }
            x += intervalX;
        }
    }

    /**
     * 组装font描述
     *
     * @return font描述
     */
    private String getFont() {

        // 设置font样式
        String fontStr = "";
        if (italic) {
            fontStr += "italic ";
        }
        if (bold) {
            fontStr += "bold ";
        } else if (fontWeight != null && !fontWeight.isEmpty()) {
            fontStr += fontWeight + " ";
        }
        fontStr = fontStr + STBase.fmt(fontSize) + "mm " + fontName;

        return fontStr;
    }

    /**
     * 水印文字内容
     *
     * @return 水印文字内容
     */
    public String getValue() {
        return value;
    }

    /**
     * 设置水印文字内容
     *
     * @param value 水印文字内容
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * 获取文字颜色
     *
     * @return color value
     */
    public String getColor() {
        return color;
    }

    /**
     * 设置文字颜色
     *
     * @param color 符合CSS3样式的color value被支持，可选值 颜色英文单词,例如red,blue、16进制color value，#000000、rgb(0,0,0)、rgba(0,0,0,1)
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * get font name
     *
     * @return font name
     */
    public String getFontName() {
        return fontName;
    }

    /**
     * set font name
     *
     * @param fontName font name
     */
    public void setFontName(String fontName) {
        this.fontName = fontName;
    }

    /**
     * get font size
     *
     * @return 字号，单位 mm
     */
    public double getFontSize() {
        return fontSize;
    }

    /**
     * set font size
     *
     * @param fontSize 字号，单位 mm
     */
    public void setFontSize(double fontSize) {
        this.fontSize = fontSize;
    }

    /**
     * whether bold
     *
     * @return 是否加粗
     */
    public Boolean getBold() {
        return bold;
    }

    /**
     * set whether bold
     *
     * @param bold whether bold
     */
    public void setBold(Boolean bold) {
        this.bold = bold;
    }

    /**
     * get font width
     *
     * @return fontwidth，如：normal、bold、bolder、lighter、100、200、300、400、500、600、700、800、900
     */
    public String getFontWeight() {
        return fontWeight;
    }

    /**
     * set font width
     *
     * @param fontWeight fontwidth，如：normal、bold、bolder、lighter、100、200、300、400、500、600、700、800、900
     */
    public void setFontWeight(String fontWeight) {
        this.fontWeight = fontWeight;
    }

    /**
     * whether italic
     *
     * @return 是否斜体
     */
    public Boolean getItalic() {
        return italic;
    }

    /**
     * 设置是否斜体
     * @param italic 是否斜体
     */
    public void setItalic(Boolean italic) {
        this.italic = italic;
    }

    /**
     * 获取文字之间的间距
     *
     * @return 文字之间的间距，单位mm
     */
    public Double getLetterSpacing() {
        return letterSpacing;
    }

    /**
     * 设置文字之间的间距
     *
     * @param letterSpacing 文字之间的间距，单位mm
     */
    public void setLetterSpacing(Double letterSpacing) {
        this.letterSpacing = letterSpacing;
    }

    /**
     * 获取透明度
     *
     * @return 透明度，0.0~1.0
     */
    public Double getGlobalAlpha() {
        return globalAlpha;
    }

    /**
     * set transparency
     *
     * @param globalAlpha 透明度，0.0~1.0
     */
    public void setGlobalAlpha(Double globalAlpha) {
        if (globalAlpha > 1 || globalAlpha < 0) {
            throw new IllegalArgumentException("透明度超出范围0.0~1.0");
        }
        this.globalAlpha = globalAlpha;
    }

    /**
     * 获取rotation angle
     *
     * @return rotation angle，0-360, 默认330（-30）
     */
    public Double getAngle() {
        return angle;
    }

    /**
     * 设置rotation angle，基于坐标系(0,1)方向，向第4象限偏移的角度。
     * 与数学意义上的弧度角度存在
     *
     * @param angle rotation angle，0-360, 默认330（-30）
     */
    public void setAngle(Double angle) {
        if (angle > 360 || angle < -360) {
            throw new IllegalArgumentException("rotation angle超出范围");
        }
        if (angle < 0) {
            angle = 360 + angle;
        }
        this.angle = angle;
    }

    /**
     * 获取横向间隔
     *
     * @return 横向间隔，单位mm
     */
    public Double getIntervalX() {
        return intervalX;
    }

    /**
     * 设置水印横向间距
     *
     * @param intervalX 横向间距，单位mm
     */
    public void setIntervalX(Double intervalX) {
        this.intervalX = intervalX;
    }

    /**
     * 获取水印纵向间距，单位mm
     *
     * @return 水印纵向间距，单位mm
     */
    public Double getIntervalY() {
        return intervalY;
    }

    /**
     * 设置水印纵向间距
     *
     * @param intervalY 纵向间距，单位mm
     */
    public void setIntervalY(Double intervalY) {
        this.intervalY = intervalY;
    }

    /**
     * 是否调试
     *
     * @return 是否调试 true - 调试模式绘制边框 false - 非调试模式
     */
    public boolean isDEBUG() {
        return DEBUG;
    }

    /**
     * 设置调试状态
     *
     * @param DEBUG 是否调试 true - 调试模式绘制边框 false - 非调试模式
     */
    public void setDEBUG(boolean DEBUG) {
        this.DEBUG = DEBUG;
    }

    /**
     * 水印注释信息
     *
     * @return 水印注释对象，{@link Annotation} 的实现instance
     */
    public Annotation getAnnotation() {
        return annotation;
    }

    /**
     * 设置水印注释信息
     *
     * @param annotation 水印注释对象，{@link Annotation} 的实现instance
     */
    public void setAnnotation(Annotation annotation) {
        this.annotation = annotation;
    }


    /**
     * set external font for cell drawer
     * <p>
     * Note: OFDRW does not provide any font subsetting; your font file will be added directly to the OFD file, which may increase the file size significantly.
     *
     * @param fontName font name, e.g. "Source Han Serif"
     * @param fontPath path to the font file
     * @return this
     */
    public WatermarkDrawer setFont(String fontName, Path fontPath) {
        if (fontName == null || fontName.isEmpty()) {
            throw new IllegalArgumentException("font name(fontName)不能为空");
        }
        if (fontPath == null || Files.exists(fontPath) == false) {
            throw new IllegalArgumentException("font file(fontPath)不存在");
        }
        this.setFontName(fontName);
        this.extFontPath = fontPath;
        return null;
    }

    /**
     * 绘制辅助线
     *
     * @param ctx drawing context
     * @param metrics 文字度量
     * @param offsetX 横向偏移，单位mm
     * @param offsetY 纵向偏移，单位mm
     */
    private void debugBorder(DrawContext ctx, TextMetrics metrics, double offsetX, double offsetY) {
        double lineWidth = 0.353d;
        double width = metrics.width + lineWidth * 2;
        double height = metrics.fontSize + lineWidth * 2;

        ctx.save();
        ctx.translate(offsetX, offsetY);
        ctx.setLineDash(1.5d, 1.5d);
        ctx.setLineWidth(lineWidth);
        ctx.setGlobalAlpha(0.53);
        ctx.strokeStyle = "rgb(255,0,0)";
        ctx.moveTo(0, 0);
        ctx.lineTo(width, height);
        ctx.moveTo(width, 0);
        ctx.lineTo(0, height);
        ctx.rotate(angle);
        ctx.rect(offsetX, offsetY, width, height);
        ctx.stroke();
        ctx.restore();
    }

}

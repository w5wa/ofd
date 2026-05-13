package org.ofdrw.layout.element.canvas;

import org.ofdrw.core.basicType.STBase;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;

/**
 * 类单元格特殊Canvas 绘制器
 * <p>
 * 可以实现类似于单元个效果，用于简化区域占位区块绘制。
 *
 * @author Quan Guanyu
 * @since 2023-11-13 18:48:53
 */
public class CellContentDrawer implements Drawer {

    /**
     * 调试开关，在开启后会绘制辅助线
     */
    public static Boolean DEBUG = false;

    /**
     * Canvas对象
     */
    private final Canvas canvas;

    /**
     * 单元格文字内容
     */
    private String value;

    /**
     * 文字水平浮动方式
     * <p>
     * default: float left
     */
    private TextAlign textAlign = TextAlign.left;

    /**
     * 文字垂直方向浮动方式
     * <p>
     * 默认：center
     */
    private VerticalAlign verticalAlign = VerticalAlign.center;


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
    private double fontSize = 3;

    /**
     * line spacing
     */
    private Double lineSpace = 0.6;


    /**
     * whether bold
     */
    private Boolean bold = false;

    /**
     * get font width
     *
     * @return String fontwidth，应遵循 CSS3标准，可选值为 normal | bold | bolder | lighter | 100 | 200 | 300 | 400 | 500 | 600 | 700 | 800 | 900
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
     * image
     */
    private Img img = null;

    /**
     * 是否有下划线
     */
    private boolean underline = false;

    /**
     * 是否删除线
     */
    private boolean deleteLine = false;

    /**
     * 外部font路径
     */
    private Path extFontPath = null;


    /**
     * 通过已有Canvas构造单元格
     * <p>
     * 注意该方法将会替换Canvas的绘制器为单元格的绘制器。
     *
     * @param canvas Canvas
     */
    public CellContentDrawer(Canvas canvas) {
        if (canvas == null) {
            throw new IllegalArgumentException("Canvas 不能为空");
        }
        this.canvas = canvas;
        canvas.setDrawer(this);
    }

    /**
     * 创建单元格
     *
     * @param x      左下角x坐标
     * @param y      左下角y坐标
     * @param width  width
     * @param height height
     */
    public CellContentDrawer(double x, double y, double width, double height) {
        this.canvas = new Canvas(x, y, width, height);
        this.canvas.setDrawer(this);
    }


    /**
     * 文字行
     */
    private static class TextLine {
        /**
         * text content
         */
        public String text;
        /**
         * 文本width
         */
        public double width;

        public TextLine(String text, double width) {
            this.text = text;
            this.width = width;
            if (DEBUG) {
                System.out.println(">> text:" + text + " width:" + width);
            }
        }
    }

    /**
     * image
     */
    private static class Img {
        /**
         * image path
         */
        public Path path;
        /**
         * imagewidth
         */
        public double width;

        /**
         * imageheight
         */
        public double height;

        public Img(Path path, double width, double height) {
            this.path = path;
            this.width = width;
            this.height = height;
        }
    }


    /**
     * 单元格内部绘制
     *
     * @param ctx drawing context
     * @throws IOException 图形drawing exception
     */
    @Override
    public void draw(DrawContext ctx) throws IOException {
        ctx.save();
        try {
            if (this.img != null) {
                // 绘制image
                drawImg(ctx);
            } else {
                if (extFontPath != null) {
                    // add external font
                    ctx.addFont(fontName, extFontPath);
                }
                // 绘制文字
                drawText(ctx);
            }
        } finally {
            ctx.restore();
        }
    }

    /**
     * 单元格image绘制
     *
     * @param ctx drawing context
     * @throws IOException imagedrawing exception
     */
    private void drawImg(DrawContext ctx) throws IOException {
        if (this.img == null) {
            return;
        }
        if (this.img.width <= 0 || this.img.height <= 0) {
            // 若未对image进行宽高设置，则从image中获取宽高
            BufferedImage gImg = ImageIO.read(img.path.toFile());
            this.img.width = ctx.mm(gImg.getWidth());
            this.img.height = ctx.mm(gImg.getHeight());
            if (DEBUG) {
                System.out.printf(">> 从image中获取宽高 img.width:%.2f img.height:%.2f\n", img.width, img.height);
            }
        }

        double x = 0;
        switch (this.textAlign) {

            case right:
            case end:
                // 右浮动
                x = canvas.getWidth() - img.width;
                break;
            case center:
                // center
                x = (canvas.getWidth() - img.width) / 2d;
                break;
            case start:
            case left:
            default:
                // 左浮动
                x = 0d;
                break;
        }
        double y = 0;
        switch (this.verticalAlign) {
            case bottom:
                y = canvas.getHeight() - img.height;
                break;
            case center:
                y = (canvas.getHeight() - img.height) / 2d;
                break;
            case top:
            default:
                y = 0d;
                break;
        }
        ctx.drawImage(img.path, x, y, img.width, img.height);

        if (DEBUG) {
            debugBorder(ctx);
        }
    }

    /**
     * 单元格文字内容绘制
     *
     * @param ctx drawing context
     * @throws IOException 文字drawing exception
     */
    private void drawText(DrawContext ctx) throws IOException {
        if (this.value == null || this.value.isEmpty()) {
            return;
        }

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
        ctx.font = fontStr + STBase.fmt(fontSize) + "mm " + fontName;
        if (this.letterSpacing != 0) {
            // set character spacing
            ctx.getFont().setLetterSpacing(this.letterSpacing);
        }

        // set font color
        if (this.color != null && !this.color.isEmpty()) {
            ctx.fillStyle = this.color;
        }

        double width = canvas.getWidth();
        double height = canvas.getHeight();
        TextMetrics textMetrics = ctx.measureText(this.value);


        LinkedList<TextLine> lines = new LinkedList<>();
        double textLineWidth = 0;
        int offset = 0;
        // 分segment
        for (int i = 0; i < this.value.length(); i++) {
            char c = this.value.charAt(i);
            // 换行符提前结束
            if (c == '\n') {
                lines.add(new TextLine(this.value.substring(offset, i), textLineWidth));
                offset = i + 1;
                textLineWidth = 0;
                continue;
            }
            double cWidth = 0;
            if (i == this.value.length() - 1) {
                cWidth = ctx.measureText(String.valueOf(c)).width;
            } else {
                cWidth = textMetrics.offset[i];
            }
            if (textLineWidth + cWidth > width) {
                // 超出width，换行
                lines.add(new TextLine(this.value.substring(offset, i), textLineWidth));
                offset = i;
                textLineWidth = cWidth;
            } else {
                textLineWidth += cWidth;
            }
        }
        // 最后一行
        if (offset < this.value.length()) {
            lines.add(new TextLine(this.value.substring(offset), textLineWidth));
        }


        // 内容height：(字号 + 行间距) *行数
        double contentHeight = (this.fontSize + this.lineSpace) * lines.size();

        double offsetY = 0;
        // 第一行 Y 偏移量计算：font坐标位于font的左下角
        if (this.verticalAlign == VerticalAlign.top) {
            offsetY = fontSize;
        } else if (this.verticalAlign == VerticalAlign.center) {
            offsetY = height / 2 - contentHeight / 2 + fontSize;
        } else {
            // 底部对齐 偏移量为：总height - 内容height + 字号
            offsetY = height - contentHeight + fontSize;
        }

        for (TextLine line : lines) {
            double offsetX = 0;
            // 按照左右浮动方式 依次计算出每行的X 偏移量
            if (this.textAlign == TextAlign.left || this.textAlign == TextAlign.start || this.textAlign == null) {
                // 左浮动
                offsetX = 0;
            } else if (this.textAlign == TextAlign.center) {
                // center
                offsetX = (width - line.width) / 2d;
            } else {
                // 右浮动
                offsetX = width - line.width;
            }
            if (!"".equals(line.text)) {
                ctx.fillText(line.text, offsetX, offsetY);
            }
            // 文字装饰线条width
            // 比例系数由经验指定无特定规则
            double fontLineWidth = fontSize / 30;
            double underlineOffset = fontLineWidth * 3.2;
            // 下划线
            if (this.underline) {
                ctx.save();
                ctx.setLineWidth(fontLineWidth);
                ctx.beginPath();
                ctx.moveTo(offsetX, offsetY + underlineOffset);
                ctx.lineTo(offsetX + line.width, offsetY + underlineOffset);
                ctx.stroke();
                ctx.restore();
            }
            // 删除线
            if (this.deleteLine) {
                ctx.save();
                ctx.setLineWidth(fontLineWidth);
                ctx.beginPath();
                // 由于文字定位在基线位置，此处以 5/18 比例计算出删除线位置，5/18为经验值，无特殊意义。
                ctx.moveTo(offsetX, offsetY - fontSize * 5 / 18);
                ctx.lineTo(offsetX + line.width, offsetY - fontSize * 5 / 18);
                ctx.stroke();
                ctx.restore();
            }

            if (DEBUG) {
                ctx.save();
                ctx.setLineDash(1.5d, 1.5d);
                ctx.setLineWidth(0.1);
                ctx.setGlobalAlpha(0.53);
                ctx.strokeStyle = "rgb(255,0,0)";
                ctx.strokeRect(offsetX, offsetY - fontSize, line.width, fontSize + lineSpace);
                ctx.stroke();
                ctx.restore();
            }
            // 绘制完上一行后将offsetY移动到下一行
            offsetY += this.fontSize + this.lineSpace;
        }

        if (DEBUG) {
            debugBorder(ctx);
        }
    }

    /**
     * 绘制辅助线
     *
     * @param ctx drawing context
     */
    private void debugBorder(DrawContext ctx) {

        double width = canvas.getWidth();
        double height = canvas.getHeight();

        ctx.save();
        double lineWidth = 0.353d;
        ctx.setLineDash(1.5d, 1.5d);
        ctx.setLineWidth(lineWidth);
        ctx.setGlobalAlpha(0.53);
        ctx.strokeStyle = "rgb(255,0,0)";
        ctx.moveTo(0, 0);
        ctx.lineTo(width, height);
        ctx.moveTo(width, 0);
        ctx.lineTo(0, height);
        ctx.rect(0, 0, width, height);
        ctx.stroke();

        ctx.restore();
    }

    /**
     * 获取单元格文字内容
     *
     * @return 单元格文字内容
     */
    public String getValue() {
        return value;
    }

    /**
     * 设置单元格文字内容
     *
     * @param value 单元格文字内容
     * @return this
     */
    public CellContentDrawer setValue(String value) {
        this.value = value;
        return this;
    }

    /**
     * set image
     *
     * @param imgPath image path, supports png, jpg, jpeg, gif, bmp formats
     * @param w       imagewidth，单位：毫米
     * @param h       imageheight，单位：毫米
     * @return this
     */
    public CellContentDrawer setValue(Path imgPath, double w, double h) {
        this.img = new Img(imgPath, w, h);
        return this;
    }

    /**
     * set image
     * <p>
     * imagewidth与height通过 {@link DrawContext#mm(int)} } 方法转换为毫米
     *
     * @param imgPath image path, supports png, jpg, jpeg, gif, bmp formats
     * @return this
     * @throws IOException image加载异常
     */
    public CellContentDrawer setValue(Path imgPath) throws IOException {
        this.img = new Img(imgPath, 0, 0);
        return this;
    }

    /**
     * 获取单元格颜色
     *
     * @return 单元格颜色，格式：#000000、rgb(0,0,0)、rgba(0,0,0,1)
     */
    public String getColor() {
        return color;
    }

    /**
     * 设置单元格颜色
     *
     * @param color 单元格颜色，格式：#000000、rgb(0,0,0)、rgba(0,0,0,1)
     * @return this
     */
    public CellContentDrawer setColor(String color) {
        if (color == null || color.isEmpty()) {
            throw new IllegalArgumentException("颜色(color)不能为空");
        }
        this.color = color;
        return this;
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
     * @param fontName font name，仅支持系统安装font，且不会嵌入到OFD中。
     * @return this
     */
    public CellContentDrawer setFontName(String fontName) {
        this.fontName = fontName;
        return this;
    }

    /**
     * get font size
     *
     * @return 字号，默认：0.353 （单位：毫米）
     */
    public double getFontSize() {
        return fontSize;
    }

    /**
     * set font size
     *
     * @param fontSize 字号，默认：3（单位：毫米）
     * @return this
     */
    public CellContentDrawer setFontSize(double fontSize) {
        if (fontSize <= 0) {
            throw new IllegalArgumentException("字号(fontSize)必须大于0");
        }
        this.fontSize = fontSize;
        return this;
    }

    /**
     * 获取单元格的Canvas对象
     *
     * @return 单元格的Canvas对象
     */
    public Canvas getCanvas() {
        return canvas;
    }

    /**
     * 获取 文字对齐方式
     *
     * @return 文字对齐方式，默认：左对齐
     */
    public TextAlign getTextAlign() {
        return textAlign;
    }

    /**
     * 设置 文字对齐方式
     *
     * @param textAlign 文字对齐方式
     * @return this
     */
    public CellContentDrawer setTextAlign(TextAlign textAlign) {
        this.textAlign = textAlign;
        return this;
    }

    /**
     * 获取 文字垂直方向浮动方式
     *
     * @return 文字垂直方向浮动方式，默认：center {@link VerticalAlign#center}
     */
    public VerticalAlign getVerticalAlign() {
        return verticalAlign;
    }

    /**
     * 设置 文字垂直方向浮动方式
     *
     * @param verticalAlign 文字垂直方向浮动方式
     * @return this
     */
    public CellContentDrawer setVerticalAlign(VerticalAlign verticalAlign) {
        this.verticalAlign = verticalAlign;
        return this;
    }

    /**
     * 获取 行间距
     *
     * @return 行间距，默认值 0.6mm
     */
    public Double getLineSpace() {
        return lineSpace;
    }

    /**
     * 设置 行间距
     *
     * @param lineSpace 行间距
     * @return this
     */
    public CellContentDrawer setLineSpace(Double lineSpace) {
        this.lineSpace = lineSpace;
        return this;
    }

    /**
     * @return whether bold, default: not bold
     * @deprecated typo — use {@link #getBold()} instead
     * whether bold
     */
    @Deprecated
    public Boolean getBlob() {
        return bold;
    }

    /**
     * @param bolb 是否加粗
     * @return this
     * @deprecated 单词错误 {@link #setBold(Boolean)}
     * <p>
     * set whether bold
     */
    @Deprecated
    public CellContentDrawer setBlob(Boolean bolb) {
        this.bold = bolb;
        return this;
    }

    /**
     * whether bold
     *
     * @return whether bold, default: not bold
     */
    public Boolean getBold() {
        return bold;
    }

    /**
     * set whether bold
     *
     * @param bold whether bold
     * @return this
     */
    public CellContentDrawer setBold(Boolean bold) {
        this.bold = bold;
        return this;
    }

    /**
     * whether italic
     *
     * @return true - 斜体、false - 正常
     */
    public Boolean getItalic() {
        return italic;
    }

    /**
     * set whether italic
     *
     * @param italic 是否斜体，true - 斜体、false - 正常
     * @return this
     */
    public CellContentDrawer setItalic(Boolean italic) {
        this.italic = italic;
        return this;
    }

    /**
     * 获取 文字之间的间距
     *
     * @return 文字之间的间距，默认为：0
     */
    public Double getLetterSpacing() {
        return letterSpacing;
    }

    /**
     * 设置 文字之间的间距
     *
     * @param letterSpacing 文字之间的间距，可以为负数，默认为：0。
     * @return this
     */
    public CellContentDrawer setLetterSpacing(Double letterSpacing) {
        this.letterSpacing = letterSpacing;
        return this;
    }

    /**
     * 获取image path
     *
     * @return image path，可能为空。
     */
    public Path getImgPath() {
        if (img == null) {
            return null;
        }
        return img.path;
    }

    /**
     * 获取imageheight
     *
     * @return imageheight，可能为0。
     */
    public double getImgWidth() {
        if (img == null) {
            return 0;
        }
        return img.width;
    }

    /**
     * 获取imagewidth
     *
     * @return imagewidth，可能为0。
     */
    public double getImgHeight() {
        if (img == null) {
            return 0;
        }
        return img.height;
    }

    /**
     * 设置是否开启下划线
     *
     * @param underline true - 启下划线，false - 禁用下划线
     * @return this
     */
    public CellContentDrawer setUnderline(boolean underline) {
        this.underline = underline;
        return this;
    }

    /**
     * 获取是否开启下划线
     *
     * @return true - 启下划线，false - 不启用下划线
     */
    public boolean getUnderline() {
        return this.underline;
    }

    /**
     * 设置是否开启删除线
     *
     * @param deleteLine true - 启删除线，false - 禁用删除线
     * @return this
     */
    public CellContentDrawer setDeleteLine(boolean deleteLine) {
        this.deleteLine = deleteLine;
        return this;
    }

    /**
     * 获取是否开启删除线
     *
     * @return true - 启删除线，false - 禁用删除线
     */
    public boolean getDeleteLine() {
        return this.deleteLine;
    }

    /**
     * get font width
     *
     * @return fontwidth，遵循 CSS3标准，可选值为 normal | bold | bolder | lighter | 100 | 200 | 300 | 400 | 500 | 600 | 700 | 800 | 900
     */
    public String getFontWeight() {
        return fontWeight;
    }

    /**
     * set font width
     *
     * @param fontWeight fontwidth，应遵循 CSS3标准，可选值为 normal | bold | bolder | lighter | 100 | 200 | 300 | 400 | 500 | 600 | 700 | 800 | 900
     */
    public void setFontWeight(String fontWeight) {
        this.fontWeight = fontWeight;
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
    public CellContentDrawer setFont(String fontName, Path fontPath) {
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

}

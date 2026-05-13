package org.ofdrw.layout.element.canvas;


import java.io.IOException;
import java.nio.file.Path;

/**
 * 单元对象
 * <p>
 * 绘制行为详见渲染器：{@link org.ofdrw.layout.element.canvas.CellContentDrawer}
 *
 * @author Quan Guanyu
 * @since 2023-11-21 19:22:28
 */
public class Cell extends Canvas {

    /**
     * 单元格内容绘制器
     */
    private final CellContentDrawer cellDrawer;

    /**
     * 创建单元对象
     *
     * @param width  width (unit: mm)
     * @param height height (unit: mm)
     */
    public Cell(Double width, Double height) {
        super(width, height);
        this.cellDrawer = new CellContentDrawer(this);
        this.setDrawer(this.cellDrawer);
    }

    /**
     * 创建单元对象
     *
     * @param x x坐标（单位：毫米mm）
     * @param y y坐标（单位：毫米mm）
     * @param w width（单位：毫米mm）
     * @param h height（单位：毫米mm）
     */
    public Cell(double x, double y, double w, double h) {
        super(x, y, w, h);
        this.cellDrawer = new CellContentDrawer(this);
        this.setDrawer(this.cellDrawer);
    }

    /**
     * 设置单元格内容绘制器
     *
     * @param drawer 新的绘制器
     * @return this
     */
    @Override
    public Cell setDrawer(Drawer drawer) {
        if (!(drawer instanceof CellContentDrawer)) {
            throw new IllegalArgumentException("Cell的绘制器必须是CellContentDrawer");
        }
        super.setDrawer(drawer);
        return this;
    }

    /**
     * 获取单元格内容绘制器
     *
     * @return 单元格内容绘制器
     */
    @Override
    public CellContentDrawer getDrawer() {
        return cellDrawer;
    }


    /**
     * 获取单元格文字内容
     *
     * @return 单元格文字内容
     */
    public String getValue() {
        return cellDrawer.getValue();
    }

    /**
     * 设置单元格文字内容
     *
     * @param value 单元格文字内容
     * @return this
     */
    public Cell setValue(String value) {
        this.cellDrawer.setValue(value);
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
    public Cell setValue(Path imgPath, double w, double h) {
        this.cellDrawer.setValue(imgPath, w, h);
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
    public Cell setValue(Path imgPath) throws IOException {
        this.cellDrawer.setValue(imgPath);
        return this;
    }

    /**
     * 获取单元格颜色
     *
     * @return 单元格颜色，格式：#000000、rgb(0,0,0)、rgba(0,0,0,1)
     */
    public String getColor() {
        return cellDrawer.getColor();
    }

    /**
     * 设置单元格颜色
     *
     * @param color 单元格颜色，格式：#000000、rgb(0,0,0)、rgba(0,0,0,1)
     * @return this
     */
    public Cell setColor(String color) {
        if (color == null || color.isEmpty()) {
            throw new IllegalArgumentException("颜色(color)不能为空");
        }
        this.cellDrawer.setColor(color);
        return this;
    }

    /**
     * get font name
     *
     * @return font name
     */
    public String getFontName() {
        return this.cellDrawer.getFontName();
    }

    /**
     * set font name
     *
     * @param fontName font name，仅支持系统安装font，且不会嵌入到OFD中。
     * @return this
     */
    public Cell setFontName(String fontName) {
        this.cellDrawer.setFontName(fontName);
        return this;
    }

    /**
     * 设置单元格使用的外部font
     * <p>
     * Note: OFDRW does not provide any font subsetting; your font file will be added directly to the OFD file, which may increase the file size significantly.
     *
     * @param fontName font name, e.g. "Source Han Serif"
     * @param fontPath path to the font file
     * @return this
     */
    public Cell setFont(String fontName, Path fontPath) {
        this.cellDrawer.setFont(fontName, fontPath);
        return this;
    }

    /**
     * get font size
     *
     * @return 字号，默认：0.353 （单位：毫米）
     */
    public double getFontSize() {
        return this.cellDrawer.getFontSize();
    }

    /**
     * set font size
     *
     * @param fontSize 字号，默认：3（单位：毫米）
     * @return this
     */
    public Cell setFontSize(double fontSize) {
        this.cellDrawer.setFontSize(fontSize);
        return this;
    }

    /**
     * 获取 文字对齐方式
     *
     * @return 文字对齐方式，默认：左对齐
     */
    public TextAlign getTextAlign() {
        return this.cellDrawer.getTextAlign();
    }

    /**
     * 设置 文字对齐方式
     *
     * @param textAlign 文字对齐方式
     * @return this
     */
    public Cell setTextAlign(TextAlign textAlign) {
        this.cellDrawer.setTextAlign(textAlign);
        return this;
    }

    /**
     * 获取 文字垂直方向浮动方式
     *
     * @return 文字垂直方向浮动方式，默认：center {@link VerticalAlign#center}
     */
    public VerticalAlign getVerticalAlign() {
        return this.cellDrawer.getVerticalAlign();
    }

    /**
     * 设置 文字垂直方向浮动方式
     *
     * @param verticalAlign 文字垂直方向浮动方式
     * @return this
     */
    public Cell setVerticalAlign(VerticalAlign verticalAlign) {
        this.cellDrawer.setVerticalAlign(verticalAlign);
        return this;
    }

    /**
     * 获取 行间距
     *
     * @return 行间距，默认值 0.6mm
     */
    public Double getLineSpace() {
        return this.cellDrawer.getLineSpace();
    }

    /**
     * 设置 行间距
     *
     * @param lineSpace 行间距
     * @return this
     */
    public Cell setLineSpace(Double lineSpace) {
        this.cellDrawer.setLineSpace(lineSpace);
        return this;
    }

    /**
     * @return whether bold, default: not bold
     * @deprecated typo — use {@link #getBold()} instead
     * whether bold
     */
    @Deprecated
    public Boolean getBlob() {
        return this.cellDrawer.getBlob();
    }

    /**
     * @param blob 是否加粗
     * @return this
     * @deprecated typo — use {@link #getBold()} instead
     * set whether bold
     */
    @Deprecated
    public Cell setBlob(Boolean blob) {
        this.cellDrawer.setBlob(blob);
        return this;
    }

    /**
     * whether bold
     *
     * @return whether bold, default: not bold
     */
    public Boolean getBold() {
        return this.cellDrawer.getBold();
    }

    /**
     * set whether bold
     *
     * @param blob 是否加粗
     * @return this
     */
    public Cell setBold(Boolean blob) {
        this.cellDrawer.setBold(blob);
        return this;
    }


    /**
     * whether italic
     *
     * @return true - 斜体、false - 正常
     */
    public Boolean getItalic() {
        return this.cellDrawer.getItalic();
    }

    /**
     * set whether italic
     *
     * @param italic 是否斜体，true - 斜体、false - 正常
     * @return this
     */
    public Cell setItalic(Boolean italic) {
        this.cellDrawer.setItalic(italic);
        return this;
    }

    /**
     * 获取 文字之间的间距
     *
     * @return 文字之间的间距，默认为：0
     */
    public Double getLetterSpacing() {
        return this.cellDrawer.getLetterSpacing();
    }

    /**
     * 设置 文字之间的间距
     *
     * @param letterSpacing 文字之间的间距，可以为负数，默认为：0。
     * @return this
     */
    public Cell setLetterSpacing(Double letterSpacing) {
        this.cellDrawer.setLetterSpacing(letterSpacing);
        return this;
    }

    /**
     * 设置是否开启下划线
     *
     * @param underline true - 启下划线，false - 禁用下划线
     * @return this
     */
    public Cell setUnderline(boolean underline) {
        this.cellDrawer.setUnderline(underline);
        return this;
    }

    /**
     * 获取是否开启下划线
     *
     * @return true - 启下划线，false - 不启用下划线
     */
    public boolean getUnderline() {
        return this.cellDrawer.getUnderline();
    }

    /**
     * 设置是否开启删除线
     *
     * @param deleteLine true - 启删除线，false - 禁用删除线
     * @return this
     */
    public Cell setDeleteLine(boolean deleteLine) {
        this.cellDrawer.setDeleteLine(deleteLine);
        return this;
    }

    /**
     * 获取是否开启删除线
     *
     * @return true - 启删除线，false - 禁用删除线
     */
    public boolean getDeleteLine() {
        return this.cellDrawer.getDeleteLine();
    }

    /**
     * 获取image path
     *
     * @return image path，可能为空。
     */
    public Path getImgPath() {
        return this.cellDrawer.getImgPath();
    }

    /**
     * 获取imageheight
     *
     * @return imageheight，可能为0。
     */
    public double getImgWidth() {
        return this.cellDrawer.getImgWidth();
    }

    /**
     * 获取imagewidth
     *
     * @return imagewidth，可能为0。
     */
    public double getImgHeight() {
        return this.cellDrawer.getImgHeight();
    }

    /**
     * get font width
     *
     * @return fontwidth，遵循 CSS3标准，可选值为 normal | bold | bolder | lighter | 100 | 200 | 300 | 400 | 500 | 600 | 700 | 800 | 900
     */
    public String getFontWeight() {
        return this.cellDrawer.getFontWeight();
    }

    /**
     * set font width
     *
     * @param fontWeight width，应遵循 CSS3标准: normal | bold | bolder | lighter | 100 | 200 | 300 | 400 | 500 | 600 | 700 | 800 | 900
     * @return this
     */
    public Cell setFontWeight(String fontWeight) {
        this.cellDrawer.setFontWeight(fontWeight);
        return this;
    }
}

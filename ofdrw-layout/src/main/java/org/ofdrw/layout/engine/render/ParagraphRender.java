package org.ofdrw.layout.engine.render;

import org.ofdrw.core.basicStructure.pageObj.layer.block.CT_PageBlock;
import org.ofdrw.core.basicStructure.pageObj.layer.block.PathObject;
import org.ofdrw.core.basicStructure.pageObj.layer.block.TextObject;
import org.ofdrw.core.basicType.ST_Box;
import org.ofdrw.core.basicType.ST_ID;
import org.ofdrw.core.basicType.ST_Loc;
import org.ofdrw.core.graph.pathObj.AbbreviatedData;
import org.ofdrw.core.pageDescription.color.color.CT_Color;
import org.ofdrw.core.text.TextCode;
import org.ofdrw.core.text.text.Weight;
import org.ofdrw.layout.element.*;
import org.ofdrw.layout.engine.ResManager;

import java.io.IOException;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * segment落渲染器
 * <p>
 * {@link org.ofdrw.layout.element.Paragraph} 的渲染器
 *
 * @author Quan Guanyu
 * @since 2020-03-24 04:31:37
 */
public class ParagraphRender implements Processor {
    /**
     * 执行segment落渲染
     *
     * @param pageLoc    absolute path of the page in the virtual container.
     * @param layer      the layer where the image will be placed
     * @param resManager resource manager
     * @param e          segment落对象
     * @param maxUnitID  maximum element ID provider
     * @throws RenderException rendering error occurred
     */
    @Override
    public void render(ST_Loc pageLoc, CT_PageBlock layer, ResManager resManager, Div e, AtomicInteger maxUnitID) throws RenderException{
        if (e instanceof Paragraph) {
            render(layer, resManager, (Paragraph) e, maxUnitID);
        }
    }

    /**
     * 将segment落渲染到layer上
     *
     * @param layer      layer
     * @param resManager resource manager
     * @param e          segment落对象
     * @param maxUnitID  object ID提供者
     */
    public static void render(CT_PageBlock layer, ResManager resManager, Paragraph e, AtomicInteger maxUnitID) {
        if (e == null) {
            return;
        }
        LinkedList<TxtLineBlock> lines = e.getLines();
        // segment落中没有任何内容，那么跳过不渲染
        if (lines == null || lines.isEmpty()) {
            // 直接加入到虚拟页面的segment落对象是经过预处理的，所以此处尝试预处理。
            e.doPrepare(e.getWidth() + e.widthPlus());
            lines = e.getLines();
            if (lines == null || lines.isEmpty()) {
                // 如果还是没有行，那么是个空segment落，跳过
                return;
            }
        }

        // 可容纳元素的总height
        Double containerHeight = e.getHeight();
        /*
        每一行左上角坐标
         */
        double lineTopX = e.getX() + e.getMarginLeft() + e.getBorderLeft() + e.getPaddingLeft();
        double offsetY = e.getY() + e.getMarginTop() + e.getBorderTop() + e.getPaddingTop();
        // 已经写入的元素height统计
        double hCount = 0;
        // 渲染每一行文字
        for (TxtLineBlock txtLine : lines) {
            // 行内的X偏移量为行开头
            double offsetX;
            // 根据浮动方式确定起始字符坐标
            switch (txtLine.getTextAlign()) {
                case right:
                    offsetX = lineTopX + (txtLine.getLineMaxAvailableWidth() - txtLine.getWidth());
                    break;
                case center:
                    offsetX = lineTopX + (txtLine.getLineMaxAvailableWidth() - txtLine.getWidth()) / 2;
                    break;
                case left:
                default:
                    offsetX = lineTopX;
                    break;
            }

            // 一行内的所有Span的图元height都为行height（最高文字height + 行间距）
            double h = txtLine.getHeight();
            if (hCount + h > containerHeight) {
                // 如果文字内容height大于容纳文字的容器height，那么将该部分内容舍弃，不渲染。
                break;
            }

            // 遍历行内的每个Span
            for (Span s : txtLine.getInlineSpans()) {
                // 文字图元width
                double w = s.blockSize().getWidth();
                if (w == 0) {
                    // 空行
                    continue;
                }
                if (s instanceof PlaceholderSpan) {
                    // ignored占位符的渲染只进行坐标偏移
                    offsetX += w;
                    continue;
                }

                // 将font加入到资源中
                ST_ID id = null;
                try {
                    id = resManager.addFont(s.getFont());
                } catch (IOException ex) {
                    throw new RenderException("渲染异常，font复制失败：" + ex.getMessage(), ex);
                }
                // 新建font object
                TextObject txtObj = new TextObject(maxUnitID.incrementAndGet());
                ST_Box boundary = new ST_Box(offsetX, offsetY, w, h);
                txtObj.setBoundary(boundary)
                        // 设置fontID
                        .setFont(id.ref())
                        // 设置font大小
                        .setSize(s.getFontSize());
                // set font weight
                if (s.getWeight() != null) {
                    txtObj.setWeight(s.getWeight());
                } else if (s.isBold()) {
                    txtObj.setWeight(Weight.W_800);
                }

                // 是否是斜体
                if (s.isItalic()) {
                    txtObj.setItalic(true);
                }
                // 是否填充，默认为true表示填充
                if (!s.isFill()) {
                    txtObj.setFill(false);
                }
                // 设置font color，默认颜色为黑色
                int[] color = s.getColor();
                if (color != null && color.length >= 3) {
                    txtObj.setFillColor(CT_Color.rgb(color));
                }
                // 创建OFD文字定位对象
                Double offset = txtLine.getMaxSpanHeight();
                TextCode tcSTTxt = new TextCode()
                        // 定位点位于文字的左下角，文字文字Y偏移量为该行最高文字的height
                        .setCoordinate(0d, offset)
                        .setContent(s.getText());
                Double[] deltaX = s.getDeltaX();
                if (deltaX.length > 0) {
                    // 如果多余一个字符那么需要设置字符偏移量
                    tcSTTxt.setDeltaX(deltaX);
                }
                // 加入到字符对象中
                txtObj.addTextCode(tcSTTxt);
                if (e.getOpacity() != null) {
                    // graphic element transparency
                    txtObj.setAlpha((int) (e.getOpacity() * 255));
                }
                // 将text object加入到layer
                layer.addPageBlock(txtObj);
                // 是否contains下划线
                if (s.isUnderline()) {
                    ST_ID underlineId = new ST_ID(maxUnitID.incrementAndGet());
                    // 构造下划线
                    double underlineOffset = offset + s.getUnderlineOffset();
                    double underlineWidth = s.getUnderlineWidth();
                    if (underlineWidth == 0) {
                        // 0.05 比例系数为经验值
                        underlineWidth = s.getFontSize() * 0.05;
                    }
                    PathObject underline = drawUnderline(underlineId, boundary, color, underlineOffset, underlineWidth);
                    if (e.getOpacity() != null) {
                        // graphic element transparency
                        underline.setAlpha((int) (e.getOpacity() * 255));
                    }
                    // 加入到text object的上方
                    layer.addPageBlock(underline);
                }
                // 计算行内下一个图元的X coordinate
                offsetX += w;
            }
            offsetY += h;
            hCount += h;
        }
    }


    /**
     * 绘制文字的下划线
     *
     * @param id       下划线ID
     * @param boundary 绘制下划线的区域
     * @param color    下划线颜色 [R, G, B]
     * @param offset   在绘制区域内Y的偏移量
     * @param width    下划线线宽
     * @return 路径对象
     */
    private static PathObject drawUnderline(ST_ID id, ST_Box boundary, int[] color, double offset, double width) {
        PathObject res = new PathObject(id);
        if (color != null && color.length >= 3) {
            res.setStrokeColor(CT_Color.rgb(color));
        }
        res.setBoundary(boundary)
                .setAbbreviatedData(new AbbreviatedData().M(0, offset).lineTo(boundary.getWidth(), offset))
                .setLineWidth(width);
        return res;
    }
}

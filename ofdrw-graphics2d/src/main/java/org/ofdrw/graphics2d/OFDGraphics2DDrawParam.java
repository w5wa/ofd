package org.ofdrw.graphics2d;

import org.ofdrw.core.basicType.ST_Array;
import org.ofdrw.core.basicType.ST_Box;
import org.ofdrw.core.basicType.ST_Pos;
import org.ofdrw.core.basicType.ST_RefID;
import org.ofdrw.core.pageDescription.CT_GraphicUnit;
import org.ofdrw.core.pageDescription.color.color.*;
import org.ofdrw.core.pageDescription.drawParam.CT_DrawParam;
import org.ofdrw.core.pageDescription.drawParam.LineCapType;
import org.ofdrw.core.pageDescription.drawParam.LineJoinType;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;

/**
 * drawing parameters上下文
 *
 * @author Quan Guanyu
 * @since 2023-1-30 21:37:36
 */
public class OFDGraphics2DDrawParam {

    /**
     * document context
     */
    private final OFDGraphicsDocument docCtx;

    /**
     * 工作区大小
     */
    ST_Box area;

    /**
     * font
     */
    Font font;

    /**
     * 引用对象
     */
    ST_RefID ref;

    /**
     * AWT 描边属性（颜色、描边样式）
     */
    BasicStroke gStroke;

    /**
     * AWT 画笔预设
     */
    Paint gColor;

    /**
     * clipping area
     */
    java.awt.geom.Area clip;


    /**
     * AWTtransformation matrix
     */
    AffineTransform ctm;

    /**
     * 背景色
     */
    Color gBackground;

    /**
     * 前景色
     */
    Color gForeground;

    /**
     * 渲染器信息
     * <p>
     * this attribute is retained for AWT interface compatibility only; it has no actual use.
     */
    RenderingHints hints;

    /**
     * 设置像素合并方式
     * <p>
     * this attribute is retained for AWT interface compatibility only; it has no actual use.
     */
    Composite composite;

    /**
     * 文字drawing context
     */
    FontRenderContext fontRenderCtx;

    /**
     * 创建drawing parameters
     *
     * @param docCtx document context
     * @param area   工作区大小
     */
    public OFDGraphics2DDrawParam(OFDGraphicsDocument docCtx, ST_Box area) {
        this.docCtx = docCtx;
        this.gStroke = new BasicStroke(0.353f);
        // 默认 stroke color为黑色
        this.gColor = new Color(0, 0, 0);
        this.gBackground = new Color(255, 255, 255);
        this.gForeground = new Color(0, 0, 0);

        this.ctm = new AffineTransform();
        this.ref = null;
        this.clip = null;

        this.hints = new RenderingHints(null);
        this.composite = AlphaComposite.SrcOver;

        this.font = new Font("sanserif", Font.PLAIN, 3);
        if (area != null) {
            this.area = area.clone();
        }

    }

    /**
     * 创建drawing parameters
     * <p>
     * 若您需要使用该方法，请手动设置工作区 {@link #area} 否则默认为A4大小
     *
     * @param docCtx document context
     * @deprecated {@link OFDGraphics2DDrawParam#OFDGraphics2DDrawParam(OFDGraphicsDocument, ST_Box)}
     */
    @Deprecated
    public OFDGraphics2DDrawParam(OFDGraphicsDocument docCtx) {
        this(docCtx, new ST_Box(0, 0, 210d, 297d));
    }


    /**
     * 构造drawing parameters，并追加到文档中
     * <p>
     * 如果存在缓存优先使用缓存中的drawing parameters
     *
     * @return drawing parameters
     */
    ST_RefID makeDrawParam() {
        if (ref != null) {
            return ref;
        }

        CT_DrawParam param = new CT_DrawParam();
        // set stroke attributes
        setStrokeParam(param);

        // 设置颜色和填充
        setColorParam(param);

        // 设置线宽
        double lineWidth = gStroke.getLineWidth();
        double scale = 1;
        if (!this.ctm.isIdentity()) {
            scale = Math.min(Math.abs(this.ctm.getScaleX()), Math.abs(this.ctm.getScaleY()));
        }
        param.setLineWidth(lineWidth * scale);

        this.ref = docCtx.addDrawParam(param).ref();
        return ref;
    }

    /**
     * 设置颜色和填充
     *
     * @param param drawing parameters
     */
    private void setColorParam(CT_DrawParam param) {
        CT_Color ctColor = null;
        if (this.gColor instanceof Color) {
            final Color c = (Color) this.gColor;
            ctColor = CT_Color.rgb(c.getRed(), c.getGreen(), c.getBlue());
            int alpha = c.getAlpha();
            if (alpha != 255) {
                ctColor.setAlpha(alpha);
            }
        } else if (this.gColor instanceof LinearGradientPaint) {
            // 线性渐变
            final LinearGradientPaint lgp = (LinearGradientPaint) this.gColor;

            ctColor = new CT_Color();
            CT_AxialShd axialShd = new CT_AxialShd();

            // 轴线起点
            axialShd.setStartPoint(ST_Pos.getInstance(lgp.getStartPoint().getX(), lgp.getStartPoint().getY()));
            // 轴线终点
            axialShd.setEndPoint(ST_Pos.getInstance(lgp.getEndPoint().getX(), lgp.getEndPoint().getY()));

            // 设置颜色segment以及分布
            Color[] colors = lgp.getColors();
            float[] fractions = lgp.getFractions();
            for (int i = 0; i < colors.length; i++) {
                CT_Color cc = CT_Color.rgb(colors[i].getRed(), colors[i].getGreen(), colors[i].getBlue());
                int alpha = colors[i].getAlpha();
                if (alpha != 255) {
                    cc.setAlpha(alpha);
                }
                axialShd.addSegment(new Segment((double) fractions[i], cc));
            }

            // set gradient drawing mode
            switch (lgp.getCycleMethod()) {
                case NO_CYCLE:
                    axialShd.setMapType(MapType.Direct);
                    break;
                case REPEAT:
                    axialShd.setMapType(MapType.Repeat);
                    break;
                case REFLECT:
                    axialShd.setMapType(MapType.Reflect);
                    break;
            }

            ctColor.setColor(axialShd);
        } else if (this.gColor instanceof RadialGradientPaint) {
            // 径向渐变
            final RadialGradientPaint rgp = (RadialGradientPaint) this.gColor;

            ctColor = new CT_Color();
            CT_RadialShd radialShd = new CT_RadialShd();
            // 设置颜色segment以及分布
            Color[] colors = rgp.getColors();
            float[] fractions = rgp.getFractions();
            for (int i = 0; i < colors.length; i++) {
                CT_Color cc = CT_Color.rgb(colors[i].getRed(), colors[i].getGreen(), colors[i].getBlue());
                int alpha = colors[i].getAlpha();
                if (alpha != 255) {
                    cc.setAlpha(alpha);
                }
                radialShd.addSegment(new Segment((double) fractions[i], cc));
            }
            // 设置渐变绘制方式
            switch (rgp.getCycleMethod()) {
                case NO_CYCLE:
                    radialShd.setMapType(MapType.Direct);
                    break;
                case REFLECT:
                    radialShd.setMapType(MapType.Reflect);
                    break;
                case REPEAT:
                    radialShd.setMapType(MapType.Repeat);
                    break;
            }
            // 设置半径
            radialShd.setEndRadius((double) rgp.getRadius());
            // 设置 起始 椭圆中心点
            ST_Pos startPoint = ST_Pos.getInstance(rgp.getCenterPoint().getX(), rgp.getCenterPoint().getY());
            radialShd.setStartPoint(startPoint);
            // 设置 结束 椭圆中心点
            ST_Pos endPoint = ST_Pos.getInstance(rgp.getFocusPoint().getX(), rgp.getFocusPoint().getY());
            radialShd.setEndPoint(endPoint);
            ctColor.setColor(radialShd);
        } else if (this.gColor instanceof GradientPaint) {
            final GradientPaint gp = (GradientPaint) this.gColor;
            ctColor = new CT_Color();
            CT_AxialShd axialShd = new CT_AxialShd();

            // 轴线起点
            axialShd.setStartPoint(ST_Pos.getInstance(gp.getPoint1().getX(), gp.getPoint1().getY()));
            // 轴线终点
            axialShd.setEndPoint(ST_Pos.getInstance(gp.getPoint2().getX(), gp.getPoint2().getY()));

            // set gradient drawing mode
            if (gp.isCyclic()) {
                axialShd.setMapType(MapType.Repeat);
            }
            // 设置起点终点
            axialShd.addSegment(new Segment(CT_Color.rgb(gp.getColor1().getRed(), gp.getColor1().getGreen(), gp.getColor1().getBlue())));
            axialShd.addSegment(new Segment(CT_Color.rgb(gp.getColor2().getRed(), gp.getColor2().getGreen(), gp.getColor2().getBlue())));

            ctColor.setColor(axialShd);
        }

        if (ctColor != null) {
            // 同时set fill color和stroke color
            param.setFillColor(ctColor);
            param.setStrokeColor(ctColor);
        }
    }

    /**
     * 设置描边参数
     *
     * @param param drawing parameters上下文
     */
    private void setStrokeParam(CT_DrawParam param) {
        // 线条连接样式
        switch (gStroke.getLineJoin()) {
            case BasicStroke.JOIN_BEVEL:
                param.setJoin(LineJoinType.Bevel);
                break;
            case BasicStroke.JOIN_MITER:
                param.setJoin(LineJoinType.Miter);
                break;
            case BasicStroke.JOIN_ROUND:
                param.setJoin(LineJoinType.Round);
                break;
            default:
                param.setJoin(null);
                break;
        }

        // 线width
        if (gStroke.getLineWidth() > 0) {
            param.setLineWidth((double) gStroke.getLineWidth());
        }

        // 线条虚线重复样式
        final float[] dashArray = gStroke.getDashArray();
        if (dashArray != null && dashArray.length > 0) {
            final ST_Array pattern = new ST_Array();
            for (float v : dashArray) {
                pattern.add(Float.toString(v));
            }
            param.setDashPattern(pattern);
        } else {
            param.setDashPattern(null);
        }

        // 线端点样式
        switch (gStroke.getEndCap()) {
            case BasicStroke.CAP_BUTT:
                param.setCap(LineCapType.Butt);
                break;
            case BasicStroke.CAP_ROUND:
                param.setCap(LineCapType.Round);
                break;
            case BasicStroke.CAP_SQUARE:
                param.setCap(LineCapType.Square);
                break;
            default:
                param.setCap(null);
        }

        // Join的截断值
        final float miterLimit = gStroke.getMiterLimit();
        if (miterLimit > 0) {
            param.setMiterLimit((double) miterLimit);
        } else {
            param.setMiterLimit(null);
        }
    }

    /**
     * set stroke attributes
     *
     * @param s 属性参数
     */
    public void setStroke(Stroke s) {
        // 清空引用缓存
        this.ref = null;
        if (s == null) {
            // 如果为空时设置为默认颜色黑色
            s = new BasicStroke(0.353f);
        }

        if (s instanceof BasicStroke) {
            this.gStroke = (BasicStroke) s;
        }
    }

    /**
     * 设置颜色
     *
     * @param paint 颜色对象
     */
    public void setColor(Paint paint) {
        if (this.gColor == paint) {
            return;
        }
        // 清空引用缓存
        this.ref = null;
        if (paint == null) {
            // 如果为空时设置为默认颜色黑色
            paint = new Color(0, 0, 0);
        }
        this.gColor = paint;
    }

    /**
     * 设置前景色
     *
     * @param c 前景色
     */
    public void setForeground(Color c) {
        this.gForeground = c;
        setColor(c);
    }

    /**
     * 在图元上应用drawing parameters配置
     * <p>
     * 包括： 描边、transformation matrix、裁剪区域、颜色
     *
     * @param target 目标图元
     * @deprecated {@link #makeDrawParam()}
     */
    @Deprecated
    public void apply(CT_GraphicUnit<?> target) {
//        if (ref == null) {
//            // 添加drawing parameters至文档资源中，并保存drawing parameters在document objectID引用
//            ref = docCtx.addDrawParam(this.pCache).ref();
//        }
//        target.setDrawParam(ref);
//
//        if (!this.ctm.isIdentity()) {
//            target.setCTM(trans(this.ctm));
//        }
//        if (clip != null) {
//            Clips clips = new Clips();
//            Area area = new Area();
//            CT_Path clipObj = new CT_Path().setAbbreviatedData(OFDShapes.path(clip));
//            clipObj.setFill(true);
//            // 裁剪区域不受所处图元影响，默认使用整个页面作为工作区
//            clipObj.setBoundary(this.area);
//            area.setClipObj(clipObj);
//            clips.addClip(new CT_Clip().addArea(area));
//            target.setClips(clips);
//        }
    }

    /**
     * 获取fontdrawing context
     * <p>
     * font绘制上线文使用hit中的drawing parameters进行控制
     * <p>
     * code from apache batik: org.apache.batik.ext.awt.g2d.AbstractGraphics2D#getFontRenderContext
     *
     * @return fontdrawing context
     */
    public FontRenderContext getFontRenderContext() {
        if (this.fontRenderCtx != null) {
            return this.fontRenderCtx;
        }
        // 抗锯齿
        Object antialiasingHint = hints.get(RenderingHints.KEY_TEXT_ANTIALIASING);
        boolean isAntialiased = true;
        if (antialiasingHint != RenderingHints.VALUE_TEXT_ANTIALIAS_ON &&
                antialiasingHint != RenderingHints.VALUE_TEXT_ANTIALIAS_DEFAULT) {
            // 如果抗锯齿没有被设置为 OFF 那么使用默认方式
            if (antialiasingHint != RenderingHints.VALUE_TEXT_ANTIALIAS_OFF) {
                antialiasingHint = hints.get(RenderingHints.KEY_ANTIALIASING);
                // 判断是否是默认的抗锯齿
                if (antialiasingHint != RenderingHints.VALUE_ANTIALIAS_ON &&
                        antialiasingHint != RenderingHints.VALUE_ANTIALIAS_DEFAULT) {
                    // Antialiasing was not requested. However, if it was not turned
                    // off explicitly, use it.
                    if (antialiasingHint == RenderingHints.VALUE_ANTIALIAS_OFF)
                        isAntialiased = false;
                }
            } else
                isAntialiased = false;

        }

        // 设置是否使用 FRACTIONALMETRICS
        boolean useFractionalMetrics = hints.get(RenderingHints.KEY_FRACTIONALMETRICS) != RenderingHints.VALUE_FRACTIONALMETRICS_OFF;

        this.fontRenderCtx = new FontRenderContext(new AffineTransform(),
                isAntialiased,
                useFractionalMetrics);
        return this.fontRenderCtx;
    }


    /**
     * 复制drawing parameters对象
     *
     * @return 复制的新对象
     */
    @Override
    public OFDGraphics2DDrawParam clone() {
        OFDGraphics2DDrawParam that = new OFDGraphics2DDrawParam(this.docCtx, this.area);
        that.gColor = this.gColor;
        that.gStroke = this.gStroke;
        that.gBackground = this.gBackground;
        that.gForeground = this.gForeground;

        that.ctm = new AffineTransform(this.ctm);
        that.clip = this.clip;
        that.ref = this.ref;

        that.hints = (RenderingHints) this.hints.clone();
        that.composite = this.composite;
        that.font = this.font;
        return that;
    }

    /**
     * 转为AWTtransformation matrix {@link AffineTransform} 为 OFD 类型transformation matrix{@link ST_Array}
     *
     * @param tx AWTtransformation matrix
     * @return OFD ST_Array
     */
    public static ST_Array trans(AffineTransform tx) {
      /*
            m00 m10 0    a b 0
            m01 m11 0  = c d 0
            m02 m12 1    e f 1
       */
        return new ST_Array(tx.getScaleX(), tx.getShearY(), tx.getShearX(), tx.getScaleY(), tx.getTranslateX(), tx.getTranslateY());
    }

}

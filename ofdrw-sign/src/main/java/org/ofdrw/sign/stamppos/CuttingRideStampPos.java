package org.ofdrw.sign.stamppos;

import org.ofdrw.core.basicStructure.pageObj.Page;
import org.ofdrw.core.basicType.ST_Box;
import org.ofdrw.core.basicType.ST_RefID;
import org.ofdrw.core.signatures.appearance.StampAnnot;
import org.ofdrw.reader.OFDReader;
import org.ofdrw.sign.SignIDProvider;

import java.util.ArrayList;
import java.util.List;

public class CuttingRideStampPos implements StampAppearance {
    /**
     * 默认骑缝章以右side边作为骑缝的位置
     */
    private Side side;

    /**
     * 图章整章width
     * <p>
     * unit: mm
     */
    private double width;

    /**
     * 图章整章height
     * <p>
     * unit: mm
     */
    private double height;

    /**
     * 图章在边上距离原地最近的边的偏移坐标
     * <p>
     * unit: mm
     * <p>
     * 默认center，为null
     */
    private Double offset = null;

    /**
     * 图章在边上的margin
     * <p>
     * unit: mm
     * <p>
     * 默认为0
     */
    private double margin = 0;

    /**
     * 两side对开时图章在左右两边的比例，List的size与骑缝章数量一致，若出现null则采用左右各0.5
     */
    private List<CuttingRatio> cuttingRatios;


    /**
     * specify the relative position of the seal on the edge
     *
     * @param side          specified edge where the seal is placed
     * @param offset        相对于原点最近的边的顶点位置，null则默认center
     * @param width         章width，单位毫米mm
     * @param height        章height，单位毫米mm
     * @param margin        页边距，单位毫米mm
     * @param cuttingRatios 两side对开时图章在左右两边的比例
     */
    public CuttingRideStampPos(Side side, Double offset, double width, double height, double margin, List<CuttingRatio> cuttingRatios) {
        this.side = side;
        this.width = width;
        this.height = height;
        this.offset = offset;
        this.margin = margin;
        this.cuttingRatios = cuttingRatios;
    }


    @Override
    public List<StampAnnot> getAppearance(OFDReader ctx, SignIDProvider idProvider) {
        // 总page number数
        int numPage = ctx.getNumberOfPages();
        List<StampAnnot> res = new ArrayList<>(numPage);
        if (side == Side.Right) {
            double itemWith = this.width * 0.5;
            for (int i = 0; i < numPage; i++) {
                Page page = ctx.getPage(i + 1);
                ST_Box pageSize = ctx.getPageSize(page);
                double x;
                ST_Box clip = null;

                if (numPage - 1 != i) {
                    if (cuttingRatios != null && cuttingRatios.size() > 0) {
                        itemWith = this.width * cuttingRatios.get(i).getLeft();
                    }
                    x = pageSize.getWidth() - itemWith - margin;
                    clip = new ST_Box(0, 0, itemWith, this.height);
                } else {
                    if (cuttingRatios != null && cuttingRatios.size() > 0) {
                        itemWith = this.width * cuttingRatios.get(i-1).getRight();
                    }
                    x = 0 - (this.width - itemWith) + margin;
                    clip = new ST_Box((this.width - itemWith), 0, itemWith, this.height);
                }
                double y;
                if (this.offset == null) {
                    y = pageSize.getHeight() / 2 - this.height / 2;
                } else {
                    y = this.offset;
                }
                ST_RefID ref = ctx.getPageObjectId(i + 1).ref();
                StampAnnot annot = new StampAnnot()
                        .setID(idProvider.incrementAndGet())
                        .setBoundary(new ST_Box(x, y, this.width, this.height))
                        .setPageRef(ref)
                        .setClip(clip);
                res.add(annot);
                if (0 != i && numPage - 1 != i) {
                    if (cuttingRatios != null && cuttingRatios.size() > 0) {
                        itemWith = this.width * cuttingRatios.get(i-1).getRight();
                    }
                    x = 0 - (this.width - itemWith) + margin;
                    clip = new ST_Box((this.width - itemWith), 0, itemWith, this.height);
                    annot = new StampAnnot()
                            .setID(idProvider.incrementAndGet())
                            .setBoundary(new ST_Box(x, y, this.width, this.height))
                            .setPageRef(ref)
                            .setClip(clip);
                    res.add(annot);
                }
            }
        } else if (side == Side.Left) {
            double itemWith = this.width * 0.5;
            for (int i = 0; i < numPage; i++) {
                Page page = ctx.getPage(i + 1);
                ST_Box pageSize = ctx.getPageSize(page);
                double x;
                ST_Box clip = null;

                if (numPage - 1 != i) {
                    if (cuttingRatios != null && cuttingRatios.size() > 0) {
                        itemWith = this.width * cuttingRatios.get(i).getLeft();
                    }
                    x = 0 - (this.width - itemWith) + margin;
                    clip = new ST_Box((this.width - itemWith), 0, itemWith, this.height);
                } else {
                    if (cuttingRatios != null && cuttingRatios.size() > 0) {
                        itemWith = this.width * cuttingRatios.get(i-1).getRight();
                    }
                    x = pageSize.getWidth() - itemWith - margin;
                    clip = new ST_Box(0, 0, itemWith, this.height);
                }
                double y;
                if (this.offset == null) {
                    y = pageSize.getHeight() / 2 - this.height / 2;
                } else {
                    y = this.offset;
                }
                ST_RefID ref = ctx.getPageObjectId(i + 1).ref();
                StampAnnot annot = new StampAnnot()
                        .setID(idProvider.incrementAndGet())
                        .setBoundary(new ST_Box(x, y, this.width, this.height))
                        .setPageRef(ref)
                        .setClip(clip);
                res.add(annot);
                if (0 != i && numPage - 1 != i) {
                    if (cuttingRatios != null && cuttingRatios.size() > 0) {
                        itemWith = this.width * cuttingRatios.get(i-1).getRight();
                    }
                    x = pageSize.getWidth() - itemWith - margin;
                    clip = new ST_Box(0, 0, itemWith, this.height);
                    annot = new StampAnnot()
                            .setID(idProvider.incrementAndGet())
                            .setBoundary(new ST_Box(x, y, this.width, this.height))
                            .setPageRef(ref)
                            .setClip(clip);
                    res.add(annot);
                }
            }
        }
        return res;
    }
}

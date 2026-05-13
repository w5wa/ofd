package org.ofdrw.layout.element;

import org.ofdrw.layout.Rectangle;


/**
 * 区域占位区块
 * <p>
 * 该element attribute与Div属性一致，也就是说可以绘制边框等内容。
 * <p>
 * 用于构造页面中一个用于容纳将来可能出现的页面元素的结构，该结构不做任何事情仅仅是占位。
 * 被添加到页面中的 区域占位区块 将会生成一个特别定位坐标到 AreaHolderBlocks.xml 文件，详见 {@link org.ofdrw.layout.areaholder.AreaHolderBlocks}
 * <p>
 * 绘制行为详见渲染器：{@link AreaHolderBlock}
 * <p>
 * 注意AreaHolderBlock将会受到 Border 与 Padding的影响，可绘制区域仅为去除了Border与Padding的内的区域。
 *
 * @author Quan Guanyu
 * @since 2023-10-28 12:24:06
 */
public class AreaHolderBlock extends Div<AreaHolderBlock> {

    /**
     * 占位area name
     * <p>
     * 用于唯一定位区域占位区块，请确保该名称在文档范围内唯一。
     */
    public String areaName;


    private AreaHolderBlock() {
    }


    /**
     * 创建AreaHolderBlock对象
     * <p>
     * AreaHolderBlock的width和height必须在创建时指定
     *
     * @param areaName area name，用于唯一定位区域占位区块，请确保该名称在文档范围内唯一。
     * @param width    width（单位：毫米mm）
     * @param height   height（单位：毫米mm）
     */
    public AreaHolderBlock(String areaName, Double width, Double height) {
        super(width, height);
        this.areaName = areaName;
    }

    /**
     * 在指定位置 创建AreaHolderBlock对象
     *
     * @param areaName area name，用于唯一定位区域占位区块，请确保该名称在文档范围内唯一。
     * @param x        画布左上角的x坐标
     * @param y        画布左上角的y坐标
     * @param w        画布的width
     * @param h        画布的height
     */
    public AreaHolderBlock(String areaName, double x, double y, double w, double h) {
        super(x, y, w, h);
        this.areaName = areaName;
    }

    /**
     * 获取 area name
     *
     * @return area name
     */
    public String getAreaName() {
        return areaName;
    }

    /**
     * 设置 area name
     *
     * @param areaName area name
     * @return this
     */
    public AreaHolderBlock setAreaName(String areaName) {
        this.areaName = areaName;
        return this;
    }

    /**
     * Canvas 不接受width重设
     */
    @Override
    public Rectangle doPrepare(Double widthLimit) {
        double w = this.getWidth() + widthPlus();
        double h = this.getHeight() + heightPlus();
        return new Rectangle(w, h);
    }


    /**
     * 获取element type名称
     * <p>
     * 用于标识element type，选择合适的处理器，自定义元素请重写该方法
     * <p>
     * 关联绘制器：{@link org.ofdrw.layout.engine.render.DivRender}
     *
     * @return element type名称
     */
    @Override
    public String elementType() {
        return "AreaHolderBlock";
    }
}

package org.ofdrw.layout.engine.render;

import org.ofdrw.core.basicStructure.pageObj.layer.block.CT_PageBlock;
import org.ofdrw.core.basicType.ST_Loc;
import org.ofdrw.layout.element.Div;
import org.ofdrw.layout.engine.ResManager;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 元素绘制器
 * <p>
 * 用于实现OFDRW元素到OFD图元的转换，并处理OFD虚拟容器以及资源管理。
 * <p>
 * 绘制器的选择由 {@link org.ofdrw.layout.engine.VPageParseEngine} 实现，您需要向 VPageParseEngine 通过名称注册绘制器。
 *
 * @author Quan Guanyu
 * @since 2024-5-27 19:22:48
 */
@FunctionalInterface
public interface Processor {

    /**
     * 处理OFDRW元素转换为OFD element
     *
     * @param pageLoc    页面路径
     * @param layer      the layer where the image will be placed
     * @param resManager resource manager
     * @param e          OFDRW元素
     * @param maxUnitID  maximum element ID provider
     * @throws RenderException rendering error occurred
     */
    public void render(ST_Loc pageLoc, CT_PageBlock layer, ResManager resManager, Div e, AtomicInteger maxUnitID) throws RenderException;
}

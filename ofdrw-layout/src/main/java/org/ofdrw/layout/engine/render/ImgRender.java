package org.ofdrw.layout.engine.render;

import org.ofdrw.core.basicStructure.pageObj.layer.block.CT_PageBlock;
import org.ofdrw.core.basicStructure.pageObj.layer.block.ImageObject;
import org.ofdrw.core.basicType.ST_Array;
import org.ofdrw.core.basicType.ST_ID;
import org.ofdrw.core.basicType.ST_Loc;
import org.ofdrw.layout.element.Div;
import org.ofdrw.layout.element.Img;
import org.ofdrw.layout.engine.ResManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * image渲染对象
 * <p>
 * {@link org.ofdrw.layout.element.Img} 的渲染器
 *
 * @author Quan Guanyu
 * @since 2020-03-22 13:17:52
 */
public class ImgRender implements Processor {
    /**
     * 执行image渲染
     *
     * @param pageLoc    absolute path of the page in the virtual container.
     * @param layer      the layer where the image will be placed
     * @param resManager resource manager
     * @param e          image对象
     * @param maxUnitID  maximum element ID provider
     * @throws RenderException rendering error occurred
     */
    @Override
    public void render(ST_Loc pageLoc, CT_PageBlock layer, ResManager resManager, Div e, AtomicInteger maxUnitID)throws RenderException {
        if (e instanceof Img) {
            render(layer, resManager, (Img) e, maxUnitID);
        }
    }

    /**
     * 渲染image对象
     * <p>
     * 由于image对象有image资源所以需要放入到document container中
     *
     * @param layer      the layer where the image will be placed
     * @param resManager resource manager
     * @param e          image对象
     * @param maxUnitID  maximum element ID provider
     */
    public static void render(CT_PageBlock layer, ResManager resManager, Img e, AtomicInteger maxUnitID) {
        if (e == null) {
            return;
        }
        // image存储路径
        Path p = e.getSrc();
        if (p == null || Files.notExists(p)) {
            throw new IllegalArgumentException("image对象(Img)路径非法");
        }
        // 加入image资源
        ST_ID id = null;
        try {
            id = resManager.addImage(p);
        } catch (IOException ex) {
            throw new RenderException("渲染image复制失败：" + ex.getMessage(), ex);
        }
        // add image to public resources
        ImageObject imgObj = new ImageObject(maxUnitID.incrementAndGet());
        imgObj.setResourceID(id.ref());
        double x = e.getX() + e.getMarginLeft() + e.getBorderLeft() + e.getPaddingLeft();
        double y = e.getY() + e.getMarginTop() + e.getBorderTop() + e.getPaddingTop();
        imgObj.setBoundary(x, y, e.getWidth(), e.getHeight());
        imgObj.setCTM(new ST_Array(e.getWidth(), 0, 0, e.getHeight(), 0, 0));
        if (e.getOpacity() != null) {
            // graphic element transparency
            imgObj.setAlpha((int) (e.getOpacity() * 255));
        }
        layer.addPageBlock(imgObj);
    }
}

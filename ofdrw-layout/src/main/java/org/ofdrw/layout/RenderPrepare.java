package org.ofdrw.layout;

/**
 * 渲染准备
 * <p>
 * 在渲染前完成元素初步布局和测量以及缓存等工作
 *
 * @author Quan Guanyu
 * @since 2020-02-28 04:02:47
 */
public interface RenderPrepare {
    /**
     * 执行渲染前的准备工作
     * <p>
     * 包括简单的内部布局，必要数据的缓存
     *
     * @param widthLimit width limit
     * @return element size
     */
    Rectangle doPrepare(Double widthLimit);
}

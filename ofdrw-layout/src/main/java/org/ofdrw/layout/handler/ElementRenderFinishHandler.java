package org.ofdrw.layout.handler;

import org.ofdrw.core.basicType.ST_ID;
import org.ofdrw.core.basicType.ST_Loc;

import java.util.List;

/**
 * OFDRW元素渲染结束时触发的回调函数
 * <p>
 * 用与获取OFDRW元素在转换为OFD element生成的object ID。
 *
 * @author Quan Guanyu
 * @since 2024-5-27 20:10:18
 */
@FunctionalInterface
public interface ElementRenderFinishHandler {

    /**
     * OFDRW元素渲染结束时触发的回调函数
     *
     * @param loc          OFD element所处页面在OFD容器内的absolute path，不能为null。
     * @param contentObjId 内容对象绘制后产生的OFD elementID序列，不能为null。
     * @param resIds       元素生成过程中引用的resource ID序列，不能为null。
     */
    void handle(ST_Loc loc, List<ST_ID> contentObjId, List<ST_ID> resIds);
}

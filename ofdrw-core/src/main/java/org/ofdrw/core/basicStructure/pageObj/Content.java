package org.ofdrw.core.basicStructure.pageObj;

import org.dom4j.Element;
import org.ofdrw.core.OFDElement;
import org.ofdrw.core.basicStructure.pageObj.layer.CT_Layer;
import org.ofdrw.core.basicType.ST_ID;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * page content描述，该节点不存在是，表示空白页面
 * <p>
 * 7.7 page object 表 12
 *
 * @author Quan Guanyu
 * @since 2019-10-10 09:55:20
 */
public class Content extends OFDElement {
    public Content(Element proxy) {
        super(proxy);
    }

    public Content() {
        super("Content");
    }

    /**
     * [required]
     * 增加 layer node
     * <p>
     * a page can contain one or more layers
     * <p>
     * Note: each added layer node must have the ID attribute set.
     *
     * @param layer layer node
     * @return this
     * @throws IllegalArgumentException 加入的layer对象（CT_Layer）没有设置ID属性
     */
    public Content addLayer(CT_Layer layer) {
        ST_ID id = layer.getObjID();
        if (id == null) {
            throw new IllegalArgumentException("加入的layer对象（CT_Layer）没有设置ID属性");
        }
        this.add(layer);
        return this;
    }

    /**
     * [required]
     * 获取 layer node列表
     * <p>
     * a page can contain one or more layers
     * <p>
     * Note: each added layer node must have the ID attribute set.
     *
     * @return layer node
     */
    public List<CT_Layer> getLayers() {
        return this.getOFDElements("Layer", CT_Layer::new);
    }

    /**
     * [required]
     * 获取 排序后的layer node列表
     * <p>
     * a page can contain one or more layers
     * <p>
     * 注意：每个加入的layer node必须设置 ID属性，排序如下：
     * 背景模板
     * background layer
     * 正文模板
     * body layer
     * 前景模板
     * foreground layer
     *
     * @return layer node
     */
    public List<CT_Layer> getOrderedLayers() {
        List<CT_Layer> listLayers = this.getOFDElements("Layer", CT_Layer::new);
        listLayers.sort(Comparator.comparingInt(p -> p.getType().order()));
        return listLayers;
    }
}

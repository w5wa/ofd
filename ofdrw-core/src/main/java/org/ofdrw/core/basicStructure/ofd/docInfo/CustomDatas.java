package org.ofdrw.core.basicStructure.ofd.docInfo;

import org.dom4j.Element;
import org.ofdrw.core.OFDElement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 用户自定义元数据集合。其子节点为 CustomData
 *
 * @author Quan Guanyu
 * @since 2019-10-01 07:36:46
 */
public class CustomDatas extends OFDElement {
    public CustomDatas(Element proxy) {
        super(proxy);
    }

    public CustomDatas() {
        super("CustomDatas");
    }

    /**
     * [required]
     * add user-defined metadata
     *
     * @param name  用户自定义元数据名称
     * @param value 用户自定义metadata value
     * @return this
     */
    public CustomDatas addCustomData(String name, String value) {
        CustomData customData = new CustomData(name, value);
        return addCustomData(customData);
    }

    /**
     * [required]
     * add user-defined metadata
     *
     * @param customData 用户自定义元数据
     * @return this
     */
    public CustomDatas addCustomData(CustomData customData) {
        this.add(customData);
        return this;
    }

    /**
     * [required]
     * 获取自定义元数据集合
     *
     * @return 自定义元数据集合
     */
    public List<CustomData> getCustomDatas() {
        return this.getOFDElements("CustomData", CustomData::new);
    }

    /**
     * 获取用户自定义metadata value
     *
     * @param name 元数据名称
     * @return metadata value
     */
    public String getCustomDataValue(String name) {
        List<CustomData> dataList = getCustomDatas();
        String res = null;
        for (CustomData item : dataList) {
            if (name.equals(item.getDataName())) {
                res = item.getValue();
                break;
            }
        }
        return res;
    }

    @Override
    public String getQualifiedName() {
        return "ofd:CustomDatas";
    }
}

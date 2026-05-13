package org.ofdrw.core.basicStructure.res;

import org.dom4j.Element;
import org.ofdrw.core.OFDElement;
import org.ofdrw.core.basicStructure.res.resources.Fonts;
import org.ofdrw.core.basicType.ST_Loc;

import java.util.ArrayList;
import java.util.List;

/**
 * 资源
 * <p>
 * 资源是绘制图元时所需数据（如drawing parameters、color space、glyph、图像、音视频等）的集合。
 * 在页面中出现的资源数据内容都保存在容器的特定文件夹内，但其索引信息保存在resource file中。
 * 一个文档可能contains一个或多个resource file。资源根据作用范围分为public resource和页资源，公共resource file
 * 在文档root node中进行制定，页resource file在页对象中进行制定。
 * <p>
 * 7.9 资源 图 20
 *
 * @author Quan Guanyu
 * @since 2019-10-11 06:00:24
 */
public class Res extends OFDElement {

    public Res(Element proxy) {
        super(proxy);
    }

    public Res() {
        super("Res");
    }

    /**
     * [required attribute]
     * 设置 此resource file的通用数据存储路径。
     * <p>
     * the BaseLoc attribute specifies the resource file storage location,
     * 比如 R1.xml 中可以指定 BaseLoc为“./Res”，
     * indicating that the default storage location for all data files in this resource file is
     * in the Res directory under the current path.
     *
     * @param baseLoc 此resource file的通用数据存储路径
     * @return this
     */
    public Res setBaseLoc(ST_Loc baseLoc) {
        this.addAttribute("BaseLoc", baseLoc.toString());
        return this;
    }

    /**
     * [required attribute]
     * 获取 此resource file的通用数据存储路径。
     * <p>
     * the BaseLoc attribute specifies the resource file storage location,
     * 比如 R1.xml 中可以指定 BaseLoc为“./Res”，
     * indicating that the default storage location for all data files in this resource file is
     * in the Res directory under the current path.
     *
     * @return 此resource file的通用数据存储路径
     */
    public ST_Loc getBaseLoc() {
        return ST_Loc.getInstance(this.attributeValue("BaseLoc"));
    }

    /**
     * [optional]
     * 添加 资源
     * <p>
     * a resource file can describe 0 or more resources
     *
     * @param resource resource
     * @return this
     */
    public Res addResource(OFDResource resource) {
        this.add(resource);
        return this;
    }

    /**
     * 获取fontresource file
     *
     * @return fontresource list
     */
    public List<Fonts> getFonts() {
        List<Fonts> fontsList = new ArrayList<>();
        for (OFDResource item : getResources()) {
            if (item instanceof Fonts) {
                fontsList.add((Fonts) item);
            }
        }
        return fontsList;
    }

    /**
     * [optional]
     * 获取 resource list
     * <p>
     * a resource file can describe 0 or more resources
     * <p>
     * tip：可以使用<code>instanceof</code>判断是哪一种资源
     *
     * @return resource list
     */
    public List<OFDResource> getResources() {
        List<Element> elements = this.elements();
        List<OFDResource> res = new ArrayList<>(elements.size());
        elements.forEach(item -> res.add(OFDResource.getInstance(item)));
        return res;
    }
}

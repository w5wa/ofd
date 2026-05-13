package org.ofdrw.core.customTags;

import org.dom4j.Element;
import org.ofdrw.core.OFDElement;
import org.ofdrw.core.OFDSimpleTypeElement;
import org.ofdrw.core.basicType.ST_Loc;

/**
 * 自定义标引入口
 * <p>
 * 16 图 82 表 63
 *
 * @author Quan Guanyu
 * @since 2019-11-19 06:15:00
 */
public class CustomTag extends OFDElement {
    public CustomTag(Element proxy) {
        super(proxy);
    }

    public CustomTag() {
        super("CustomTag");
    }

    /**
     * [required]
     * 设置 自定义标引内容节点使用的类型标识
     *
     * @param typeId 自定义标引内容节点使用的类型标识
     * @return this
     */
    public CustomTag setTypeID(String typeId) {
        if (typeId == null || typeId.trim().length() == 0) {
            throw new IllegalArgumentException("自定义标引内容节点使用的类型标识（TypeID）为空");
        }
        this.addAttribute("TypeID", typeId);
        return this;
    }

    /**
     * [required]
     * 获取 自定义标引内容节点使用的类型标识
     *
     * @return 自定义标引内容节点使用的类型标识
     */
    public String getTypeID() {
        String str = this.attributeValue("TypeID");
        if (str == null || str.trim().length() == 0) {
            throw new IllegalArgumentException("自定义标引内容节点使用的类型标识（TypeID）为空");
        }
        return str;
    }

    /**
     * 设置 namespace
     * <p>
     * 附录 A.9 CustomTags.xsd
     *
     * @param nameSpace namespace
     * @return this
     */
    public CustomTag setNameSpace(String nameSpace) {
        if (nameSpace == null || nameSpace.trim().length() == 0) {
            return this;
        }
        this.addAttribute("NameSpace", nameSpace);
        return this;
    }

    /**
     * 获取 namespace
     * <p>
     * 附录 A.9 CustomTags.xsd
     *
     * @return namespace
     */
    public String getNameSpace() {
        return this.attributeValue("NameSpace");
    }


    /**
     * [optional]
     * 设置 points to自定义标引内容节点适用的Schema文件
     *
     * @param schemaLoc points to自定义标引内容节点适用的Schema文件
     * @return this
     */
    public CustomTag setSchemaLoc(ST_Loc schemaLoc) {
        if (schemaLoc == null) {
            this.removeOFDElemByNames("SchemaLoc");
            return this;
        }
        this.set(new OFDSimpleTypeElement("SchemaLoc", schemaLoc));
        return this;
    }

    /**
     * [optional]
     * 获取 points to自定义标引内容节点适用的Schema文件
     *
     * @return points to自定义标引内容节点适用的Schema文件
     */
    public ST_Loc getSchemaLoc() {
        Element e = this.getOFDElement("SchemaLoc");
        return e == null ? null : new ST_Loc(e.getTextTrim());
    }


    /**
     * [required]
     * 设置 points to自定义标引文件
     *
     * 这类文件中通过“非接触方式”引用版式内容流中的图元和相关信息
     *
     * @param fileLoc points to自定义标引文件
     * @return this
     */
    public CustomTag setFileLoc(ST_Loc fileLoc){
        if(fileLoc == null){
            throw new IllegalArgumentException("points to自定义标引文件（FileLoc）为空");
        }
        this.set(new OFDSimpleTypeElement("FileLoc", fileLoc));
        return this;
    }

    /**
     * [required]
     * 设置 points to自定义标引文件
     *
     * 这类文件中通过“非接触方式”引用版式内容流中的图元和相关信息
     *
     * @return  points to自定义标引文件
     */
    public ST_Loc getFileLoc(){
        Element e = this.getOFDElement("FileLoc");
        if (e == null) {
            throw new IllegalArgumentException("points to自定义标引文件（FileLoc）为空");
        }
        return new ST_Loc(e.getTextTrim());
    }
}

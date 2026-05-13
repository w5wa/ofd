package org.ofdrw.core.signatures.appearance;

import org.dom4j.Element;
import org.jetbrains.annotations.Nullable;
import org.ofdrw.core.OFDElement;
import org.ofdrw.core.basicType.ST_Loc;

/**
 * 电子seal/stamp信息
 * <p>
 * 18.2.1 图 86 表 67
 *
 * @author Quan Guanyu
 * @since 2019-11-21 18:54:20
 */
public class Seal extends OFDElement {
    public Seal(Element proxy) {
        super(proxy);
    }

    public Seal() {
        super("Seal");
    }

    public Seal(ST_Loc baseLoc) {
        this();
        this.setBaseLoc(baseLoc);
    }


    /**
     * [required]
     * 设置 points to包内的安全电子seal/stampfile path
     * <p>
     * 遵循密码领域的相关规范
     *
     * @param baseLoc points to包内的安全电子seal/stampfile path
     * @return this
     */
    public Seal setBaseLoc(ST_Loc baseLoc) {
        if (baseLoc == null) {
            throw new IllegalArgumentException("points to包内的安全电子seal/stampfile path（BaseLoc）为空");
        }
        this.setOFDEntity("BaseLoc", baseLoc);
        return this;
    }

    /**
     * [required]
     * 获取 points to包内的安全电子seal/stampfile path
     * <p>
     * 遵循密码领域的相关规范
     *
     * @return points to包内的安全电子seal/stampfile path
     */
    public ST_Loc getBaseLoc() {
        Element e = this.getOFDElement("BaseLoc");
        if (e == null) {
            throw new IllegalArgumentException("points to包内的安全电子seal/stampfile path（BaseLoc）为空");
        }
        return ST_Loc.getInstance(e);
    }


    /**
     * [optional, OFD 2.0]
     * 设置 印模image存储位置
     *
     * @param imageLoc 印模image存储位置，null表示删除
     * @return this
     */
    public Seal setImageLoc(@Nullable ST_Loc imageLoc) {
        if (imageLoc == null) {
            this.removeOFDElemByNames("ImageLoc");
            return this;
        }
        this.setOFDEntity("ImageLoc", imageLoc);
        return this;
    }

    /**
     * [optional, OFD 2.0]
     * 获取 印模image存储位置
     *
     * @return 印模image存储位置，可能为null
     */
    @Nullable
    public ST_Loc getImageLoc(){
        Element e = this.getOFDElement("ImageLoc");
        if (e == null) {
            return null;
        }
        return ST_Loc.getInstance(e);
    }
}

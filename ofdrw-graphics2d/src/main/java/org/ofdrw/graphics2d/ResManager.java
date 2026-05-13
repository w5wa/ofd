package org.ofdrw.graphics2d;

import org.dom4j.DocumentException;
import org.ofdrw.core.OFDElement;
import org.ofdrw.core.basicStructure.doc.Document;
import org.ofdrw.core.basicStructure.res.CT_MultiMedia;
import org.ofdrw.core.basicStructure.res.Res;
import org.ofdrw.core.basicStructure.res.resources.*;
import org.ofdrw.core.basicType.ST_ID;
import org.ofdrw.core.basicType.ST_Loc;
import org.ofdrw.core.compositeObj.CT_VectorG;
import org.ofdrw.core.pageDescription.color.colorSpace.CT_ColorSpace;
import org.ofdrw.core.pageDescription.drawParam.CT_DrawParam;
import org.ofdrw.core.text.font.CT_Font;
import org.ofdrw.pkg.container.DocDir;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * public resource manager
 * <p>
 * 管理待加入文档中所有资源
 *
 * @author Quan Guanyu
 * @since 2020-03-22 16:20:07
 */
public class ResManager {

    /**
     * 自增的ID生成器
     */
    private AtomicInteger maxUnitID;
    /**
     * document container
     */
    private DocDir docDir;

    /**
     * 媒体resource list
     * <p>
     * located in the document resource list
     */
    private MultiMedias medias;

    /**
     * drawing parameters列表
     * <p>
     * located in the document resource list
     */
    private DrawParams drawParams;


    /**
     * fontresource list
     * <p>
     * located in the public resource list
     */
    private Fonts fonts;

    /**
     * color space的描述列表
     * <p>
     * located in the public resource list
     */
    private ColorSpaces colorSpaces;

    /**
     * 矢量图像列表
     * <p>
     * located in the document resource list
     */
    private CompositeGraphicUnits compositeGraphicUnits;


    /**
     * drawing parametersHash
     * <p>
     * KEY: resource object的去除ID后的XMLstring的hashCode
     * VALUE: 文档中的object ID。
     * <p>
     * 该缓存表用于解决drawing parameters冗余造成的资源浪费。
     */
    private final HashMap<Integer, ST_ID> resObjHash = new HashMap<>();

    private ResManager() {
    }


    /**
     * 创建资源管理
     *
     * @param docDir    文档虚拟容器
     * @param maxUnitID auto-increment maximum ID provider
     */
    public ResManager(DocDir docDir, AtomicInteger maxUnitID) {
        this();
        this.docDir = docDir;
        this.maxUnitID = maxUnitID;
    }

    /**
     * 获取public resource清单
     * <p>
     * 如： 图形、font等需要共用的资源
     *
     * @return public resource清单
     */
    public Res pubRes() {
        try {
            return docDir.getPublicRes();
        } catch (FileNotFoundException | DocumentException e) {
            // if not found, create a public resource manifest; container directory is Res under document root
            Res publicRes = new Res().setBaseLoc(ST_Loc.getInstance("Res"));
            docDir.setPublicRes(publicRes);
            document().getCommonData().addPublicRes(ST_Loc.getInstance("PublicRes.xml"));
            return publicRes;
        }
    }

    /**
     * 文档资源清单
     * <p>
     * 与文档相关的资源：image、视频等
     *
     * @return 文档资源清单
     */
    public Res docRes() {
        try {
            return docDir.getDocumentRes();
        } catch (FileNotFoundException | DocumentException e) {
            // if not found, create a public resource manifest; container directory is Res under document root
            Res docRes = new Res().setBaseLoc(ST_Loc.getInstance("Res"));
            docDir.setDocumentRes(docRes);
            document().getCommonData().addDocumentRes(ST_Loc.getInstance("DocumentRes.xml"));
            return docRes;
        }
    }

    /**
     * ignored无法获取到得到错误信息
     *
     * @return document对象
     */
    private Document document() {
        try {
            return docDir.getDocument();
        } catch (FileNotFoundException | DocumentException ex) {
            throw new RuntimeException("文档中缺少Document.xml 文件");
        }
    }

    /**
     * add resource object directly to resource list
     * <p>
     * 加入资源时将优先检查缓存是否存在完全一致的资源，如果存在则复用对象。
     * <p>
     * 注意：加入对象的ID将被ignored，object ID有resource manager生成并设置。
     *
     * @param resObj resource object
     * @return 对象在文档中的resource ID
     */
    public ST_ID addRawWithCache(OFDElement resObj) {
        if (resObj == null) {
            return null;
        }

        // 移除对象上已经存在的用于基于资源本身的Hash值
        resObj.removeAttr("ID");
        int key = resObj.asXML().hashCode();

        ST_ID objId = this.resObjHash.get(key);
        if (objId != null) {
            // 文档中已经存在相同资源，则复用该资源。
            resObj.setObjID(objId);
            return objId;
        } else {
            // 文档中不存在该资源则resource ID，并缓存
            objId = new ST_ID(maxUnitID.incrementAndGet());
            resObj.setObjID(objId);
            this.resObjHash.put(key, objId);
        }

        // 判断资源类型加入到合适的resource list中
        if (resObj instanceof CT_ColorSpace) {
            Res resMenu = pubRes();
            if (colorSpaces == null) {
                this.colorSpaces = new ColorSpaces();
                resMenu.addResource(colorSpaces);
            }
            colorSpaces.addColorSpace((CT_ColorSpace) resObj);
        } else if (resObj instanceof CT_Font) {
            Res resMenu = pubRes();
            if (fonts == null) {
                this.fonts = new Fonts();
                resMenu.addResource(fonts);
            }
            fonts.addFont((CT_Font) resObj);
        } else if (resObj instanceof CT_DrawParam) {
            Res resMenu = docRes();
            if (drawParams == null) {
                this.drawParams = new DrawParams();
                resMenu.addResource(drawParams);
            }
            drawParams.addDrawParam((CT_DrawParam) resObj);
        } else if (resObj instanceof CT_MultiMedia) {
            Res resMenu = docRes();
            if (medias == null) {
                this.medias = new MultiMedias();
                resMenu.addResource(medias);
            }
            medias.addMultiMedia((CT_MultiMedia) resObj);
        } else if (resObj instanceof CT_VectorG) {
            Res resMenu = docRes();
            if (compositeGraphicUnits == null) {
                this.compositeGraphicUnits = new CompositeGraphicUnits();
                resMenu.addResource(compositeGraphicUnits);
            }
            compositeGraphicUnits.addCompositeGraphicUnit((CT_VectorG) resObj);
        }
        return objId;
    }
}

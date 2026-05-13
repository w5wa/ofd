package org.ofdrw.layout.engine;

import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.ofdrw.core.OFDElement;
import org.ofdrw.core.basicStructure.doc.CT_CommonData;
import org.ofdrw.core.basicStructure.doc.Document;
import org.ofdrw.core.basicStructure.ofd.OFD;
import org.ofdrw.core.basicStructure.res.CT_MultiMedia;
import org.ofdrw.core.basicStructure.res.MediaType;
import org.ofdrw.core.basicStructure.res.OFDResource;
import org.ofdrw.core.basicStructure.res.Res;
import org.ofdrw.core.basicStructure.res.resources.*;
import org.ofdrw.core.basicType.ST_ID;
import org.ofdrw.core.basicType.ST_Loc;
import org.ofdrw.core.compositeObj.CT_VectorG;
import org.ofdrw.core.pageDescription.color.colorSpace.CT_ColorSpace;
import org.ofdrw.core.pageDescription.drawParam.CT_DrawParam;
import org.ofdrw.core.text.font.CT_Font;
import org.ofdrw.font.Font;
import org.ofdrw.pkg.container.DocDir;
import org.ofdrw.pkg.container.OFDDir;
import org.ofdrw.reader.OFDReader;
import org.ofdrw.reader.ResourceLocator;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
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
     * OFD document object
     */
    private OFDDir root;


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
     * document object
     */
    private Document document;

    /**
     * 文档资源
     */
    private Res documentRes;

    /**
     * public resource
     */
    private Res publicRes;

    /**
     * 新增资源object ID
     */
    public ArrayList<ST_ID> newResIds = new ArrayList<>();

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
     * 创建resource manager
     * <p>
     * requires Document.xml to exist in the Doc_N path
     *
     * @param root      document root directory
     * @param docDir    文档虚拟容器，请确保document container中存在 Document.xml
     * @param maxUnitID auto-increment maximum ID provider
     * @throws RuntimeException document parsing exception
     */
    public ResManager(OFDDir root, DocDir docDir, AtomicInteger maxUnitID) {
        this();
        this.root = root;
        this.docDir = docDir;
        this.maxUnitID = maxUnitID;

        try {
            this.document = docDir.getDocument();
        } catch (FileNotFoundException | DocumentException e) {
            throw new RuntimeException("文档解析失败未能找到 Document.xml", e);
        }

        // 如果存在public resource，尝试加载
        if (docDir.exist(DocDir.PublicResFileName)) {
            try {
                this.publicRes = docDir.getPublicRes();
                reloadRes(publicRes);
            } catch (FileNotFoundException e) {
                // ignore file not found，不解析
            } catch (DocumentException e) {
                throw new RuntimeException("已有 PublicRes.xml resource file解析失败", e);
            }
        }

        // 如果存在文档资源，尝试加载
        if (docDir.exist(DocDir.DocumentResFileName)) {
            try {
                this.documentRes = docDir.getDocumentRes();
                reloadRes(documentRes);
            } catch (FileNotFoundException e) {
                // ignore file not found，不解析
            } catch (DocumentException e) {
                throw new RuntimeException("已有 DocumentRes.xml resource file解析失败", e);
            }
        }

    }

    /**
     * 创建resource manager，
     *
     * <p>
     * requires Document.xml to exist in the Doc_N path
     *
     * @param reader OFD parser
     * @throws DocumentException     document parsing exception
     * @throws FileNotFoundException OFD document结构非法
     */
    public ResManager(OFDReader reader) throws FileNotFoundException, DocumentException {
        this();

        OFDDir ofdDir = reader.getOFDDir();
        OFD ofd = ofdDir.getOfd();
        // resource locator
        ResourceLocator resourceLocator = reader.getResourceLocator();
        // find Document.xml and serialize
        ST_Loc docRoot = ofd.getDocBody().getDocRoot();
        Document document = resourceLocator.get(docRoot, Document::new);
        CT_CommonData commonData = document.getCommonData();

        this.root = ofdDir;
        this.docDir = ofdDir.obtainDocDefault();
        this.document = document;
        this.maxUnitID = new AtomicInteger(commonData.getMaxUnitID().getId().intValue());

        try {
            resourceLocator.save();
            resourceLocator.cd(docDir);
            for (ST_Loc loc : commonData.getPublicResList()) {
                this.publicRes = resourceLocator.get(loc, Res::new);
                reloadRes(this.publicRes);
            }
            for (ST_Loc loc : commonData.getDocumentResList()) {
                this.documentRes = resourceLocator.get(loc, Res::new);
                reloadRes(documentRes);
            }
        } catch (Exception e) {
            // ignored异常
        } finally {
            resourceLocator.restore();
        }
    }

    /**
     * 创建资源管理
     *
     * <p>
     * requires Document.xml to exist in the Doc_N path
     *
     * @param docDir    文档虚拟容器，请确保document container中存在 Document.xml
     * @param maxUnitID auto-increment maximum ID provider
     * @deprecated 缺少根容器可能导致部分资源无法获取，请使用 {@link #ResManager(OFDDir, DocDir, AtomicInteger)}
     */
    @Deprecated
    public ResManager(DocDir docDir, AtomicInteger maxUnitID) {
        this(null, docDir, maxUnitID);
    }

    /**
     * 重载public resource缓存
     */
    private void reloadRes(Res res) {
        List<OFDResource> resources = res.getResources();
        if (resources == null || resources.isEmpty()) {
            return;
        }
        for (OFDResource resource : resources) {
            // 获取各个集合下的resource object
            List<Element> elements = resource.elements();
            if (elements == null || elements.isEmpty()) {
                continue;
            }
            for (Element ctResObj : elements) {
                // 获取原来resource object的ID
                ST_ID id = ST_ID.getInstance(ctResObj.attributeValue("ID"));
                if (id == null) {
                    return;
                }
                // 遍历每一个resource object，复制对象，删除object ID，序列化为XMLstring
                Element copy = (Element) ctResObj.clone();
                copy.remove(copy.attribute("ID"));
                String key = copy.asXML();
                resObjHash.put(key.hashCode(), id);
            }
        }

    }

    /**
     * 增加font资源
     * <p>
     * 如果font已经被加入，那么不会重复加入
     *
     * @param font font描述对象
     * @return font的object ID
     * @throws IOException file copy exception
     */
    public ST_ID addFont(Font font) throws IOException {
        return addFontRet(font).getID();
    }

    /**
     * 增加font资源 并获取 添加的font object
     * <p>
     * 如果font已经被加入，那么不会重复加入
     *
     * @param font font描述对象
     * @return font的对象
     * @throws IOException file copy exception
     */
    public CT_Font addFontRet(Font font) throws IOException {
        // 获取font全名
        String familyName = font.getFamilyName();
        // 新建一个OFDfont object
        CT_Font ctFont = new CT_Font()
                .setFontName(font.getName())
                .setFamilyName(familyName);
        Path fontFile = font.getFontFile();
        if (fontFile != null && font.isEmbeddable()) {
            // 将font file加入到document container中
            fontFile = docDir.addResourceWithPath(fontFile);

            String filename = fontFile.getFileName().toString();
            // 若resource file中的相对路径不是Res，那么采用absolute path
            if (publicRes != null && !ST_Loc.equal("Res", publicRes.getBaseLoc())) {
                filename = docDir.getAbsLoc().cat("Res").cat(filename).toString();
            }
            ctFont.setFontFile(filename);
        }

        // 设置特殊字族属性
        if (familyName != null) {
            switch (familyName.toLowerCase()) {
                case "serif":
                    ctFont.setSerif(true);
                    break;
                case "bold":
                    ctFont.setBold(true);
                    break;
                case "italic":
                    ctFont.setItalic(true);
                    break;
                case "fixedwidth":
                    ctFont.setFixedWidth(true);
                    break;
            }
        }
        addRawWithCache(ctFont);
        return ctFont;
    }

    /**
     * 加入一个image资源
     * <p>
     * 如果image已经存在那么不会重复加入
     *
     * @param imgPath image path，请避免资源和文档中已经存在的资源重复
     * @return resource ID
     * @throws IOException file copy exception
     */
    public ST_ID addImage(Path imgPath) throws IOException {
        // 将文件加入资源容器中，并获取资源在文件中的absolute path
        Path imgCtnPath = docDir.addResourceWithPath(imgPath);
        // 获取在容器中的file name
        String filename = imgCtnPath.getFileName().toString();
        // 若resource file中的相对路径不是Res，那么采用absolute path
        if (documentRes != null && !ST_Loc.equal("Res", documentRes.getBaseLoc())) {
            filename = docDir.getAbsLoc().cat("Res").cat(filename).toString();
        }

        // 获取image文件后缀名称
        String fileSuffix = pictureFormat(filename);
        // 创建image对象
        CT_MultiMedia multiMedia = new CT_MultiMedia()
                .setType(MediaType.Image)
                .setFormat(fileSuffix)
                .setMediaFile(ST_Loc.getInstance(filename));
        // 添加到resource list中
        return addRawWithCache(multiMedia);
    }

    /**
     * 加入一个drawing parameters
     * <p>
     * 如果存在相同或类似的drawing parameters则不会重复添加。
     *
     * @param param drawing parameters
     * @return resource ID
     */
    public ST_ID addDrawParam(CT_DrawParam param) {
        if (param == null) {
            return null;
        }
        // 复制drawing parameters，防止出现节点重复添加问题。
        param = param.clone();

        return addRawWithCache(param.clone());
    }

    /**
     * 根据image名称推断image格式
     *
     * @param fileName imagefile name
     * @return image格式string
     */
    private String pictureFormat(String fileName) {
        String fileSuffix = fileName.substring(fileName.lastIndexOf(".") + 1).toUpperCase();
        switch (fileSuffix) {
            case "JPG":
                return "JPEG";
            case "TIF":
                return "TIFF";
            default:
                return fileSuffix;
        }
    }

    /**
     * 获取public resource清单
     * <p>
     * 如： 图形、font等需要共用的资源
     *
     * @return public resource清单
     */
    public Res pubRes() {
        if (publicRes != null) {
            return publicRes;
        }
        // if not found, create a public resource manifest; container directory is Res under document root
        Res pubRes = new Res().setBaseLoc(ST_Loc.getInstance("Res"));
        docDir.setPublicRes(pubRes);
        CT_CommonData commonData = document.getCommonData();
        if (commonData == null) {
            commonData = new CT_CommonData();
            document.setCommonData(commonData);
        }
        commonData.addPublicRes(ST_Loc.getInstance("PublicRes.xml"));
        this.publicRes = pubRes;
        return publicRes;
    }

    /**
     * 文档资源清单
     * <p>
     * 与文档相关的资源：image、视频等
     *
     * @return 文档资源清单
     */
    public Res docRes() {
        if (documentRes != null) {
            return documentRes;
        }

        // if not found, create a public resource manifest; container directory is Res under document root
        Res docRes = new Res().setBaseLoc(ST_Loc.getInstance("Res"));
        docDir.setDocumentRes(docRes);
        CT_CommonData commonData = document.getCommonData();
        if (commonData == null) {
            commonData = new CT_CommonData();
            document.setCommonData(commonData);
        }
        commonData.addDocumentRes(ST_Loc.getInstance("DocumentRes.xml"));
        documentRes = docRes;
        return documentRes;
    }


    /**
     * add resource object directly to resource list
     * <p>
     * 注意：该方法是一个原生方法，具有一定的资源重复风险。
     *
     * @param resObj resource object
     * @return this
     * @deprecated {@link #addRawWithCache(OFDElement)}
     */
    @Deprecated
    public ResManager addRaw(OFDElement resObj) {
        if (resObj == null) {
            return this;
        }

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
        return this;
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
            // 记录resource ID
            newResIds.add(objId);
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

    /**
     * 通过字族名获取font object，如果无法找到则返还null
     *
     * @param name font name
     * @return font object 或 null
     */
    public ExistCTFont getFont(String name) {
        if ("".equals(name) || name == null) {
            return null;
        }
        name = name.toLowerCase();
        CT_Font res = null;
        // 尝试从public resource中获取 font清单
        Res resMenu = pubRes();
        List<Fonts> fontsList = resMenu.getFonts();
        for (Fonts fonts : fontsList) {
            List<CT_Font> arr = fonts.getFonts();
            for (CT_Font ctFont : arr) {
                // ignored大小写的比较
                String fontName = ctFont.getFontName();
                if (fontName != null) {
                    fontName = fontName.toLowerCase();
                }

                String familyName = ctFont.getFamilyName();
                if (familyName != null) {
                    familyName = familyName.toLowerCase();
                }

                if (name.equals(fontName) || name.equals(familyName)) {
                    // 找到最后一个匹配的font
                    res = ctFont;
                }
            }
        }
        if (res == null) {
            // 尝试从文档资源中获取 font清单
            resMenu = docRes();
            fontsList = resMenu.getFonts();
            for (Fonts fonts : fontsList) {
                List<CT_Font> arr = fonts.getFonts();
                for (CT_Font ctFont : arr) {
                    // ignored大小写的比较
                    String fontName = ctFont.getFontName();
                    if (fontName != null) {
                        fontName = fontName.toLowerCase();
                    }

                    String familyName = ctFont.getFamilyName();
                    if (familyName != null) {
                        familyName = familyName.toLowerCase();
                    }
                    if (name.equals(fontName) || name.equals(familyName)) {
                        res = ctFont;
                    }
                }
            }
        }

        if (res == null) {
            // 无法找到font
            return null;
        }

        // 获取font file的absolute path
        ST_Loc loc = res.getFontFile();
        Path p = null;
        if (loc != null && root != null) {
            ST_Loc abs = abs(resMenu, loc);
            try {
                p = root.getFile(abs.getFileName());
            } catch (FileNotFoundException e) {
                // ignore
            }
        }

        return new ExistCTFont(res, p);
    }

    /**
     * 设置文档的root node
     *
     * @param root root node
     */
    public void setRoot(OFDDir root) {
        this.root = root;
    }

    /**
     * 获取文档根容器
     *
     * @return 文档根容器
     */
    public OFDDir getRoot() {
        return root;
    }

    /**
     * 资源完整路径
     *
     * @param res    资源清单
     * @param target 目标路径
     * @return 相对于文件的absolute path
     */
    private ST_Loc abs(Res res, ST_Loc target) {
        if (target.isRootPath()) {
            // absolute path
            return target;
        }
        ST_Loc absLoc = null;
        ST_Loc base = res.getBaseLoc();
        if (base != null && base.isRootPath()) {
            // resource file的通用存储路径 为根路径时直接在此基础上拼接
            absLoc = base;
        } else {
            // resource file的通用存储路径 为相对路径时，拼接当前文档路径
            absLoc = docDir.getAbsLoc().cat(base);
        }
        return absLoc.cat(target);
    }

    /**
     * 获取新加入的resource ID
     *
     * @return 新加入的resource ID
     */
    public ArrayList<ST_ID> getNewResIds() {
        return newResIds;
    }

    /**
     * 获取当前操作的document container
     *
     * @return document container
     */
    public DocDir getDocDir() {
        return docDir;
    }
}

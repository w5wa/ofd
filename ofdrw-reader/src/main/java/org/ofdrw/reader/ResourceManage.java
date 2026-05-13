package org.ofdrw.reader;

import org.apache.commons.io.IOUtils;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.ofdrw.core.OFDElement;
import org.ofdrw.core.basicStructure.doc.CT_CommonData;
import org.ofdrw.core.basicStructure.doc.Document;
import org.ofdrw.core.basicStructure.ofd.DocBody;
import org.ofdrw.core.basicStructure.ofd.OFD;
import org.ofdrw.core.basicStructure.pageObj.layer.block.ImageObject;
import org.ofdrw.core.basicStructure.res.CT_MultiMedia;
import org.ofdrw.core.basicStructure.res.MediaType;
import org.ofdrw.core.basicStructure.res.OFDResource;
import org.ofdrw.core.basicStructure.res.Res;
import org.ofdrw.core.basicStructure.res.resources.*;
import org.ofdrw.core.basicType.ST_Loc;
import org.ofdrw.core.basicType.ST_RefID;
import org.ofdrw.core.compositeObj.CT_VectorG;
import org.ofdrw.core.pageDescription.CT_GraphicUnit;
import org.ofdrw.core.pageDescription.color.colorSpace.CT_ColorSpace;
import org.ofdrw.core.pageDescription.drawParam.CT_DrawParam;
import org.ofdrw.core.text.font.CT_Font;
import org.ofdrw.pkg.container.OFDDir;
import org.ofdrw.reader.tools.ImageUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * resource manager（只读）
 * <p>
 * 使用ID随机访问文档中出现的resource object
 * <p>
 * 包括 public resource sequence（PublicRes） 和 文档资源序列（DocumentRes）
 * <p>
 * 注意：resource manager提供的resource object均为只读对象（副本），不允许对资源进行修改,所有提供的对象
 * 中文档的相对路径均在加载时转换为absolute path。
 *
 * @author Quan Guanyu
 * @since 2021-04-10 11:06:00
 */
public class ResourceManage {
    /**
     * color space
     */
    private final Map<String, CT_ColorSpace> colorSpaceMap = new HashMap<>();
    /**
     * drawing parameters
     */
    private final Map<String, CT_DrawParam> drawParamMap = new HashMap<>();
    /**
     * glyph
     */
    private final Map<String, CT_Font> fontMap = new HashMap<>();
    /**
     * 多媒体对象
     */
    private final Map<String, CT_MultiMedia> multiMediaMap = new HashMap<>();
    /**
     * 矢量图像
     */
    private final Map<String, CT_VectorG> compositeGraphicUnitMap = new HashMap<>();

    /**
     * 所有资源和ID的映射表
     */
    private final Map<String, OFDElement> allResMap = new HashMap<>();

    /**
     * document common data structure
     */
    private CT_CommonData commonData;


    private final OFDReader ofdReader;

    /**
     * 创建resource manager
     * <p>
     * 选择默认文档（Doc_0）进行资源的加载
     *
     * @param ofdReader OFD parser
     */
    public ResourceManage(OFDReader ofdReader) {
        this.ofdReader = ofdReader;
        try {
            loadDefaultDoc();
        } catch (Exception e) {
            throw new RuntimeException("文档结构解析异常", e);
        }
    }

    /**
     * 指定文档创建resource manager
     *
     * @param ofdReader OFD parser
     * @param docNum    文档序号，从0起
     */
    public ResourceManage(OFDReader ofdReader, int docNum) {
        this.ofdReader = ofdReader;
        try {
            loadDoc(docNum);
        } catch (Exception e) {
            throw new RuntimeException("文档结构解析异常", e);
        }
    }

    /**
     * get drawing parameters
     * <p>
     * Note: all resource objects provided by the resource manager are read-only (copies); modifications are not allowed.
     *
     * @param id resource ID
     * @return drawing parameters; returns null if not present
     */
    public CT_DrawParam getDrawParam(String id) {
        return drawParamMap.get(id);
    }

    /**
     * 递归的解析drawing parameters并覆盖配置参数内容
     *
     * @param id resource ID
     * @return drawing parameters; returns null if not present
     */
    public CT_DrawParam getDrawParamFinal(String id) {
        return getDrawParamFinal(id, new ArrayList<>());
    }

    /**
     * 递归的解析drawing parameters并覆盖配置参数内容（内部使用，带访问路径跟踪）
     *
     * @param id resource ID
     * @param visited 访问路径（用于检测循环引用）
     * @return drawing parameters; returns null if not present
     */
    private CT_DrawParam getDrawParamFinal(String id, List<String> visited) {
        if (id == null) {
            return null;
        }
        CT_DrawParam current = drawParamMap.get(id);
        // 使用继承属性填充本机
        return superDrawParam(current, visited);
    }

    /**
     * 寻找继承属性用于覆盖当前为空的属性
     *
     * @param current 当前需要子节点
     * @param visited 访问路径（用于检测循环引用）
     * @return 补全后的子节点副本
     */
    public CT_DrawParam superDrawParam(CT_DrawParam current, List<String> visited) {
        if (current == null) {
            return null;
        }
        // 复制为副本防止造成污染
        current = current.clone();
        ST_RefID relative = current.getRelative();
        if (relative == null) {
            return current;
        }
        // 检测循环引用
        String relativeId = relative.toString();
        if (visited.contains(relativeId)) {
            // 发现循环引用，直接返回当前副本
            return current;
        }
        // 添加当前节点ID到访问路径
        if (current.getID() != null) {
            visited.add(current.getID().toString());
        }
        // 递归的寻找上一级继承的参数的最终参数
        CT_DrawParam parent = getDrawParamFinal(relativeId, visited);
        // 移除当前节点ID（避免影响其他调用链）
        if (current.getID() != null && !visited.isEmpty()) {
            visited.remove(visited.size() - 1);
        }
        if (parent == null) {
            return current;
        }
        // 本次绘制属性将覆盖其引用的drawing parameters中的同名属性。
        if (current.attributeValue("LineWidth") == null
                && parent.attributeValue("LineWidth") != null) {
            current.addAttribute("LineWidth", parent.attributeValue("LineWidth"));
        }
        if (current.attributeValue("Join") == null
                && parent.attributeValue("Join") != null) {
            current.addAttribute("Join", parent.attributeValue("Join"));
        }
        if (current.attributeValue("Cap") == null
                && parent.attributeValue("Cap") != null) {
            current.addAttribute("Cap", parent.attributeValue("Cap"));
        }
        if (current.attributeValue("DashOffset") == null
                && parent.attributeValue("DashOffset") != null) {
            current.addAttribute("DashOffset", parent.attributeValue("DashOffset"));
        }
        if (current.attributeValue("DashPattern") == null
                && parent.attributeValue("DashPattern") != null) {
            current.addAttribute("DashPattern", parent.attributeValue("DashPattern"));
        }
        if (current.attributeValue("MiterLimit") == null
                && parent.attributeValue("MiterLimit") != null) {
            current.addAttribute("MiterLimit", parent.attributeValue("MiterLimit"));
        }
        if (current.getFillColor() == null
                && parent.getFillColor() != null) {
            current.setFillColor(parent.getFillColor());
        }
        if (current.getStrokeColor() == null
                && parent.getStrokeColor() != null) {
            current.setStrokeColor(parent.getStrokeColor());
        }
        return current;
    }

    /**
     * 寻找继承属性用于覆盖当前为空的属性
     *
     * @param current 当前需要子节点
     * @return 补全后的子节点副本
     */
    public CT_DrawParam superDrawParam(CT_DrawParam current) {
        return superDrawParam(current, new ArrayList<>());
    }

    /**
     * 补充 图元信息 通过引用的配置参数
     * <p>
     * 尝试将图元中描述的绘制信息和引用的drawing parameters进行合并
     *
     * @param current 当前图元对象
     * @return 继承到的drawing parameters
     */
    public CT_DrawParam superDrawParam(CT_GraphicUnit<?> current) {
        if (current == null) {
            return null;
        }
        ST_RefID drawParam = current.getDrawParam();
        if (drawParam == null) {
            return null;
        }
        CT_DrawParam parent = getDrawParamFinal(drawParam.toString());
        if (parent == null) {
            return null;
        }
        // 本次绘制属性将覆盖其引用的drawing parameters中的同名属性。
        if (current.attributeValue("LineWidth") == null
                && parent.attributeValue("LineWidth") != null) {
            current.addAttribute("LineWidth", parent.attributeValue("LineWidth"));
        }
        if (current.attributeValue("Join") == null
                && parent.attributeValue("Join") != null) {
            current.addAttribute("Join", parent.attributeValue("Join"));
        }
        if (current.attributeValue("Cap") == null
                && parent.attributeValue("Cap") != null) {
            current.addAttribute("Cap", parent.attributeValue("Cap"));
        }
        if (current.attributeValue("DashOffset") == null
                && parent.attributeValue("DashOffset") != null) {
            current.addAttribute("DashOffset", parent.attributeValue("DashOffset"));
        }
        if (current.attributeValue("DashPattern") == null
                && parent.attributeValue("DashPattern") != null) {
            current.addAttribute("DashPattern", parent.attributeValue("DashPattern"));
        }
        if (current.attributeValue("MiterLimit") == null
                && parent.attributeValue("MiterLimit") != null) {
            current.addAttribute("MiterLimit", parent.attributeValue("MiterLimit"));
        }
        return parent;


    }

    /**
     * 获取多媒体对象
     * <p>
     * Note: all resource objects provided by the resource manager are read-only (copies); modifications are not allowed.
     *
     * @param id resource ID
     * @return 多媒体对象，不存在返回null
     */
    public CT_MultiMedia getMultiMedia(String id) {
        return multiMediaMap.get(id);
    }

    /**
     * 获取image资源的image对象
     *
     * @param refID 引用ID
     * @return image对象
     * @throws IOException IO exception
     */
    public BufferedImage getImage(String refID) throws IOException {
        CT_MultiMedia multiMedia = getMultiMedia(refID);
        if (multiMedia == null) return null;
        if (MediaType.Image != multiMedia.getType()) return null;

        // 该路径在解析是已经被映射成absolute path
        ST_Loc loc = multiMedia.getMediaFile();
        if (loc == null) return null;
        final ResourceLocator rl = ofdReader.getResourceLocator();
        rl.save();
        try {
            final Path imgPath = rl.getFile(loc);
            try (InputStream in = Files.newInputStream(imgPath)) {
                final String fileName = loc.getFileName().toLowerCase();
                if (fileName.endsWith(".jb2") || fileName.endsWith(".gbig2")) {
                    return ImageUtils.readJB2(in);
                } else {
                    return ImageIO.read(in);
                }
            }
        } finally {
            rl.restore();
        }
    }

    /**
     * 获取image资源的image对象
     *
     * @param refID 引用ID
     * @return byte[]
     * @throws IOException IO exception
     */
    public byte[] getImageByteArray(String refID) throws IOException {
        CT_MultiMedia multiMedia = getMultiMedia(refID);
        if (multiMedia == null) return null;
        if (MediaType.Image != multiMedia.getType()) return null;

        // 该路径在解析是已经被映射成absolute path
        ST_Loc loc = multiMedia.getMediaFile();
        if (loc == null) return null;
        final ResourceLocator rl = ofdReader.getResourceLocator();
        rl.save();
        try {
            final Path imgPath = rl.getFile(loc);
            try (InputStream in = Files.newInputStream(imgPath)) {
                return IOUtils.toByteArray(in);
            }
        } finally {
            rl.restore();
        }
    }

    /**
     * 获取image对象的图像
     * <p>
     * 如果image存在蒙板，那么返回蒙板后的图像
     *
     * @param imageObject image对象
     * @return image对象（蒙板后的图像）
     * @throws IOException image操作IO exception
     */
    public BufferedImage getImage(ImageObject imageObject) throws IOException {
        final ST_RefID resourceID = imageObject.getResourceID();
        if (resourceID == null) {
            return null;
        }
        BufferedImage image = getImage(resourceID.toString());
        if (image == null) return null;
        if (imageObject.getImageMask() != null) {
            BufferedImage mask = getImage(imageObject.getImageMask().toString());
            if (mask != null) image = ImageUtils.renderMask(image, mask);
        }
        return image;
    }

    /**
     * 获取 glyph
     * <p>
     * Note: all resource objects provided by the resource manager are read-only (copies); modifications are not allowed.
     *
     * @param id resource ID
     * @return glyph，不存在返回null
     */
    public CT_Font getFont(String id) {
        return fontMap.get(id);
    }

    /**
     * 获取color space
     * <p>
     * Note: all resource objects provided by the resource manager are read-only (copies); modifications are not allowed.
     *
     * @param id resource ID，如果为null则返回 Document.xml CommonData 中的默认color space
     * @return color space，不存在返回null(null时默认RGB)
     */
    public CT_ColorSpace getColorSpace(String id) {
        if (id == null) {
            ST_RefID defaultCSIdRef = commonData.getDefaultCS();
            if (defaultCSIdRef == null) {
                return null;
            }
            id = defaultCSIdRef.toString();
        }

        return colorSpaceMap.get(id);
    }


    /**
     * 获取矢量图形
     * <p>
     * Note: all resource objects provided by the resource manager are read-only (copies); modifications are not allowed.
     *
     * @param id resource ID
     * @return 矢量图形，不存在返回null
     */
    public CT_VectorG getCompositeGraphicUnit(String id) {
        return compositeGraphicUnitMap.get(id);
    }

    /**
     * 加载指定文档的资源
     * <p>
     * 由于每个文档的ID体系都是独立的，
     * <p>
     * 所以资源也是独立的，因此每次加载都会对上一个文档的资源进行清理。
     *
     * @param docNum 文档序号，从0起
     * @return this
     * @throws IOException       file read/write exception
     * @throws DocumentException document parsing exception
     */
    public ResourceManage loadDoc(int docNum) throws IOException, DocumentException {

        final OFDDir ofdDir = ofdReader.getOFDDir();
        // 根目录: /OFD.xml
        final OFD ofd = ofdDir.getOfd();
        final DocBody docBody = ofd.getDocBody(docNum);

        // 由于每个文档的ID体系都是独立的，所以资源也是独立的
        // 这里对上一个文档的资源进行清理
        colorSpaceMap.clear();
        drawParamMap.clear();
        fontMap.clear();
        multiMediaMap.clear();
        compositeGraphicUnitMap.clear();
        // 重新加载资源
        loadDocRes(docBody);
        return this;
    }

    /**
     * 多文档资源加载
     *
     * @throws IOException       file read/write exception
     * @throws DocumentException document parsing exception
     */
    private void loadDefaultDoc() throws IOException, DocumentException {
        final OFDDir ofdDir = ofdReader.getOFDDir();
        // 根目录: /OFD.xml
        final OFD ofd = ofdDir.getOfd();
        // 加载OFD中出现的第一个文档
        loadDocRes(ofd.getDocBody());
    }

    /**
     * 加载文档中的资源
     *
     * @param docBody 文档描述信息
     * @throws IOException       file read/write exception
     * @throws DocumentException document parsing exception
     */
    private void loadDocRes(DocBody docBody) throws IOException, DocumentException {
        final ResourceLocator rl = ofdReader.getResourceLocator();
        try {
            rl.save();
            final ST_Loc docRoot = docBody.getDocRoot();
            // path resolver gets and caches the virtual container
            Document document = rl.get(docRoot, Document::new);
            rl.cd(docRoot.parent());

            commonData = new CT_CommonData((Element) document.getCommonData().clone());
            // public resource（PublicRes）
            for (ST_Loc pubResLoc : commonData.getPublicResList()) {
                loadResFile(rl, pubResLoc);
            }

            // 文档资源序列（DocumentRes）
            for (ST_Loc docResLoc : commonData.getDocumentResList()) {
                loadResFile(rl, docResLoc);
            }

            // 页面资源，暂时ignored
        } finally {
            rl.restore();
        }
    }

    /**
     * 加载资源文文件中描述的resource object
     * <p>
     * 该方法不应该抛出异常所有异常均应该被ignored以便程序继续执行
     *
     * @param rl     resource loader
     * @param resLoc resource file位置
     */
    private void loadResFile(ResourceLocator rl, ST_Loc resLoc) {
        if (resLoc == null) {
            return;
        }
        try {
            rl.save();
            rl.cd(resLoc.parent());
            Res res = rl.get(resLoc, Res::new);
            // 如果resource file的通用存储路径
            final ST_Loc baseLoc = res.getBaseLoc();
            // 遍历每一个resource object
            for (OFDResource ofdResource : res.getResources()) {
                // color space
                if (ofdResource instanceof ColorSpaces) {
                    for (CT_ColorSpace colorSpace : ((ColorSpaces) ofdResource).getColorSpaces()) {
                        // make a copy as a read-only object
                        CT_ColorSpace item = new CT_ColorSpace((Element) colorSpace.clone());
                        // 如果file path存在，则转换为absolute path
                        if (item.getProfile() != null) {
                            // convert file path to absolute address
                            ST_Loc absProfile = abs(rl, baseLoc, item.getProfile());
                            // 替换地址
                            item.setProfile(absProfile);
                        }
                        colorSpaceMap.put(item.getID().toString(), item);
                        allResMap.put(item.getID().toString(), item);
                    }
                    continue;
                }
                // drawing parameters
                if (ofdResource instanceof DrawParams) {
                    for (CT_DrawParam drawParam : ((DrawParams) ofdResource).getDrawParams()) {
                        // make a copy as a read-only object
                        CT_DrawParam item = new CT_DrawParam(drawParam.clone());
                        drawParamMap.put(item.getID().toString(), item);
                        allResMap.put(item.getID().toString(), item);
                    }
                    continue;
                }
                // font
                if (ofdResource instanceof Fonts) {
                    for (CT_Font font : ((Fonts) ofdResource).getFonts()) {
                        // make a copy as a read-only object
                        CT_Font item = new CT_Font((Element) font.clone());
                        // 如果地址存在，则转换为absolute path
                        if (item.getFontFile() != null) {
                            // convert file path to absolute address
                            ST_Loc absFontFile = abs(rl, baseLoc, item.getFontFile());
                            // 替换地址
                            item.setFontFile(absFontFile);
                        }
                        fontMap.put(item.getID().toString(), item);
                        allResMap.put(item.getID().toString(), item);
                    }
                    continue;
                }
                // 媒体对象
                if (ofdResource instanceof MultiMedias) {
                    for (CT_MultiMedia multiMedia : ((MultiMedias) ofdResource).getMultiMedias()) {
                        // make a copy as a read-only object
                        CT_MultiMedia item = new CT_MultiMedia((Element) multiMedia.clone());
                        // 如果地址存在，则转换为absolute path
                        if (item.getMediaFile() != null) {
                            // convert file path to absolute address
                            ST_Loc absMediaFile = abs(rl, baseLoc, item.getMediaFile());
                            item.setMediaFile(absMediaFile);
                        }
                        multiMediaMap.put(item.getID().toString(), item);
                        allResMap.put(item.getID().toString(), item);
                    }
                    continue;
                }

                // 矢量图形
                if (ofdResource instanceof CompositeGraphicUnits) {
                    for (CT_VectorG ctVectorG : ((CompositeGraphicUnits) ofdResource).getCompositeGraphicUnits()) {
                        // make a copy as a read-only object
                        CT_VectorG item = new CT_VectorG((Element) ctVectorG.clone());
                        compositeGraphicUnitMap.put(item.getID().toString(), item);
                        allResMap.put(item.getID().toString(), item);
                    }
                }
            }
        } catch (Exception e) {
            // System.out.println("[可ignored] 无法解析resource description file " + resLoc.toString() + " " + e.getMessage());
        } finally {
            rl.restore();
        }
    }

    /**
     * 获取资源的absolute address
     *
     * @param rl     resource loader
     * @param base   resource file的通用存储路径
     * @param target 资源file path
     * @return resource fileabsolute address
     */
    private ST_Loc abs(ResourceLocator rl, ST_Loc base, ST_Loc target) {
        // 目标路径不存在那么就返还null
        if (target == null) {
            return null;
        }
        if (target.isRootPath()) {
            // absolute path
            return target;
        }

        ST_Loc absLoc;
        if (base != null) {
            if (base.isRootPath()) {
                // resource file的通用存储路径 为根路径时直接在此基础上拼接
                absLoc = base;
            } else {
                // resource file的通用存储路径 为相对路径时，以结合当前resource file位置推断当前路径
                absLoc = rl.getAbsTo(base);
            }
            absLoc = absLoc.cat(target);
        } else {
            // 不存在 通用存储路径 直接根据但前目录位置获取到absolute path
            absLoc = rl.getAbsTo(target);
        }
        return absLoc;
    }

    /**
     * 获取文档中所有 color space
     * <p>
     * Note: all resource objects provided by the resource manager are read-only (copies); modifications are not allowed.
     *
     * @return color space列表
     */
    public List<CT_ColorSpace> getColorSpaces() {
        return new ArrayList<CT_ColorSpace>(colorSpaceMap.values());
    }

    /**
     * 获取文档中所有 drawing parameters
     * <p>
     * Note: all resource objects provided by the resource manager are read-only (copies); modifications are not allowed.
     *
     * @return drawing parameters
     */
    public List<CT_DrawParam> getDrawParams() {
        return new ArrayList<>(drawParamMap.values());
    }

    /**
     * 获取文档中所有 glyph
     * <p>
     * Note: all resource objects provided by the resource manager are read-only (copies); modifications are not allowed.
     *
     * @return glyph
     */
    public List<CT_Font> getFonts() {
        return new ArrayList<>(fontMap.values());
    }

    /**
     * 获取文档中所有 媒体对象
     * <p>
     * Note: all resource objects provided by the resource manager are read-only (copies); modifications are not allowed.
     *
     * @return 媒体对象
     */
    public List<CT_MultiMedia> getMultiMedias() {
        return new ArrayList<>(multiMediaMap.values());
    }

    /**
     * 获取文档中所有 矢量图形
     * <p>
     * Note: all resource objects provided by the resource manager are read-only (copies); modifications are not allowed.
     *
     * @return 矢量图形
     */
    public List<CT_VectorG> getCompositeGraphicUnits() {
        return new ArrayList<>(compositeGraphicUnitMap.values());
    }

    /**
     * @return 解析器
     */
    public OFDReader getOfdReader() {
        return ofdReader;
    }

    /**
     * 通过ID获取资源
     * <p>
     * 如果资源不存在，那么返回null
     *
     * @param id resource ID
     * @return resource object，null
     */
    public OFDElement get(String id) {
        return allResMap.get(id);
    }
}

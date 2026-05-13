package org.ofdrw.reader;

import org.apache.commons.io.FileUtils;
import org.dom4j.DocumentException;
import org.ofdrw.core.OFDElement;
import org.ofdrw.core.annotation.Annotations;
import org.ofdrw.core.annotation.pageannot.AnnPage;
import org.ofdrw.core.annotation.pageannot.PageAnnot;
import org.ofdrw.core.attachment.Attachments;
import org.ofdrw.core.attachment.CT_Attachment;
import org.ofdrw.core.basicStructure.doc.CT_CommonData;
import org.ofdrw.core.basicStructure.doc.CT_PageArea;
import org.ofdrw.core.basicStructure.doc.Document;
import org.ofdrw.core.basicStructure.ofd.DocBody;
import org.ofdrw.core.basicStructure.pageObj.CT_TemplatePage;
import org.ofdrw.core.basicStructure.pageObj.Page;
import org.ofdrw.core.basicStructure.pageObj.Template;
import org.ofdrw.core.basicStructure.pageObj.layer.Type;
import org.ofdrw.core.basicStructure.pageTree.Pages;
import org.ofdrw.core.basicType.ST_Box;
import org.ofdrw.core.basicType.ST_ID;
import org.ofdrw.core.basicType.ST_Loc;
import org.ofdrw.core.signatures.Signature;
import org.ofdrw.core.signatures.Signatures;
import org.ofdrw.gm.ses.parse.SESVersionHolder;
import org.ofdrw.gm.ses.parse.VersionParser;
import org.ofdrw.pkg.container.DocDir;
import org.ofdrw.pkg.container.OFDDir;
import org.ofdrw.reader.model.AnnotionEntity;
import org.ofdrw.reader.model.StampAnnotEntity;
import org.ofdrw.reader.model.TemplatePageEntity;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * OFD parser
 *
 * @author Quan Guanyu
 * @since 2020-04-01 21:39:25
 */
public class OFDReader implements Closeable {

    /**
     * Reader工作过程中的working directory
     * <p>
     * used to store decompressed OFD document container content
     */
    private Path workDir;

    /**
     * OFD虚拟容器对象
     */
    private OFDDir ofdDir;

    /**
     * resource locator
     * <p>
     * resolve path to get resource
     */
    protected ResourceLocator rl;

    /**
     * 是否已经关闭文档
     */
    private boolean closed = false;

    /**
     * resource loader
     */
    private ResourceManage resMgt;

    private OFDReader() {
    }

    public Path getWorkDir() {
        return workDir;
    }

    /**
     * 设置 OFD解压后最大占用文件大小
     * <p>
     * 默认值： 100MB
     *
     * @param size 解压文件大小，单位字节（Byte）
     * @deprecated 该参数已经弃用。
     */
    @Deprecated
    public static void setZipFileMaxSize(long size) {
        ZipUtil.setMaxSize(size);
    }

    /**
     * construct an OFDReader
     *
     * @param ofdFile OFD file
     * @throws IOException OFD file IO exception
     */
    public OFDReader(Path ofdFile) throws IOException {
        if (ofdFile == null || Files.notExists(ofdFile)) {
            throw new IllegalArgumentException("文件位置(ofdFile)不正确");
        }
        workDir = Files.createTempDirectory("ofd-tmp-");
        // decompress document to temporary working directory
        ZipUtil.unZipFileByApacheCommonCompress(ofdFile.toFile(), workDir.toAbsolutePath().toString() + File.separator);
        ofdDir = new OFDDir(workDir);
        // create resource locator
        rl = new ResourceLocator(ofdDir);
        resMgt = new ResourceManage(this);
    }

    /**
     * construct an OFDReader
     *
     * @param ofdFileLoc OFD file位置，例如：”/home/user/myofd.ofd“
     * @throws IOException OFD file IO exception
     */
    public OFDReader(String ofdFileLoc) throws IOException {
        this(Paths.get(ofdFileLoc));
    }

    /**
     * construct an OFDReader
     *
     * @param stream OFD file输入流，流由调用者负责关闭。
     * @throws IOException OFD file IO exception
     */
    public OFDReader(InputStream stream) throws IOException {
        if (stream == null) {
            throw new IllegalArgumentException("文件输入流(stream)不正确");
        }
        workDir = Files.createTempDirectory("ofd-tmp-");
        // decompress document to temporary working directory
        ZipUtil.unZipFileByApacheCommonCompress(stream, workDir.toAbsolutePath() + File.separator);
        ofdDir = new OFDDir(workDir);
        // create resource locator
        rl = new ResourceLocator(ofdDir);
        resMgt = new ResourceManage(this);
    }

    /**
     * 因一些ofd文件无法使用ZipUtil解压缩，可以让用户自己在外面解压缩好后，传入根目录创建
     * 例如用户可以使用unzip或者unar等命令行方式解压缩，因此通过参数控制是否删除目录。
     *
     * @param unzippedPathRoot 已经解压的OFD根目录位置
     * @param deleteOnClose    退出时是否删除 unzippedPathRoot 文件， true - 退出时删除；false - 不删除
     */
    public OFDReader(String unzippedPathRoot, boolean deleteOnClose) {
        workDir = Paths.get(unzippedPathRoot);
        if (Files.notExists(workDir) || !Files.isDirectory(workDir)) {
            throw new IllegalArgumentException("文件位置(unzippedPathRoot)不正确");
        }
        ofdDir = new OFDDir(workDir);
        // create resource locator
        rl = new ResourceLocator(ofdDir);
        // 通过参数来指定是否删除外部文档，保证谁创建的目录谁负责这个原则
        if (!deleteOnClose) {
            closed = true;
        }
        resMgt = new ResourceManage(this);
    }

    /**
     * 获取文档虚拟容器
     *
     * @return OFD document虚拟容器
     */
    public OFDDir getOFDDir() {
        return ofdDir;
    }

    /**
     * 获取默认文档Doc_0中的签名列表文件的absolute path
     *
     * @return 签名列表文件absolute path
     * @throws BadOFDException 错误OFD结构和文件格式导致结构无法解析
     */
    public ST_Loc getDefaultDocSignaturesPath() {
        try {
            rl.save();
            rl.cd("/");
            DocBody docBody = ofdDir.getOfd().getDocBody();
            // 签名列表file path
            ST_Loc loc = docBody.getSignatures();
            if (loc == null) {
                return null;
            }
            // 转化为absolute path
            String signListFileAbsPath = rl.toAbsolutePath(loc);
            return ST_Loc.getInstance(signListFileAbsPath);
        } catch (FileNotFoundException | DocumentException e) {
            throw new BadOFDException("错误OFD结构和文件格式", e);
        } finally {
            rl.restore();
        }
    }

    /**
     * 获取默认的签名列表对象
     * <p>
     * 如果file not found则返还null
     *
     * @return 签名列表对象
     */
    public Signatures getDefaultSignatures() {
        ST_Loc signaturesLoc = getDefaultDocSignaturesPath();
        // 文件中不存在 Signatures.xml 或是 路基上的file not found，都认为file not found
        if (signaturesLoc == null || !(rl.exist(signaturesLoc.toString()))) {
            return null;
//            throw new BadOFDException("OFD document中不存在Signatures.xml");
        }
        // 获取签名列表对象
        try {
            return rl.get(signaturesLoc, Signatures::new);
        } catch (FileNotFoundException | DocumentException e) {
            throw new BadOFDException("错误OFD结构和文件格式", e);
        }
    }


    /**
     * 文档是否containsnumber签名
     *
     * @return true - 含有；false - 不含；
     */
    public boolean hasSignature() {
        DocBody docBody = null;
        try {
            docBody = ofdDir.getOfd().getDocBody();
            ST_Loc signaturesLoc = docBody.getSignatures();
            return signaturesLoc != null;
        } catch (FileNotFoundException | DocumentException e) {
            throw new BadOFDException("错误OFD结构和文件格式", e);
        }
    }

    /**
     * 获取注解列表文件对象
     * <p>
     * 如果文档中没有注释文件，那么返还null
     *
     * @return 注解列表文件对象或null
     */
    public Annotations getAnnotations() {
        try {
            // path resolver gets and caches the virtual container
            Document document = cdDefaultDoc();

            ST_Loc annotations = document.getAnnotations();
            if (annotations == null || !(rl.exist(annotations.toString()))) {
                return null;
            }
            return rl.get(annotations, Annotations::new);
        } catch (FileNotFoundException | DocumentException e) {
            throw new BadOFDException("OFD解析失败，原因:" + e.getMessage(), e);
        } finally {
            // restore the original workspace
            rl.restore();
        }
    }

    /**
     * 获取OFD含有的总页面数量
     *
     * @return 总页数
     */
    public int getNumberOfPages() {
        try {
            // path resolver gets and caches the virtual container
            Document document = cdDefaultDoc();
            Pages pages = document.getPages();
            return pages.getSize();
        } catch (FileNotFoundException | DocumentException e) {
            throw new BadOFDException("OFD解析失败，原因:" + e.getMessage(), e);
        } finally {
            // restore the original workspace
            rl.restore();
        }
    }

    /**
     * 获取page information
     *
     * @param pageNum page number, starting from 1
     * @return page information
     */
    public PageInfo getPageInfo(int pageNum) {
        if (pageNum <= 0) {
            throw new NumberFormatException("page number(pageNum)不能小于0");
        }
        try {
            rl.save();
            int index = pageNum - 1;
            // path resolver gets and caches the virtual container
            Document document = cdDefaultDoc();
            Pages pages = document.getPages();
            List<org.ofdrw.core.basicStructure.pageTree.Page> pageList = pages.getPages();
            if (index >= pageList.size()) {
                throw new NumberFormatException(pageNum + "超过最大page number:" + pageList.size());
            }
            // get page path
            ST_Loc pageLoc = pageList.get(index).getBaseLoc();

            Page obj = rl.get(pageLoc, Page::new);
            // 获取页面的容器absolute path
            pageLoc = rl.getAbsTo(pageLoc);
            ST_Box pageSize = getPageSize(obj);

            // 加载模板
            ArrayList<TemplatePageEntity> templatePages = new ArrayList<>();
            for (Template item : obj.getTemplates()) {
                TemplatePageEntity template = getTemplate(item.getTemplateID().toString());
                Type type = Type.getInstance(item.attributeValue("ZOrder"));
                template.setOrder(type);
                templatePages.add(template);
            }

            // Page_N 数组
            int n = index;
            String pageNName = new ST_Loc(pageLoc.parent()).getFileName().toLowerCase();
            if (pageNName.matches("page_\\d+")) {
                try {
                    n = Integer.parseInt(pageNName.replace("page_", ""));
                } catch (NumberFormatException e) {
                    // ignore
                }
            }

            return new PageInfo()
                    .setIndex(pageNum)
                    .setId(pageList.get(index).getID())
                    .setObj(obj)
                    .setSize(pageSize.clone())
                    .setPageAbsLoc(pageLoc)
                    .setTemplates(templatePages)
                    .setPageN(n);
        } catch (FileNotFoundException | DocumentException e) {

            throw new RuntimeException("OFD解析失败，原因:" + e.getMessage(), e);
        } finally {
            // restore the original workspace
            rl.restore();
        }
    }

    /**
     * 获取 page information集合
     *
     * @return page information集合
     */
    public List<PageInfo> getPageList() {
        // 获取总page number数
        final int numberOfPages = getNumberOfPages();
        List<PageInfo> res = new ArrayList<>(numberOfPages);
        for (int i = 0; i < numberOfPages; i++) {
            PageInfo pageInfo = getPageInfo(i + 1);
            res.add(pageInfo);
        }
        return res;
    }

    /**
     * 解析页面模板对象
     *
     * @param id 模板ID
     * @return 模板实体，如果模板不存在返还null
     */
    public TemplatePageEntity getTemplate(String id) {
        if (id == null || id.trim().length() == 0) {
            return null;
        }
        try {
            final Document document = cdDefaultDoc();
            final CT_CommonData commonData = document.getCommonData();
            TemplatePageEntity res = null;
            for (CT_TemplatePage item : commonData.getTemplatePages()) {
                String itemId = item.getID().toString();
                if (id.equals(itemId)) {
                    ST_Loc loc = item.getBaseLoc();
                    if (loc == null) {
                        break;
                    }
                    // 加载模板内容
                    Page page = rl.get(loc, Page::new);
                    res = new TemplatePageEntity(item, page);
                    break;
                }
            }
            return res;
        } catch (DocumentException | FileNotFoundException e) {
            return null;
        } finally {
            rl.restore();
        }
    }

    /**
     * 切换目录到指定的文档下
     * <p>
     * 该操作将会导致resource loader变更目录
     * <p>
     * 如果需要恢复被切换的目录请主动在外部调用restore
     *
     * @param numOfDoc 文档序号
     * @return document object
     * @throws DocumentException     document parsing exception
     * @throws FileNotFoundException Document.xml file not found
     */
    public Document cdDoc(int numOfDoc) throws DocumentException, FileNotFoundException {
        rl.save();
        rl.cd("/");
        DocBody docBody = ofdDir.getOfd().getDocBody(numOfDoc);
        ST_Loc docRoot = docBody.getDocRoot();
        final Document document = rl.get(docRoot, Document::new);
        // path resolver gets and caches the virtual container
        rl.cd(docRoot.parent());
        return document;
    }

    /**
     * 获取document object
     *
     * @param numOfDoc 文档序号
     * @return document object
     * @throws DocumentException     document parsing exception
     * @throws FileNotFoundException Document.xml file not found
     */
    public Document getDoc(int numOfDoc) throws DocumentException, FileNotFoundException {
        rl.save();
        try {
            rl.cd("/");
            DocBody docBody = ofdDir.getOfd().getDocBody(numOfDoc);
            ST_Loc docRoot = docBody.getDocRoot();
            return rl.get(docRoot, Document::new);
        } finally {
            rl.restore();
        }
    }

    /**
     * 切换目录到默认的document directory下下
     * <p>
     * 该操作将会导致resource loader变更目录
     * <p>
     * 如果需要恢复被切换的目录请主动在外部调用restore
     *
     * @return document object
     * @throws DocumentException     document parsing exception
     * @throws FileNotFoundException Document.xml file not found
     */
    public Document cdDefaultDoc() throws DocumentException, FileNotFoundException {
        return cdDoc(0);
    }

    /**
     * 获取页面物理大小
     * <p>
     * 如果页面没有定义page area，则使用文件 CommonData中的定义
     *
     * @param page page object，null 表示获取默认物理页面大小。
     * @return 页面物理大小
     */
    public ST_Box getPageSize(Page page) {
        CT_PageArea pageArea = getPageArea(page);
        if (pageArea == null) {
            // 找不到就返回 A4
            return new ST_Box(0, 0, 210d, 297d);
        }
        return pageArea.getBox();
    }


    /**
     * 获取 page area
     *
     * @param num page number，从1起，若page number存在或超过最大最小page number则返还默认page area。
     * @return page area
     */
    public CT_PageArea getPageArea(int num) {
        final Page page = getPage(num);
        return getPageArea(page);
    }

    /**
     * 获取页面物理大小
     *
     * @param num page number，从1起，当page number不存在时返回文档中的默认 页面物理大小。
     * @return 页面物理尺寸
     */
    public ST_Box getPageSize(int num) {
        final Page page = getPage(num);
        return getPageSize(page);
    }

    /**
     * 获取 page area
     *
     * @param page page object，若为null 则返回文档默认page area大小。
     * @return page area
     */
    public CT_PageArea getPageArea(Page page) {
        CT_PageArea res = null;
        if (page != null) {
            // 页面存在时，从页面中读取区域信息，
            if (page.getArea() != null) {
                return page.getArea();
            }
            CT_PageArea tplArea = null;
            int biggestOrder = -1;
            // 从模板中获取
            for (Template item : page.getTemplates()) {
                TemplatePageEntity template = getTemplate(item.getTemplateID().toString());
                if (template == null || template.getPage() == null) continue;
                CT_PageArea area = template.getPage().getArea();
                int order = template.getZOrder().order();

                if (area != null && order > biggestOrder && area.getBox() != null) {
                    tplArea = area;
                    biggestOrder = order;
                }
            }
            res = tplArea;
        }

        // 若不存在则通过解析文档中的默认参数获取，从document information中获取。
        if (res == null) {
            Document document;
            try {
                document = ofdDir.obtainDocDefault().getDocument();
            } catch (FileNotFoundException | DocumentException e) {
                throw new BadOFDException("OFD解析失败，原因:" + e.getMessage(), e);
            }
            CT_CommonData commonData = document.getCommonData();
            res = commonData.getPageArea();
        }
        if (res == null) {
            // 当无法找到区域时，使用A4大小，以兼容防止后续解析NPE。
            return new CT_PageArea(0, 0, 210d, 297d);
        }
        return res;
    }

    /**
     * 通过页面page number获取page object
     *
     * @param pageNum page number，从1起
     * @return page object
     * @throws NumberFormatException page number小于1
     * @throws RuntimeException      路径不存在，或document parsing exception
     */
    public Page getPage(int pageNum) {
        ST_Loc pageLoc = getPageAbsLoc(pageNum);
        try {
            return rl.get(pageLoc, Page::new);
        } catch (FileNotFoundException | DocumentException e) {
            throw new RuntimeException("OFD解析失败，原因:" + e.getMessage(), e);
        }
    }

    /**
     * 获取制定page number页面文件的在文档中的absolute path （以 "/" 开头）
     *
     * @param pageNum page number
     * @return 页面文件的在文档中的absolute path
     * @throws NumberFormatException page number小于1
     * @throws RuntimeException      路径不存在
     */
    public ST_Loc getPageAbsLoc(int pageNum) {
        if (pageNum <= 0) {
            throw new NumberFormatException("page number(pageNum)不能小于0");
        }
        try {
            rl.save();
            int index = pageNum - 1;
            // path resolver gets and caches the virtual container
            Document document = cdDefaultDoc();
            Pages pages = document.getPages();
            List<org.ofdrw.core.basicStructure.pageTree.Page> pageList = pages.getPages();
            if (index >= pageList.size()) {
                throw new NumberFormatException(pageNum + "超过最大page number:" + pageList.size());
            }
            // get page path
            ST_Loc pageLoc = pageList.get(index).getBaseLoc();
            String absolutePath = rl.toAbsolutePath(pageLoc);
            return ST_Loc.getInstance(absolutePath);
        } catch (FileNotFoundException | DocumentException e) {
            throw new RuntimeException("OFD解析失败，原因:" + e.getMessage(), e);
        } finally {
            // restore the original workspace
            rl.restore();
        }
    }

    /**
     * 获取页面的object ID
     *
     * @param pageNum page number
     * @return object ID
     */
    public ST_ID getPageObjectId(int pageNum) {
        if (pageNum <= 0) {
            throw new NumberFormatException("page number(pageNum)不能小于0");
        }
        try {
            rl.save();
            int index = pageNum - 1;
            // path resolver gets and caches the virtual container
            Document document = cdDefaultDoc();
            Pages pages = document.getPages();
            List<org.ofdrw.core.basicStructure.pageTree.Page> pageList = pages.getPages();
            if (index >= pageList.size()) {
                throw new NumberFormatException(pageNum + "超过最大page number:" + pageList.size());
            }
            // get page path
            org.ofdrw.core.basicStructure.pageTree.Page page = pageList.get(index);
            if (page.getID() == null) {
                return null;
            }
            return page.getID();
        } catch (FileNotFoundException | DocumentException e) {
            throw new RuntimeException("OFD解析失败，原因:" + e.getMessage(), e);
        } finally {
            // restore the original workspace
            rl.restore();
        }
    }

    /**
     * 获取resource locator
     *
     * @return resource locator
     */
    public ResourceLocator getResourceLocator() {
        return rl;
    }

    /**
     * 获取所有附件对象
     * <p>
     * 注意该对象均为只读
     *
     * @return 附件数组，如果没有附件返还空数组
     * @throws BadOFDException 文档结构损坏
     */
    public List<CT_Attachment> getAttachmentList() {
        rl.save();
        try {
            DocDir docDir = ofdDir.obtainDocDefault();
            rl.cd(docDir);
            Document document = null;
            Attachments attachments = null;
            try {
                document = docDir.getDocument();
            } catch (FileNotFoundException | DocumentException e) {
                throw new BadOFDException(e);
            }
            ST_Loc attachmentsLoc = document.getAttachments();
            if (attachmentsLoc == null || (!rl.exist(attachmentsLoc.toString()))) {
                return new ArrayList<>();
            }
            try {
                // 获取附件目录
                attachments = rl.get(attachmentsLoc, Attachments::new);
            } catch (FileNotFoundException | DocumentException e) {
                System.err.println(">> 无法获取或解析Attachments.xml: " + e.getMessage());
                return new ArrayList<>();
            }

            String parent = attachmentsLoc.parent();
            if (parent != null) {
                rl.cd(parent);
            }

            List<CT_Attachment> res = attachments.getAttachments();
            if (!res.isEmpty()) {
                // 设置文件为absolute path，外部读取时工作路径不正确导致的file not found。
                for (CT_Attachment item : res) {
                    item.setFileLoc(rl.getAbsTo(item.getFileLoc()));
                }
            }
            return res;
        } finally {
            rl.restore();
        }
    }

    /**
     * get attachment file
     * <p>
     * 注意：该文件会在Close Reader时候被删除，请在之前复制到其他地方
     *
     * @param attachment 附件信息
     * @return attachment file path
     */
    public Path getAttachmentFile(CT_Attachment attachment) {
        if (attachment == null) {
            return null;
        }
        ST_Loc fileLoc = attachment.getFileLoc();
        try {
            return rl.getFile(fileLoc);
        } catch (FileNotFoundException e) {
            System.err.println(">> 无法根据附件对象的描述获取到附件: " + fileLoc.toString());
            return null;
        }
    }

    /**
     * 获取附件对象
     * <p>
     * 该方法不会恢复resource locator
     *
     * @param name attachment name
     * @return 附件对象
     */
    public CT_Attachment getAttachment(String name) {
        if (name == null || name.trim().length() == 0) {
            return null;
        }

        List<CT_Attachment> attachmentList = this.getAttachmentList();

        for (CT_Attachment attachment : attachmentList) {
            // 寻找匹配名称的附件
            if (attachment.getAttachmentName().equals(name)) {
                return attachment;
            }
        }
        return null;
    }


    /**
     * get attachment file
     * <p>
     * 注意：该文件会在Close Reader时候被删除，请在之前复制到其他地方
     *
     * @param name attachment name
     * @return attachment file path
     */
    public Path getAttachmentFile(String name) {
        if (name == null || name.trim().length() == 0) {
            return null;
        }
        rl.save();
        try {
            CT_Attachment attachment = getAttachment(name, rl);
            if (attachment == null) {
                return null;
            }
            ST_Loc fileLoc = attachment.getFileLoc();
            try {
                return rl.getFile(fileLoc);
            } catch (FileNotFoundException e) {
                System.err.println(">> 无法根据附件对象的描述获取到附件: " + fileLoc.toString());
                return null;
            }
        } finally {
            rl.restore();
        }
    }

    /**
     * 获取附件对象
     * <p>
     * 该方法不会恢复resource locator
     *
     * @param name attachment name
     * @param rl   resource locator
     * @return 附件对象
     */
    private CT_Attachment getAttachment(String name, ResourceLocator rl) {
        if (name == null || name.trim().length() == 0) {
            return null;
        }

        DocDir docDir = ofdDir.obtainDocDefault();
        rl.cd(docDir);
        Document document = null;
        Attachments attachments = null;
        try {
            document = docDir.getDocument();
        } catch (FileNotFoundException | DocumentException e) {
            throw new BadOFDException(e);
        }
        ST_Loc attachmentsLoc = document.getAttachments();
        if (attachmentsLoc == null || (!rl.exist(attachmentsLoc.toString()))) {
            // 文档中没有附件目录文件
            return null;
        }
        try {
            // 获取附件目录
            attachments = rl.get(attachmentsLoc, Attachments::new);
        } catch (FileNotFoundException | DocumentException e) {
            System.err.println(">> 无法获取或解析Attachments.xml: " + e.getMessage());
            return null;
        }

        String parent = attachmentsLoc.parent();
        if (parent != null) {
            rl.cd(parent);
        }

        for (CT_Attachment attachment : attachments.getAttachments()) {
            // 寻找匹配名称的附件
            if (attachment.getAttachmentName().equals(name)) {
                return attachment;
            }
        }
        return null;
    }


    /**
     * 获取默认文档中的seal/signature信息
     *
     * @return seal/signature信息
     */
    public List<StampAnnotEntity> getStampAnnots() {
        if (!hasSignature()) {
            // 没有签名的情况下返还空集合，防止NPE
            return Collections.emptyList();
        }

        try {
            rl.save();
            // 签名列表
            final Signatures sigFileList = getDefaultSignatures();
            if (sigFileList == null) {
                return Collections.emptyList();
            }
            ST_Loc signaturesLoc = getDefaultDocSignaturesPath();
            // 切换目录到 Signatures.xml所在目录
            rl.cd(signaturesLoc.parent());
            final List<Signature> sigInfoList = sigFileList.getSignatures();
            List<StampAnnotEntity> res = new ArrayList<>(sigInfoList.size());
            for (Signature sigInfoItem : sigInfoList) {
                ST_Loc signatureBaseLoc = sigInfoItem.getBaseLoc();
                rl.save();
                try {
                    // 签名描述文件
                    final org.ofdrw.core.signatures.sig.Signature sigDesp = rl.get(signatureBaseLoc, org.ofdrw.core.signatures.sig.Signature::new);
                    try {
                        rl.cd(signatureBaseLoc.parent());
                        ST_Loc signedValueLoc = sigDesp.getSignedValue();

                        rl.cd(signedValueLoc.parent());
                        // 获取signature value文件
                        final Path signedValueFile = rl.getFile(signedValueLoc);
                        // 解析电子seal/stamp
                        SESVersionHolder v = VersionParser.parseSES_SignatureVersion(Files.readAllBytes(signedValueFile));
                        res.add(new StampAnnotEntity(v, sigDesp.getSignedInfo()));
//                        SESVersionHolder sealHolder = null;
//                        if (sigDesp.getSignedInfo().getSeal() != null) {
//                            ST_Loc sealLoc = sigDesp.getSignedInfo().getSeal().getBaseLoc();
//                            rl.cd(signatureBaseLoc.parent());
//                            rl.cd(sealLoc.parent());
//                            final Path sealFile = rl.getFile(sealLoc);
//                            sealHolder = VersionParser.parseSES_SealVersion(Files.readAllBytes(sealFile));
//                        }
//                        res.add(new StampAnnotEntity(v, sealHolder, sigDesp.getSignedInfo()));
                    } finally {
                        rl.restore();
                    }
                } catch (Exception ignored) {
                    // ignored错误：
                    //      file not found
                    //      无法解析的seal/stamp，因为signature value可能是 电子signature value
                }
            }
            return res;
        } finally {
            rl.restore();
        }
    }

    /**
     * 获取默认文档中的所有注释信息
     *
     * @return 注释实体信息列表
     */
    public List<AnnotionEntity> getAnnotationEntities() {
        try {
            // path resolver gets and caches the virtual container
            Document document = cdDefaultDoc();
            final ST_Loc annInfosLoc = document.getAnnotations();
            if (annInfosLoc == null || (!rl.exist(annInfosLoc.toString()))) {
                return Collections.emptyList();
            }
            Annotations annotations = rl.get(annInfosLoc, Annotations::new);
            if (annotations == null) {
                return Collections.emptyList();
            }
            // 切换目录到 Annotations.xml所在文件目录
            rl.cd(annInfosLoc.parent());
            try {
                List<AnnPage> annPages = annotations.getPages();
                List<AnnotionEntity> res = new ArrayList<>(annPages.size());
                for (AnnPage annPage : annPages) {
                    try {
                        final ST_Loc fileLoc = annPage.getFileLoc();
                        final PageAnnot pageAnnot = rl.get(fileLoc, PageAnnot::new);
                        res.add(new AnnotionEntity(annPage.getPageID().toString(), pageAnnot.getAnnots()));
                    } catch (Exception ignore) {
                        // ignored无法加载的注释文件，尽力而为
                    }
                }
                return res;
            } finally {
                rl.restore();
            }
        } catch (DocumentException | FileNotFoundException e) {
            throw new RuntimeException("OFD解析失败，原因:" + e.getMessage(), e);
        }
    }

    /**
     * 获取resource manager
     * <p>
     * resource manager获取到的对象均为只读对象
     *
     * @return resource manager
     */
    public ResourceManage getResMgt() {
        return resMgt;
    }

    /**
     * 启用或关闭namespace严格解析模式
     * <p>
     * 启用严格模式后将会ignored非ofdnamespace的元素。
     * <p>
     * 默认：关闭严格模式
     *
     * @param enable true - 启用; false - 兼容模式（默认），兼容ofdnamespace
     */
    public static void setNamespaceStrictMode(boolean enable) {
        OFDElement.NSStrictMode = enable;
    }

    /**
     * 关闭文档
     * <p>
     * 删除工作区
     *
     * @throws IOException 工作区删除异常
     */
    @Override
    public void close() throws IOException {
        if (closed) {
            return;
        }
        closed = true;
        if (workDir != null && Files.exists(workDir)) {
            try {
                FileUtils.forceDelete(workDir.toFile());
            } catch (IOException e) {
                throw new IOException("无法删除Reader的工作空间，原因：" + e.getMessage(), e);
            }
        }
    }
}

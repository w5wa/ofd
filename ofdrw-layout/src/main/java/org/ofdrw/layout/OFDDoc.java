package org.ofdrw.layout;

import org.dom4j.DocumentException;
import org.ofdrw.core.attachment.Attachments;
import org.ofdrw.core.attachment.CT_Attachment;
import org.ofdrw.core.basicStructure.doc.CT_CommonData;
import org.ofdrw.core.basicStructure.doc.Document;
import org.ofdrw.core.basicStructure.ofd.DocBody;
import org.ofdrw.core.basicStructure.ofd.OFD;
import org.ofdrw.core.basicStructure.ofd.docInfo.CT_DocInfo;
import org.ofdrw.core.basicStructure.pageObj.Page;
import org.ofdrw.core.basicStructure.pageTree.Pages;
import org.ofdrw.core.basicType.ST_ID;
import org.ofdrw.core.basicType.ST_Loc;
import org.ofdrw.gv.GlobalVar;
import org.ofdrw.layout.edit.AdditionVPage;
import org.ofdrw.layout.edit.Annotation;
import org.ofdrw.layout.edit.AnnotationRender;
import org.ofdrw.layout.edit.Attachment;
import org.ofdrw.layout.element.Div;
import org.ofdrw.layout.edit.Watermark;
import org.ofdrw.layout.engine.*;
import org.ofdrw.layout.engine.render.RenderException;
import org.ofdrw.layout.exception.DocReadException;
import org.ofdrw.layout.handler.RenderFinishHandler;
import org.ofdrw.layout.handler.VPageHandler;
import org.ofdrw.pkg.container.DocDir;
import org.ofdrw.pkg.container.OFDDir;
import org.ofdrw.reader.OFDReader;
import org.ofdrw.reader.PageInfo;
import org.ofdrw.reader.ResourceLocator;

import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Virtual Document - OFD virtual document object
 * <p>
 * distinct from {@link org.ofdrw.core.basicStructure.doc.Document}
 * <p>
 * construct OFD document using API and package as OFD file.
 *
 * @author Quan Guanyu
 * @since 2020-3-17 20:13:51
 */
public class OFDDoc implements Closeable {

    /**
     * existing OFD document parser
     * <p>
     * only valid in edit mode
     */
    private OFDReader reader;
    /**
     * OFD packaging
     */
    private OFDDir ofdDir;
    /**
     * storage path for the packaged OFD document
     * outPath/outStream — choose one
     */
    private Path outPath;
    /**
     * output stream for the packaged OFD document
     * outPath/outStream — choose one
     */
    private OutputStream outStream;
    /**
     * maximum identifier value for all objects in the current document.
     * initial value: 0. MaxUnitID is mainly used for document editing;
     * when adding a new object to the document, a new
     * identifier must be allocated; the new identifier should be MaxUnitID + 1,
     * and this MaxUnitID value must be updated accordingly.
     */
    private AtomicInteger MaxUnitID = new AtomicInteger(0);

    /**
     * external resource manager
     */
    ResManager prm;

    /**
     * annotation renderer
     * <p>
     * initialized only when annotations need to be added
     */
    private AnnotationRender annotationRender;

    /**
     * flow layout element queue
     */
    private LinkedList<Div> streamQueue = new LinkedList<>();

    /**
     * fixed layout virtual page queue
     */
    private LinkedList<VirtualPage> vPageList = new LinkedList<>();

    /**
     * flow layout collection queue (for editing)
     */
    private LinkedList<StreamCollect> sPageList = new LinkedList<>();

    /**
     * page style
     * <p>
     * default: A4
     * <p>
     * margins: 2.54 cm top/bottom, 3.17 cm left/right.
     */
    private PageLayout pageLayout = PageLayout.A4();

    /**
     * document properties; this object is created during initialization and added to the document
     * a reference is kept here for convenience.
     */
    private CT_CommonData cdata;

    /**
     * whether the document is closed
     * true = closed, false = not closed
     */
    private boolean closed = false;

    /**
     * OFD document object
     */
    private Document ofdDocument;

    /**
     * document directory currently being operated
     */
    private DocDir operateDocDir;

    /**
     * callback function invoked when rendering is complete (optional)
     */
    private RenderFinishHandler renderingEndHandler;


    /**
     * 页面解析前处理器
     */
    private VPageHandler onPageHandler = null;
    
    /**
     * 文档水印
     */
    private Watermark defaultWatermark = null;


    /**
     * create an OFD file at the specified path
     *
     * @param outPath OFD output path
     */
    public OFDDoc(Path outPath) {
        this();
        if (outPath == null) {
            throw new IllegalArgumentException("OFD file storage path(outPath)为空");
        }
        if (Files.isDirectory(outPath)) {
            throw new IllegalArgumentException("OFD file storage path(outPath)不能是目录");
        }
        final Path parent = outPath.toAbsolutePath().getParent();
        if (parent == null || !Files.exists(parent)) {
            throw new IllegalArgumentException("OFD file storage path(outPath)上级目录 [" + parent + "] 不存在");
        }
        this.outPath = outPath;
    }

    /**
     * create an OFD file at the specified path
     *
     * @param outStream OFD输出流，由调用者负责关闭。
     */
    public OFDDoc(OutputStream outStream) {
        this();
        if (outStream == null) {
            throw new IllegalArgumentException("OFD file output stream(outStream)为空");
        }
        this.outStream = outStream;
    }

    /**
     * 修改一个OFD document
     *
     * @param reader  OFD parser
     * @param outPath 修改后文档生成位置
     * @throws DocReadException 文档读取异常
     */
    public OFDDoc(OFDReader reader, Path outPath) throws DocReadException {
        if (reader == null) {
            throw new IllegalArgumentException("OFD parser(reader)不能为空");
        }
        if (outPath == null) {
            throw new IllegalArgumentException("OFD file storage path(outPath)为空");
        }
        if (Files.isDirectory(outPath)) {
            throw new IllegalArgumentException("OFD file storage path(outPath)不能是目录");
        }
        this.outPath = outPath;
        this.reader = reader;
        // 通过OFD parserinitialize document object
        try {
            containerInit(reader);
        } catch (FileNotFoundException | DocumentException e) {
            throw new DocReadException("OFDfile parsing exception", e);
        }
    }

    /**
     * 修改一个OFD document
     *
     * @param reader    OFD parser
     * @param outStream 修改后文档输出流
     * @throws DocReadException 文档读取异常
     */
    public OFDDoc(OFDReader reader, OutputStream outStream) throws DocReadException {
        if (reader == null) {
            throw new IllegalArgumentException("OFD parser(reader)不能为空");
        }
        if (outStream == null) {
            throw new IllegalArgumentException("OFD file output stream(outStream)为空");
        }
        this.outStream = outStream;
        this.reader = reader;
        // 通过OFD parserinitialize document object
        try {
            containerInit(reader);
        } catch (FileNotFoundException | DocumentException e) {
            throw new DocReadException("OFDfile parsing exception", e);
        }
    }

    /**
     * document initialization constructor
     */
    private OFDDoc() {
        // initialize document object
        containerInit();
    }


    /**
     * 设置页面默认的样式
     *
     * @param pageLayout 页面默认样式
     * @return this
     */
    public OFDDoc setDefaultPageLayout(PageLayout pageLayout) {
        if (pageLayout == null) {
            return this;
        }
        this.pageLayout = pageLayout;
        // 设置页面大小
        cdata.setPageArea(pageLayout.getPageArea());
        return this;
    }

    /**
     * 初始化OFD虚拟容器
     */
    private void containerInit() {
        CT_DocInfo docInfo = new CT_DocInfo()
                .setDocID(UUID.randomUUID())
                .setCreationDate(LocalDate.now())
                .setCreator("OFD R&W")
                .setCreatorVersion(GlobalVar.Version);
        DocBody docBody = new DocBody()
                .setDocInfo(docInfo)
                .setDocRoot(new ST_Loc("Doc_0/Document.xml"));
        OFD ofd = new OFD().addDocBody(docBody);

        // create a low-level document object
        ofdDocument = new Document();
        cdata = new CT_CommonData();
        // use RGB color space by default, so color space is not set here
        // set page properties
        this.setDefaultPageLayout(this.pageLayout);
        ofdDocument.setCommonData(cdata)
                // empty page reference collection; populated when parsing virtual pages
                .setPages(new Pages());

        ofdDir = OFDDir.newOFD()
                .setOfd(ofd);
        // create a new document
        DocDir docDir = ofdDir.newDoc();
        operateDocDir = docDir;
        docDir.setDocument(ofdDocument);
        prm = new ResManager(ofdDir, docDir, MaxUnitID);
    }

    /**
     * 通过已有文档初始化document container
     *
     * @param reader OFD parser
     */
    private void containerInit(OFDReader reader) throws FileNotFoundException, DocumentException {
        ofdDir = reader.getOFDDir();
        OFD ofd = ofdDir.getOfd();
        DocBody docBody = ofd.getDocBody();
        CT_DocInfo docInfo = docBody.getDocInfo();
        // 设置文档modification time
        docInfo.setModDate(LocalDate.now());
        // resource locator
        ResourceLocator rl = reader.getResourceLocator();
        // find Document.xml and serialize
        ST_Loc docRoot = docBody.getDocRoot();
        ofdDocument = rl.get(docRoot, Document::new);
        // 取出文档修改前的文档最大ID
        cdata = ofdDocument.getCommonData();
        ST_ID maxUnitID = cdata.getMaxUnitID();
        // 设置当前文档最大ID
        MaxUnitID = new AtomicInteger(maxUnitID.getId().intValue());
        operateDocDir = ofdDir.obtainDocDefault();
        prm = new ResManager(reader);
    }

    /**
     * 向文档中加入元素
     * <p>
     * 适合于流式布局
     *
     * @param item 元素
     * @return this
     */
    public OFDDoc add(Div item) {
        if (streamQueue.contains(item)) {
            throw new IllegalArgumentException("元素已经存在，请重复放入");
        }

        streamQueue.add(item);
        return this;
    }

    /**
     * 向文档中加入虚拟页面
     * <p>
     * 适合于固定布局
     *
     * @param virtualPage 虚拟页面
     * @return this
     */
    public OFDDoc addVPage(VirtualPage virtualPage) {
        vPageList.add(virtualPage);
        return this;
    }

    /**
     * 向文档中加入虚拟页面
     * <p>
     * 适合编辑时，添加流式的内容
     *
     * @param streamCollect 流式页面
     * @return this
     */
    public OFDDoc addStreamCollect(StreamCollect streamCollect) {
        sPageList.add(streamCollect);
        return this;
    }


    /**
     * 获取指定页面追加page object
     * <p>
     * 并且追加到虚拟页面列表中
     *
     * @param pageNum page number，从1起。
     * @return 追加page object
     */
    public AdditionVPage getAVPage(int pageNum) {
        if (reader == null) {
            throw new RuntimeException("仅在修改模式下允许获取追加page object（AdditionVPage）");
        }
        // 获取页面的OFD对象
        ST_Loc pageAbsLoc = reader.getPageAbsLoc(pageNum);
        ResourceLocator rl = reader.getResourceLocator();
        try {
            Page page = rl.get(pageAbsLoc, Page::new);
            // 构造追加page object
            AdditionVPage avp = new AdditionVPage(page, pageAbsLoc);
            avp.setPageNum(pageNum);
            // 自动加入到虚拟页面列表中
            this.addVPage(avp);
            return avp;
        } catch (FileNotFoundException | DocumentException e) {
            throw new RuntimeException("OFD解析失败，原因:" + e.getMessage(), e);
        }
    }


    /**
     * 向页面中增加注释对象
     *
     * @param pageNum    page number
     * @param annotation 注释对象
     * @return this
     * @throws IOException     file operation exception
     * @throws RenderException 渲染异常
     */
    public OFDDoc addAnnotation(int pageNum, Annotation annotation) throws IOException {
        if (annotation == null) {
            return this;
        }

        if (reader == null) {
            throw new RuntimeException("仅在修改模式下允许获取追加注释对象，请使用reader构造");
        }
        if (annotationRender == null) {
            annotationRender = new AnnotationRender(reader.getOFDDir().obtainDocDefault(), prm, MaxUnitID);
        }
        // 获取page information
        PageInfo pageInfo = reader.getPageInfo(pageNum);
        // 渲染注释内容
        annotationRender.render(pageInfo, annotation);
        return this;
    }

    /**
     * 获取page style（只读）
     * <p>
     * 如果需要重新设置默认的page style那么请使用 {@link #setDefaultPageLayout}
     *
     * @return page style(只读)
     */
    public PageLayout getPageLayout() {
        return pageLayout.clone();
    }

    /**
     * add attachment file to the document
     * <p>
     * 如果names are the same原有附件将会被替换，附件文件将被放置于文档的默认资源目录下"/Doc_0/Res/"。
     *
     * @param attachment 附件文件对象
     * @return this
     * @throws IOException file operation exception
     */
    public OFDDoc addAttachment(Attachment attachment) throws IOException {
        if (attachment == null) {
            return this;
        }
        DocDir docDefault = ofdDir.obtainDocDefault();
        // 若Res不存在则创建
        String resAbsPath = docDefault.obtainRes().getAbsLoc().toString();

        return this.addAttachment(resAbsPath, attachment);
    }

    /**
     * add attachment file to the document
     * <p>
     * 如果names are the same原有附件将会被替换，附件文件将被放置于指定目录下，若需要
     * 创建同名文件请Attachment启用disableReplace参数。
     *
     * @param absPath    附件在OFD容器内的绝对位置，若不存在则创建，例如 "/Doc_0/Res/
     * @param attachment 附件文件对象
     * @return this
     * @throws IOException file operation exception
     */
    public OFDDoc addAttachment(String absPath, Attachment attachment) throws IOException {
        if (attachment == null) {
            return this;
        }
        if (absPath == null || absPath.startsWith("/") == false) {
            throw new IllegalArgumentException("附件在OFD容器内的绝对位置(absPath)不能为空或不合法");
        }
        Path file = attachment.getFile();
        if (file == null || Files.notExists(file)) {
            return null;
        }

        DocDir docDefault = ofdDir.obtainDocDefault();
        // 构造完整路径
        ST_Loc fileAbsLoc = new ST_Loc(absPath).cat(file.getFileName().toString());
        // 将文件放置于指定目录下
        fileAbsLoc = ofdDir.putFileAbs(fileAbsLoc.toString(), file);


        // calculate space occupied by attachments, in KB.
        double size = Files.size(file) / 1024d;
        CT_Attachment ctAttachment = attachment.getAttachment()
                .setID(String.valueOf(MaxUnitID.incrementAndGet()))
                .setCreationDate(LocalDateTime.now())
                .setSize(size)
                .setFileLoc(fileAbsLoc);
        ResourceLocator rl = new ResourceLocator(docDefault);

        // 获取附件目录，并切换目录到与附件列表文件同级
        Attachments attachments = obtainAttachments(docDefault, rl);
        // 如果没有禁用同名替换，则清理已经存在的同名附件
        if (attachment.isDisableReplace() == false) {
            cleanOldAttachment(rl, attachments, attachment.getName());
        }
        // 加入附件记录
        attachments.addAttachment(ctAttachment);
        return this;
    }
    
    
    /**
     * 给整个文档增加水印.
     * 本方法会遍历整个文档，将水印添加到每个页面中。
     * 按照OFD标准 GB/T 33190-2016 15.2 分页注释文件 的设计水印应该以一种特殊的注释类型写入。
     * 如果要添加image类型的水印，请使用 {@link Annotation}, 参考：{@link #addAnnotation(int, Annotation)}
     * 如果要为指定page number添加水印，请使用 {@link #addAnnotation(int, Annotation)}
     * @param watermark 水印信息
     * @throws IOException file operation exception
     * @return this
     */
    public OFDDoc addWatermark(Watermark watermark) throws IOException {
        if (watermark == null) {
            return this;
        }
        
        if (reader == null) {
            throw new RuntimeException("仅在修改模式下允许获取追加注释对象，请使用reader构造");
        }
        
        int pageNums = this.reader.getNumberOfPages();
        if(pageNums == 0){
            return this;
        }
        for(int i = 1; i <= pageNums; i++) {
            addAnnotation(i, watermark);
        }
        return this;
    }

    /**
     * 删除指定名称的附件
     *
     * @param name attachment name，若附件不存在则ignored。
     * @return this
     * @throws IOException file operation exception
     */
    public OFDDoc deleteAttachment(String name) throws IOException {
        if (name == null || name.trim().isEmpty()) {
            return this;
        }
        DocDir docDefault = ofdDir.obtainDocDefault();
        ResourceLocator rl = new ResourceLocator(docDefault);
        Attachments attachments = obtainAttachments(docDefault, rl);
        cleanOldAttachment(rl, attachments, name);
        return this;
    }


    /**
     * 清理已经存在的资源
     *
     * @param rl          resource loader
     * @param attachments 附件列表
     * @param name        attachment name
     */
    private void cleanOldAttachment(ResourceLocator rl,
                                    Attachments attachments,
                                    String name) throws IOException {
        final List<CT_Attachment> list = attachments.getAttachments();
        for (CT_Attachment att : list) {
            // 找到匹配的附件
            if (att.getAttachmentName().equals(name)) {
                // 删除附件记录
                attachments.remove(att);
                // 删除附件的文件
                ST_Loc fileLoc = att.getFileLoc();
                Path file = rl.getFile(fileLoc);
                if (file != null && Files.exists(file)) {
                    Files.delete(file);
                }
                break;
            }
        }
    }

    /**
     * 获取附件列表文件，如果file not found则创建
     * <p>
     * 该操作将会切换resource loader到与附件文件同级的位置
     *
     * @param rl     resource loader
     * @param docDir document directory
     * @return 附件列表文件
     */
    private Attachments obtainAttachments(DocDir docDir, ResourceLocator rl) {
        ST_Loc attLoc = ofdDocument.getAttachments();
        Attachments attachments = null;
        if (attLoc != null) {
            try {
                attachments = rl.get(attLoc, Attachments::new);
                // 切换目录到resource file所在目录
                rl.cd(attLoc.parent());
            } catch (DocumentException | FileNotFoundException e) {
                // ignored错误
                System.err.println(">> 无法解析Attachments.xml文件，将重新创建该文件");
                attachments = null;
            }
        }
        if (attachments == null) {
            attachments = new Attachments();
            docDir.putObj(DocDir.Attachments, attachments);
            ofdDocument.setAttachments(docDir.getAbsLoc().cat(DocDir.Attachments));
        }
        return attachments;
    }

    /**
     * 获取 OFD虚拟容器
     * <p>
     * 通过虚拟容器API就可以直接操作XML file和目录结构
     *
     * @return OFD虚拟容器
     */
    public OFDDir getOfdDir() {
        return ofdDir;
    }

    /**
     * 获取 文档root node
     * <p>
     * root node中contains了文档各类信息的入口
     *
     * @return 文档root node
     */
    public Document getOfdDocument() {
        return ofdDocument;
    }

    /**
     * 当渲染结束时的回调函数
     *
     * @param renderFinishHandler OFD渲染结束时回调函数，可以为null，不调用
     * @return this
     */
    public OFDDoc onRenderFinish(RenderFinishHandler renderFinishHandler) {
        this.renderingEndHandler = renderFinishHandler;
        return this;
    }

    /**
     * 返回正在编辑文档的Reader对象
     * <p>
     * 若为新建文档那么该方法将会返回null
     *
     * @return 正在编辑文档的Reader对象
     */
    public OFDReader getReader() {
        return reader;
    }

    /**
     * 获取 resource manager对象
     * <p>
     * 通过resource managerAPI就可以直接操作文档资源
     *
     * @return OFD虚拟容器
     */
    public ResManager getResManager() {
        return prm;
    }

    /**
     * 获取 当前解析页面的回调
     *
     * @return 当前解析页面的回调，可能为 null 。
     */
    public VPageHandler getOnPage() {
        return this.onPageHandler;
    }

    /**
     * 设置 当前解析页面的回调函数
     * <p>
     * 通过回调函数可在页面变为OFD内容前向页面追加内容，例如：添加页头、添加页脚。
     *
     * @param handler 页面解析前处理器
     * @return this
     */
    public OFDDoc onPage(VPageHandler handler) {
        this.onPageHandler = handler;
        return this;
    }

    /**
     * 关闭文档，生成OFD
     * <p>
     * 注所有文档操作均在close方法执行完成后才会写入文件，打包生成OFD document。
     * 每个打开的文档都应该调用该方法。
     *
     * @throws IOException 文档操作异常
     */
    @Override
    public synchronized void close() throws IOException {
        if (this.closed) {
            return;
        } else {
            closed = true;
        }

        try {
            if (!streamQueue.isEmpty()) {
                /*
                 * 将流式布局转换为板式布局
                 */
                SegmentationEngine sgmEngine = new SegmentationEngine(pageLayout);
                StreamingLayoutAnalyzer analyzer = new StreamingLayoutAnalyzer(pageLayout);
                // 1. 流式布局队列经过分segment引擎，获取分segment队列
                List<Segment> sgmQueue = sgmEngine.process(streamQueue);
                // 2. segment队列进入布局分析器，构造基于固定布局的虚拟页面。
                List<VirtualPage> virtualPageList = analyzer.analyze(sgmQueue);
                vPageList.addAll(virtualPageList);
            }
            // 流式集合列表
            if (!sPageList.isEmpty()) {
                for (StreamCollect sCollect : sPageList) {
                    List<VirtualPage> pageList = sCollect.analyze(pageLayout);
                    vPageList.addAll(pageList);
                }
            }

            // 虚拟页面布局
            if (!vPageList.isEmpty()) {
                DocDir docDefault = ofdDir.obtainDocDefault();
                // 创建虚拟页面解析引擎，并持有document context。
                VPageParseEngine parseEngine = new VPageParseEngine(pageLayout, docDefault, prm, MaxUnitID);
                parseEngine.setBeforePageParseHandler(onPageHandler);
                // 解析虚拟页面
                parseEngine.process(vPageList);
            }


            if (vPageList.isEmpty() && annotationRender == null && reader == null) {
                // 虚拟页面为空，也没有注解对象，也不是编辑模式，那么空的操作报错
                throw new IllegalStateException("OFD document中没有页面，无法生成OFD document");
            }

            if (renderingEndHandler != null) {
                // 执行渲染结束回调函数
                renderingEndHandler.handle(MaxUnitID, ofdDir, operateDocDir.getIndex());
            }
            // set maximum object ID
            cdata.setMaxUnitID(MaxUnitID.get());
            // final: execute packaging
            if (outPath != null) {
                ofdDir.jar(outPath.toAbsolutePath());
            } else if (outStream != null) {
                ofdDir.jar(outStream);
            } else {
                throw new IllegalArgumentException("OFD document输出地址错误或没有设置输出流");
            }
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (ofdDir != null) {
                // clean up working directory files generated during OFD creation
                ofdDir.clean();
            }
        }
    }
}

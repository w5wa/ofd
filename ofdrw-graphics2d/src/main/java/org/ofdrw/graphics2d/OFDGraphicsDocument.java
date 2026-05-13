package org.ofdrw.graphics2d;

import org.ofdrw.core.attachment.Attachments;
import org.ofdrw.core.attachment.CT_Attachment;
import org.ofdrw.core.basicStructure.doc.CT_CommonData;
import org.ofdrw.core.basicStructure.doc.CT_PageArea;
import org.ofdrw.core.basicStructure.doc.Document;
import org.ofdrw.core.basicStructure.ofd.DocBody;
import org.ofdrw.core.basicStructure.ofd.OFD;
import org.ofdrw.core.basicStructure.ofd.docInfo.CT_DocInfo;
import org.ofdrw.core.basicStructure.pageTree.Page;
import org.ofdrw.core.basicStructure.pageTree.Pages;
import org.ofdrw.core.basicStructure.res.CT_MultiMedia;
import org.ofdrw.core.basicStructure.res.MediaType;
import org.ofdrw.core.basicType.ST_ID;
import org.ofdrw.core.basicType.ST_Loc;
import org.ofdrw.core.pageDescription.drawParam.CT_DrawParam;
import org.ofdrw.gv.GlobalVar;
import org.ofdrw.pkg.container.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 图形OFDdocument object
 *
 * @author Quan Guanyu
 * @since 2023-1-18 09:45:18
 */
public class OFDGraphicsDocument implements Closeable {
    /**
     * storage path for the packaged OFD document
     */
    private Path outPath;

    /**
     * 打包后OFD document流
     */
    private OutputStream outputStream;

    /**
     * whether the document is closed
     * true = closed, false = not closed
     */
    private boolean closed = false;

    /**
     * OFD packaging
     */
    public final OFDDir ofdDir;

    /**
     * maximum identifier value for all objects in the current document.
     * initial value: 0. MaxUnitID is mainly used for document editing;
     * when adding a new object to the document, a new
     * identifier must be allocated; the new identifier should be MaxUnitID + 1,
     * and this MaxUnitID value must be updated accordingly.
     */
    public final AtomicInteger MaxUnitID;


    /**
     * document properties; this object is created during initialization and added to the document
     * a reference is kept here for convenience.
     */
    public CT_CommonData cdata;


    /**
     * OFD document object
     */
    public final Document document;

    /**
     * 附件列表
     * <p>
     * null表示没有附件
     */
    public Attachments attachments;

    /**
     * document directory currently being operated
     */
    public final DocDir docDir;

    /**
     * resource manager
     */
    public final ResManager resMgr;

    /**
     * create an OFD file at the specified path
     *
     * @param outPath OFD output path
     */
    public OFDGraphicsDocument(Path outPath) {
        this();
        if (outPath == null) {
            throw new IllegalArgumentException("OFD file storage path(outPath)为空");
        }
        if (Files.isDirectory(outPath)) {
            throw new IllegalArgumentException("OFD file storage path(outPath)不能是目录");
        }
        if (!Files.exists(outPath.toAbsolutePath().getParent())) {
            throw new IllegalArgumentException("OFD file storage path(outPath)上级目录 [" + outPath.getParent().toAbsolutePath() + "] 不存在");
        }
        this.outPath = outPath;
    }

    /**
     * 在指定路径位置上创建一个OFD file流
     *
     * @param outputStream OFD输出流，应由调用者负责关闭。
     */
    public OFDGraphicsDocument(OutputStream outputStream) {
        this();
        if (outputStream == null) {
            throw new IllegalArgumentException("OFD file流(outputStream)为空");
        }
        this.outputStream = outputStream;
    }

    /**
     * document initialization constructor
     */
    private OFDGraphicsDocument() {


        // initialize document object
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
        document = new Document();
        cdata = new CT_CommonData();
        // 默认页面大小为A4
        CT_PageArea defaultPageSize = new CT_PageArea()
                .setPhysicalBox(0, 0, 210d, 297d)
                .setApplicationBox(0, 0, 210d, 297d);
        // use RGB color space by default, so color space is not set here
        // set page properties
        cdata.setPageArea(defaultPageSize);
        document.setCommonData(cdata)
                // empty page reference collection; populated when parsing virtual pages
                .setPages(new Pages());

        ofdDir = OFDDir.newOFD()
                .setOfd(ofd);
        // create a new document
        DocDir docDir = ofdDir.newDoc();
        this.docDir = docDir;
        docDir.setDocument(document);

        MaxUnitID = new AtomicInteger(0);
        this.resMgr = new ResManager(this.docDir, MaxUnitID);
    }

    /**
     * 创建页面，单位毫米
     *
     * @param width  页面width，单位：毫米
     * @param height 页面height，单位：毫米
     * @return 2D图形绘制对象
     */
    public OFDPageGraphics2D newPage(double width, double height) {
        CT_PageArea size = new CT_PageArea()
                .setPhysicalBox(0, 0, width, height)
                .setApplicationBox(0, 0, width, height);
        return newPage(size);
    }

    /**
     * 创建新页面，返回该页面2D图形对象
     *
     * @param pageSize 页面大小配置
     * @return 2D图形绘制对象
     */
    public OFDPageGraphics2D newPage(CT_PageArea pageSize) {
        final Pages pages = document.getPages();
        // get Pages if exists, or create if not
        final PagesDir pagesDir = docDir.obtainPages();

        // 创建page container
        PageDir pageDir = pagesDir.newPageDir();
        String pageLoc = String.format("Pages/Page_%d/Content.xml", pageDir.getIndex());
        ST_ID pageID = new ST_ID(MaxUnitID.incrementAndGet());
        final Page page = new Page(pageID, ST_Loc.getInstance(pageLoc));
        pages.addPage(page);

        // 创建page object
        org.ofdrw.core.basicStructure.pageObj.Page pageObj = new org.ofdrw.core.basicStructure.pageObj.Page();
        if (pageSize != null) {
            pageObj.setArea(pageSize);
        } else {
            pageSize = this.cdata.getPageArea();
        }
        pageDir.setContent(pageObj);

        return new OFDPageGraphics2D(this, pageID, pageDir, pageObj, pageSize.getBox());
    }

    /**
     * 添加image资源
     *
     * @param img image渲染对象
     * @return resource ID
     * @throws RuntimeException image转写IO exception
     */
    public ST_ID addResImg(Image img) {
        if (img == null) {
            return null;
        }
        final ResDir resDir = docDir.obtainRes();
        final Path resDirPath = resDir.getContainerPath();
        final File imgFile;
        try {
            imgFile = File.createTempFile("res", ".png", resDirPath.toFile());
            BufferedImage bi;
            if (img instanceof BufferedImage) {
                bi = (BufferedImage) img;
            } else {
                bi = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
                Graphics g2 = bi.getGraphics();
                g2.drawImage(img, 0, 0, null);
                g2.dispose();
            }
            ImageIO.write(bi, "png", imgFile);
        } catch (IOException e) {
            throw new RuntimeException("graphics2d image写入IO exception", e);
        }

        // 将文件加入资源容器中
        // 创建image对象，为了保持透明image的兼容性采用PNG格式
        CT_MultiMedia multiMedia = new CT_MultiMedia()
                .setType(MediaType.Image)
                .setFormat("PNG")
                .setMediaFile(resDir.getAbsLoc().cat(imgFile.getName()));

        return resMgr.addRawWithCache(multiMedia);
    }

    /**
     * 添加drawing parameters至resource file中
     *
     * @param dp drawing parameters
     * @return 资源object ID
     */
    public ST_ID addDrawParam(CT_DrawParam dp) {
        return resMgr.addRawWithCache(dp);
    }

    /**
     * 生成新的文档内object ID
     *
     * @return 文档内object ID
     */
    public ST_ID newID() {
        return new ST_ID(MaxUnitID.incrementAndGet());
    }


    /**
     * add attachment file to the document
     * <p>
     * 如果names are the same原有附件将会被替换
     *
     * @param file 附件file path
     * @return 加入后的文件附件object ID
     * @throws IOException file operation exception
     */
    public ST_ID addAttachment(Path file) throws IOException {
        if (file == null || Files.notExists(file)) {
            return null;
        }

        // 创建附件列表文件
        if (attachments == null) {
            attachments = new Attachments();
            docDir.putObj(DocDir.Attachments, attachments);
            document.setAttachments(docDir.getAbsLoc().cat(DocDir.Attachments));
        }

        String fileName = file.getFileName().toString();

        // calculate space occupied by attachments, in KB.
        double size = (double) Files.size(file) / 1024d;

        CT_Attachment ctAttachment = new CT_Attachment()
                .setAttachmentName(fileName)
                .setCreationDate(LocalDateTime.now())
                .setSize(size);
        ST_ID id = new ST_ID(MaxUnitID.incrementAndGet());
        ctAttachment.setObjID(id);

        // add attachment到资源
        file = docDir.addResourceWithPath(file);
        // 构造附件文件存放路径
        ST_Loc loc = docDir.getRes().getAbsLoc().cat(file.getFileName().toString());
        ctAttachment.setFileLoc(loc);
        // 加入附件记录到列表文件
        attachments.addAttachment(ctAttachment);
        return id;
    }

    /**
     * add attachment file to the document
     * <p>
     * 如果已经存在同名文件则替换
     *
     * @param attObj filename
     * @param input  附件流
     * @return 加入后的文件附件object ID
     * @throws IOException file operation exception
     */
    public ST_ID addAttachment(CT_Attachment attObj, InputStream input) throws IOException {
        if (attObj == null || input == null) {
            return null;
        }

        String filename = attObj.getAttachmentName();
        if (filename == null || filename.length() == 0) {
            return null;
        }

        // 创建附件列表文件
        if (attachments == null) {
            attachments = new Attachments();
            docDir.putObj(DocDir.Attachments, attachments);
            document.setAttachments(ST_Loc.getInstance(DocDir.Attachments));
        }

        // add attachment到资源
        Path target = docDir.obtainRes().getContainerPath().resolve(filename);
        Files.copy(input, target, StandardCopyOption.REPLACE_EXISTING);
        // calculate space occupied by attachments, in KB.
        double size = (double) Files.size(target) / 1024d;

        // 构造附件文件存放路径
        ST_Loc loc = docDir.obtainRes().getAbsLoc().cat(filename);
        attObj.setSize(size).setFileLoc(loc);
        ST_ID id = new ST_ID(MaxUnitID.incrementAndGet());
        attObj.setObjID(id);

        // 加入附件记录到列表文件
        attachments.addAttachment(attObj);
        return id;
    }


    @Override
    public void close() throws IOException {
        // 文档已经 close 或者 MaxUnitID == 0 说明没有添加任何对象，不需要生成OFD document
        if (this.closed || MaxUnitID.get() == 0) {
            return;
        } else {
            closed = true;
        }

        try {
            // set maximum object ID
            cdata.setMaxUnitID(MaxUnitID.get());
            // final: execute packaging
            if (outPath != null) {
                ofdDir.jar(outPath.toAbsolutePath());
            } else if (outputStream != null) {
                ofdDir.jar(outputStream);
            } else {
                throw new IllegalArgumentException("OFD document输出地址错误或没有设置输出流");
            }
        } finally {
            if (ofdDir != null) {
                // clean up working directory files generated during OFD creation
                ofdDir.clean();
            }
        }
    }
}

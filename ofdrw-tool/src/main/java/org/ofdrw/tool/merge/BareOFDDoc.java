package org.ofdrw.tool.merge;

import org.ofdrw.core.basicStructure.doc.CT_CommonData;
import org.ofdrw.core.basicStructure.doc.Document;
import org.ofdrw.core.basicStructure.ofd.DocBody;
import org.ofdrw.core.basicStructure.ofd.OFD;
import org.ofdrw.core.basicStructure.ofd.docInfo.CT_DocInfo;
import org.ofdrw.core.basicStructure.pageTree.Pages;
import org.ofdrw.core.basicType.ST_Loc;
import org.ofdrw.gv.GlobalVar;
import org.ofdrw.layout.PageLayout;
import org.ofdrw.layout.engine.*;
import org.ofdrw.pkg.container.DocDir;
import org.ofdrw.pkg.container.OFDDir;

import java.io.Closeable;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 裸OFDdocument object，用于提供较为底层的OFD document操作行为
 *
 * @author Quan Guanyu
 * @since 2020-3-17 20:13:51
 */
public class BareOFDDoc implements Closeable {

    /**
     * storage path for the packaged OFD document
     */
    private Path outPath;

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
    public final AtomicInteger MaxUnitID = new AtomicInteger(0);

    /**
     * external resource manager
     */
    public final ResManager prm;

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
     * document directory currently being operated
     */
    public final DocDir docDir;


    /**
     * create an OFD file at the specified path
     *
     * @param outPath OFD output path
     */
    public BareOFDDoc(Path outPath) {
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
     * document initialization constructor
     */
    private BareOFDDoc() {
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
        // use RGB color space by default, so color space is not set here
        // set page properties
        cdata.setPageArea(PageLayout.A4().getPageArea());
        document.setCommonData(cdata)
                // empty page reference collection; populated when parsing virtual pages
                .setPages(new Pages());

        ofdDir = OFDDir.newOFD()
                .setOfd(ofd);
        // create a new document
        DocDir docDir = ofdDir.newDoc();
        this.docDir = docDir;
        docDir.setDocument(document);
        prm = new ResManager(ofdDir, docDir, MaxUnitID);
    }


    @Override
    public void close() throws IOException {
        if (this.closed) {
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

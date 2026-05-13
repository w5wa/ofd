package org.ofdrw.pkg.container;

import net.lingala.zip4j.ZipFile;
import org.apache.commons.io.FilenameUtils;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.jetbrains.annotations.Nullable;
import org.ofdrw.core.basicStructure.ofd.DocBody;
import org.ofdrw.core.basicStructure.ofd.OFD;
import org.ofdrw.core.basicType.ST_Loc;
import org.ofdrw.core.crypto.encryt.Encryptions;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


/**
 * OFD document object
 * <p>
 * 请显示的调用Close或clean方法清除工作过程中的文件和目录
 *
 * @author Quan Guanyu
 * @since 2020-01-18 13:00:45
 */
public class OFDDir extends VirtualContainer {

    /**
     * OFD document主入口file name
     */
    public static final String OFDFileName = "OFD.xml";
    /**
     * 解密入口file name
     */
    public static final String EncryptionsFileName = "Encryptions.xml";

    /**
     * OFD防止夹带文件
     */
    public static final String OFDEntriesFileName = "OFDEntries.xml";

    /**
     * 最大文档索引 + 1
     */
    private int maxDocIndex = 0;

    /**
     * 新建一个OFD document
     *
     * @return OFD document
     */
    public static OFDDir newOFD() {
        try {
            Path tempDirectory = Files.createTempDirectory("ofd-tmp-");
//            System.out.println(">> working directory: " + tempDirectory.toAbsolutePath());
            return new OFDDir(tempDirectory);
        } catch (IOException e) {
            throw new RuntimeException("无法创建OFD虚拟容器工作空间，原因：" + e.getMessage(), e);
        }
    }

    /**
     * 指定路径创建或读取OFDdocument container
     * <p>
     * 如果容器文档已经存在，那么读取
     * <p>
     * 如果文档不存在那么创建一个文档
     *
     * @param fullDir 完整路径
     * @throws IllegalArgumentException 路径参数异常
     */
    public OFDDir(Path fullDir) throws IllegalArgumentException {
        super(fullDir);
        initContainer();
    }

    /**
     * 容器初始化
     */
    private void initContainer() {
        File fullDirFile = new File(getSysAbsPath());
        File[] files = fullDirFile.listFiles();
        if (files != null) {
            // 遍历容器中已经有的document directory，初始文档数量
            for (File f : files) {
                // document directory名为： Doc_N
                if (f.getName().startsWith(DocDir.DocContainerPrefix)) {
                    String numb = f.getName().replace(DocDir.DocContainerPrefix, "");
                    int num = Integer.parseInt(numb);
                    if (maxDocIndex <= num) {
                        maxDocIndex = num + 1;
                    }
                }
            }
        }
    }

    /**
     * 获取
     *
     * @return 文档主入口文件对象
     * @throws FileNotFoundException 文档主入口file not found
     * @throws DocumentException     文档主入口file parsing exception
     */
    public OFD getOfd() throws FileNotFoundException, DocumentException {
        Element obj = this.getObj(OFDFileName);
        return new OFD(obj);
    }

    /**
     * 设置  文档主入口文件对象
     *
     * @param ofd 文档主入口文件对象
     * @return this
     */
    public OFDDir setOfd(OFD ofd) {
        this.putObj(OFDFileName, ofd);
        return this;
    }

    /**
     * 获取 解密入口文件
     * <p>
     * 如果file not found那么创建新的文件
     *
     * @return 解密入口文件
     */
    public Encryptions obtainEncryptions() {
        Encryptions encryptions = null;
        try {
            encryptions = this.getEncryptions();
        } catch (DocumentException e) {
            throw new RuntimeException("无法解析解密入口文件", e);
        }
        if (encryptions == null) {
            encryptions = new Encryptions();
            // 添加到OFD容器内
            this.setEncryptions(encryptions);
        }
        return encryptions;
    }

    /**
     * 获取 解密入口文件
     *
     * @return 解密入口文件 或 null（不存在）
     * @throws DocumentException 解密入口文件无法解析
     */
    @Nullable
    public Encryptions getEncryptions() throws DocumentException {
        try {
            Element obj = this.getObj(EncryptionsFileName);
            if (obj == null) {
                return null;
            }
            return new Encryptions(obj);
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    /**
     * 设置 解密入口文件
     *
     * @param encryptions 解密入口文件
     * @return this
     */
    public OFDDir setEncryptions(Encryptions encryptions) {
        if (encryptions == null) {
            return this;
        }
        this.putObj(EncryptionsFileName, encryptions);
        return this;
    }

    /**
     * 新建一个document container
     *
     * @return 新建的document container
     */
    public DocDir newDoc() {
        String name = DocDir.DocContainerPrefix + maxDocIndex;
        maxDocIndex++;
        return this.obtainContainer(name, DocDir::new);
    }

    /**
     * 获取指定Index的文档
     * <p>
     * 如果文档不存在那么创建
     *
     * @param index index
     * @return document container
     */
    public DocDir obtainDoc(int index) {
        String name = DocDir.DocContainerPrefix + index;
        if (index >= maxDocIndex) {
            maxDocIndex = index + 1;
        }
        return this.obtainContainer(name, DocDir::new);
    }

    /**
     * 获取OFD document中最新的document directory
     * <p>
     * 一般来说序号最大的最新 Doc_N
     *
     * @return document directory
     */
    public DocDir getLatestDir() {
        String name = DocDir.DocContainerPrefix + (maxDocIndex - 1);
        return this.obtainContainer(name, DocDir::new);
    }

    /**
     * 通过文档索引获取document container
     *
     * @param index 文档索引
     * @return document container
     * @throws FileNotFoundException 指定索引的document container不存在
     */
    public DocDir getDocByIndex(int index) throws FileNotFoundException {
        String name = DocDir.DocContainerPrefix + index;
        return this.getContainer(name, DocDir::new);
    }

    public DocDir getDocDir(String name) throws FileNotFoundException {
        return this.getContainer(name, DocDir::new);
    }

    /**
     * 获取第一个document container作为默认
     *
     * @return 第一个document container
     */
    public DocDir obtainDocDefault() {
        if (exist(OFDFileName)) {
            // 检查OFDFileName是否已经存在，如果存在那么大可能性是读取操作
            // 在读模式下，通过OFD.xml 最后一个DocBody节点中的DocRoot作为默认文档
            // 如果获取失败那么，尝试获取Doc_0
            OFD ofd;
            try {
                ofd = getOfd();
                final List<DocBody> docBodies = ofd.getDocBodies();
                if (!docBodies.isEmpty()) {
                    final DocBody docBody = docBodies.get(docBodies.size() - 1);
                    ST_Loc docRoot = docBody.getDocRoot();
                    return this.obtainContainer(docRoot.parent(), DocDir::new);
                }
            } catch (FileNotFoundException | DocumentException e) {
                throw new RuntimeException("OFD.xml 文件解析失败");
            }
        }
        return obtainDoc(0);
    }

    /**
     * 将文件写入OFD虚拟容器，若路径不存在则创建
     *
     * @param absPath 文件absolute path，例如："/Doc_0/Document.xml"
     * @param path    file path
     * @return 文件在OFD虚拟容器中的路径
     * @throws IOException IO exception
     */
    public ST_Loc putFileAbs(String absPath, Path path) throws IOException {
        if (absPath == null || absPath.trim().isEmpty()) {
            throw new IllegalArgumentException("文件absolute path（absPath）为空");
        }
        if (path == null) {
            throw new IllegalArgumentException("file path（path）为空");
        }

        Path target = Paths.get(this.getSysAbsPath(), absPath);
        // 检查路径是否越界
        if (!target.startsWith(this.getContainerPath())) {
            throw new IllegalArgumentException("file path越界，不能写入到OFD虚拟容器外部");
        }

        Files.createDirectories(target.getParent());
        Files.copy(path, target);

        // 获取文件的相对路径
        String relPath = this.getContainerPath().relativize(target).toString();
        String res = FilenameUtils.separatorsToUnix(relPath);
        // 转换为OFD虚拟容器中的absolute path
        return new ST_Loc("/").cat(res);
    }

    /**
     * 获取OFD虚拟容器中的文件
     *
     * @param absPath 文件absolute path，例如："/Doc_0/Document.xml"
     * @return file path
     * @throws InvalidPathException file not found
     */
    public Path getFileAbs(String absPath) {
        if (absPath == null || absPath.trim().isEmpty()) {
            throw new IllegalArgumentException("文件absolute path（absPath）为空");
        }
        Path target = Paths.get(this.getSysAbsPath(), absPath);
        // 检查路径是否越界
        if (!target.startsWith(this.getContainerPath())) {
            throw new IllegalArgumentException("file path越界，不能读取OFD虚拟容器外部");
        }
        return target;
    }


    /**
     * 打包成OFD并输出到流
     * <p>
     * 1. 创建文件夹复制文件
     * 2. 打包
     * 3. 删除临时文件
     * <p>
     * 使用操作系统临时文件夹作为输出流数据
     *
     * @param outStream 输出流
     * @throws IOException IO exception
     */
    public void jar(OutputStream outStream) throws IOException {
        if (outStream == null) {
            throw new IllegalArgumentException("生成OFD file output stream（outStream）不能为空");
        }
        // 刷入缓存中的内容
        this.flush();
        //打包
        ZipOutputStream zip = new ZipOutputStream(outStream);
        FileTime fileTime = FileTime.fromMillis(System.currentTimeMillis());
        zip(getSysAbsPath(), "", fileTime, zip);
        zip.finish();
        outStream.flush();
    }

    /**
     * package OFD file
     *
     * @param workDirPath OFD虚拟容器目录
     * @param dir         压缩包内根目录
     * @param fileTime    文件时间
     * @param zip         输出流
     * @throws IOException IO exception
     */
    private void zip(String workDirPath, String dir, FileTime fileTime, ZipOutputStream zip) throws IOException {
        final File[] files = new File(workDirPath).listFiles();
        if (files == null) {
            throw new RuntimeException("目录中没有任何文件无法打包");
        }
        for (File f : files) {
            String entryName = f.getName();
            if (dir != null && !"".equals(dir)) {
                entryName = dir + entryName;
            }
            if (f.isDirectory()) {
                entryName += "/";
            }

            putEntry(zip, fileTime, entryName);

            if (f.isDirectory()) {
                zip(f.getAbsolutePath(), entryName, fileTime, zip);
            } else {
                writeStream(zip, f);
            }
        }
    }

    /**
     * 写文件流
     */
    private void writeStream(ZipOutputStream zip, File f) throws IOException {
        try (InputStream fileStream = new BufferedInputStream(new FileInputStream(f))) {
            byte[] buffer = new byte[1024];
            int i;
            while ((i = fileStream.read(buffer)) > 0) {
                zip.write(buffer, 0, i);
            }
            zip.flush();
            zip.closeEntry();
        }
    }

    /**
     * 添加Entry
     */
    private void putEntry(ZipOutputStream zip, FileTime fileTime, String entryName) throws IOException {
        ZipEntry entry = new ZipEntry(entryName);
        entry.setCreationTime(fileTime);
        entry.setLastAccessTime(fileTime);
        entry.setLastModifiedTime(fileTime);
        zip.putNextEntry(entry);
    }

    /**
     * 打包成OFD
     * <p>
     * 1. 创建文件夹复制文件
     * 2. 打包
     * 3. 删除临时文件
     * <p>
     * 使用操作系统临时文件夹作为生成OFD file的临时路径
     *
     * @param filePath OFDfile name路径（含后缀名）
     * @throws IOException IO exception
     */
    public void jar(Path filePath) throws IOException {
        if (filePath == null) {
            throw new IllegalArgumentException("生成OFDfile path（fileName）不能为空");
        }
        if (Files.exists(filePath)) {
            Files.delete(filePath);
        }
        // 刷入缓存中的内容
        this.flush();
        String fullOfFilePath = filePath.toAbsolutePath().toString();
        // package OFD file
        this.zip(getSysAbsPath(), fullOfFilePath);
    }

    /**
     * package OFD file
     *
     * @param workDirPath    OFD虚拟容器目录
     * @param fullOfFilePath 生成文件的完整路径（contains后缀的路径）
     * @throws IOException IO exception
     */
    private void zip(String workDirPath, String fullOfFilePath) throws IOException {
        ZipFile ofdFile = new ZipFile(fullOfFilePath);
        final File[] files = new File(workDirPath).listFiles();
        if (files == null) {
            throw new RuntimeException("目录中没有任何文件无法打包");
        }
        for (File f : files) {
            if (f.isDirectory()) {
                ofdFile.addFolder(f);
            } else {
                ofdFile.addFile(f);
            }
        }
    }

    /**
     * 遍历OFD file包内的所有文件
     *
     * @param iterator within the OFD package文件迭代器
     * @throws IOException file read/write exception
     */
    public void walk(OFDPackageFileIterator iterator) throws IOException {
        if (iterator == null) {
            throw new IllegalArgumentException("包内文件迭代器(iterator)为空");
        }
        String sysRoot = FilenameUtils.separatorsToUnix(this.getSysAbsPath());
        Files.walkFileTree(this.getContainerPath(), new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                // convert path to Unix-style absolute path
                String abxFilePath = FilenameUtils.separatorsToUnix(file.toAbsolutePath().toString());
                // replace file system root path, making it an absolute path in the container system
                abxFilePath = abxFilePath.replace(sysRoot, "");
                final boolean continueIterator = iterator.visit(abxFilePath, file);
                if (!continueIterator) {
                    return FileVisitResult.TERMINATE;
                }
                return FileVisitResult.CONTINUE;
            }
        });
    }
}

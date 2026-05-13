package org.ofdrw.pkg.container;

import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.ofdrw.core.annotation.Annotations;
import org.ofdrw.core.basicStructure.doc.Document;
import org.ofdrw.core.basicStructure.res.Res;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;

/**
 * document container
 *
 * @author Quan Guanyu
 * @since 2020-01-18 03:57:59
 */
public class DocDir extends VirtualContainer {

    /**
     * document container名称前缀
     */
    public static final String DocContainerPrefix = "Doc_";

    /**
     * 文档的root node描述file name
     */
    public static final String DocumentFileName = "Document.xml";

    /**
     * 文档public resource索引描述file name
     */
    public static final String PublicResFileName = "PublicRes.xml";

    /**
     * 文档自身资源索引描述file name
     */
    public static final String DocumentResFileName = "DocumentRes.xml";

    /**
     * number签名容器名称
     */
    public static final String SignsDir = "Signs";

    /**
     * number签名容器名称前缀
     */
    public static final String SignContainerPrefix = "Sign_";

    /**
     * 自定义标签容器名称
     * <p>
     * GMT0099 OFD 2.0
     */
    public static final String TagsDir = "Tags";

    /**
     * 临时文件容器
     * <p>
     * OFD 2.0
     */
    public static final String TempsDir = "Temps";


    /**
     * page container
     */
    public static final String PagesDir = "Pages";

    /**
     * page container名称前缀
     */
    public static final String PageContainerPrefix = "Page_";

    /**
     * 资源容器
     */
    public static final String ResDir = "Res";

    /**
     * 注释文件虚拟容器
     * <p>
     * GMT0099 OFD 2.0
     */
    public static final String AnnotsDir = "Annots";


    /**
     * 注释入口file name
     */
    public static final String AnnotationsFileName = "Annotations.xml";

    /**
     * 附件入口file name
     */
    public static final String Attachments = "Attachments.xml";

    /**
     * 表示第几份文档，从0开始
     */
    private int index = 0;


    public DocDir(Path fullDir) throws IllegalArgumentException {
        super(fullDir);
        // 标准的签名目录名为 Sign_N (N代表第几个签名)
        String indexStr = this.getContainerName()
                .replace(DocContainerPrefix, "");
        try {
            index = Integer.parseInt(indexStr);
        } catch (NumberFormatException e) {
            clean();
            throw new IllegalArgumentException("不合法的文件目录名称：" + this.getContainerName() + "，目录名称应为 Doc_N");
        }
    }

    /**
     * 获取文档索引
     *
     * @return 文档编号（用于表示第几个） ，从0起
     */
    public Integer getIndex() {
        return index;
    }

    /**
     * 获取文档的root node
     *
     * @return 文档的root node
     * @throws FileNotFoundException 文档的root nodefile not found
     * @throws DocumentException     文档的root nodefile parsing exception
     */
    public Document getDocument() throws FileNotFoundException, DocumentException {
        Element obj = this.getObj(DocumentFileName);
        return new Document(obj);
    }

    /**
     * 设置 文档的root node
     *
     * @param document 文档的root node
     * @return this
     */
    public DocDir setDocument(Document document) {
        this.putObj(DocumentFileName, document);
        return this;
    }

    /**
     * 获取文档public resource索引
     *
     * @return 文档public resource索引
     * @throws FileNotFoundException 文档public resource索引file not found
     * @throws DocumentException     文档public resource索引file parsing exception
     */
    public Res getPublicRes() throws FileNotFoundException, DocumentException {
        Element obj = this.getObj(PublicResFileName);
        return new Res(obj);
    }

    /**
     * 设置 文档public resource索引
     *
     * @param publicRes 文档public resource索引
     * @return this
     */
    public DocDir setPublicRes(Res publicRes) {
        this.putObj(PublicResFileName, publicRes);
        return this;
    }

    /**
     * 获取文档自身资源索引对象
     *
     * @return 文档自身资源索引对象
     * @throws FileNotFoundException 文档自身资源索引file not found
     * @throws DocumentException     文档自身资源索引file parsing exception
     */
    public Res getDocumentRes() throws FileNotFoundException, DocumentException {
        Element obj = this.getObj(DocumentResFileName);
        return new Res(obj);
    }


    /**
     * 获取annotation list object
     *
     * @return annotation list object
     * @throws FileNotFoundException 文档自身资源索引file not found
     * @throws DocumentException     文档自身资源索引file parsing exception
     */
    public Annotations getAnnotations() throws FileNotFoundException, DocumentException {
        Element obj = this.getObj(AnnotationsFileName);
        return new Annotations(obj);
    }

    /**
     * 设置annotation list object
     * <p>
     * 推荐使用 {@link  org.ofdrw.pkg.container.AnnotsDir#setAnnotations(Annotations)}
     *
     * @param annotations annotation list object
     * @return annotation list object
     */
    public DocDir setAnnotations(Annotations annotations) {
        this.putObj(AnnotationsFileName, annotations);
        return this;
    }

    /**
     * 设置 文档自身资源索引
     *
     * @param documentRes 文档自身资源索引
     * @return this
     */
    public DocDir setDocumentRes(Res documentRes) {
        this.putObj(DocumentResFileName, documentRes);
        return this;
    }

    /**
     * 获取资源容器
     *
     * @return 资源容器
     * @throws FileNotFoundException 资源容器不存在
     */
    public ResDir getRes() throws FileNotFoundException {
        return this.getContainer(DocDir.ResDir, ResDir::new);
    }


    /**
     * 获取 resource file夹
     * <p>
     * 如果资源file not found则创建
     *
     * @return 资源目录
     */
    public ResDir obtainRes() {
        return this.obtainContainer(DocDir.ResDir, ResDir::new);
    }

    /**
     * 获取 number签名存储目录
     *
     * @return number签名存储目录
     * @throws FileNotFoundException number签名存储目录不存在
     */
    public SignsDir getSigns() throws FileNotFoundException {
        return this.getContainer(DocDir.SignsDir, SignsDir::new);
    }

    /**
     * 获取 number签名存储目录
     * <p>
     * 如果number签名存储目录不存在则创建
     *
     * @return number签名存储目录
     */
    public SignsDir obtainSigns() {
        return this.obtainContainer(DocDir.SignsDir, SignsDir::new);
    }

    /**
     * 获取 page storage directory
     *
     * @return page storage directory
     * @throws FileNotFoundException page storage directory does not exist
     */
    public PagesDir getPages() throws FileNotFoundException {
        return this.getContainer(DocDir.PagesDir, PagesDir::new);
    }

    /**
     * 获取 page storage directory
     * <p>
     * 如果page storage directory则会创建
     *
     * @return page storage directory
     */
    public PagesDir obtainPages() {
        return this.obtainContainer(DocDir.PagesDir, PagesDir::new);
    }

    /**
     * 获取 自定义标签容器
     * <p>
     * directory will be created if it does not exist
     * <p>
     * GMT0099 OFD 2.0
     *
     * @return 自定义标签容器
     */
    public VirtualContainer obtainTags() {
        return this.obtainContainer(DocDir.TagsDir, VirtualContainer::new);
    }

    /**
     * 获取 自定义标签容器
     * <p>
     * GMT0099 OFD 2.0
     *
     * @return 自定义标签容器
     * @throws FileNotFoundException page storage directory does not exist
     */
    public VirtualContainer getTags() throws FileNotFoundException {
        return this.getContainer(DocDir.TagsDir, VirtualContainer::new);
    }

    /**
     * 获取 模板容器
     * <p>
     * directory will be created if it does not exist
     * <p>
     * GMT0099 OFD 2.0
     *
     * @return 模板容器
     */
    public TempsDir obtainTemps() {
        return this.obtainContainer(DocDir.TempsDir, TempsDir::new);
    }

    /**
     * 获取 模板容器
     * <p>
     * GMT0099 OFD 2.0
     *
     * @return 模板容器
     * @throws FileNotFoundException page storage directory does not exist
     */
    public TempsDir getTemps() throws FileNotFoundException {
        return this.getContainer(DocDir.TempsDir, TempsDir::new);
    }

    /**
     * 获取 注释容器
     * <p>
     * directory will be created if it does not exist
     * <p>
     * GMT0099 OFD 2.0
     *
     * @return 注释容器
     */
    public AnnotsDir obtainAnnots() {
        return this.obtainContainer(DocDir.AnnotsDir, AnnotsDir::new);
    }

    /**
     * 获取 注释容器
     * <p>
     * GMT0099 OFD 2.0
     *
     * @return 注释容器
     * @throws FileNotFoundException page storage directory does not exist
     */
    public AnnotsDir getAnnots() throws FileNotFoundException {
        return this.getContainer(DocDir.AnnotsDir, AnnotsDir::new);
    }


    /**
     * 增加资源
     *
     * @param resource resource
     * @return this
     * @throws IOException 文件复制过程中IO exception
     */
    public DocDir addResource(Path resource) throws IOException {
        obtainRes().add(resource);
        return this;
    }

    /**
     * 增加资源 并返回资源在文档中的absolute path
     *
     * @param resource resource
     * @return 文件复制后的absolute path
     * @throws IOException 文件复制过程中IO exception
     */
    public Path addResourceWithPath(Path resource) throws IOException {
        return obtainRes().addWithPath(resource);
    }

    /**
     * 获取资源
     *
     * @param name 资源名称（contains后缀名称）
     * @return 资源
     * @throws FileNotFoundException 资源不存在
     */
    public Path getResource(String name) throws FileNotFoundException {
        return obtainRes().get(name);
    }
}

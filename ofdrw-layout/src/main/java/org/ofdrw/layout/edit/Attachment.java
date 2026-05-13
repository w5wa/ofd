package org.ofdrw.layout.edit;

import org.ofdrw.core.attachment.CT_Attachment;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 附件对象构造器
 *
 * @author Quan Guanyu
 * @since 2020-05-20 19:18:37
 */
public class Attachment {

    /**
     * 附件文件
     */
    private Path file;

    /**
     * 附件对象
     */
    private CT_Attachment atmObj;

    /**
     * 是否禁用同名替换
     * <p>
     * default value: false (replace by same name)
     */
    private boolean disableReplace;

    private Attachment() {
    }

    /**
     * 附件对象构造对象，同名文件将被替换
     *
     * @param name attachment name
     * @param file 附件文件
     */
    public Attachment(String name, Path file) {
        this(name, file, false);
    }

    /**
     * 附件对象构造对象
     *
     * @param name           attachment name
     * @param file           附件文件
     * @param disableReplace 是否禁用同名替换，默认为 false（同名替换）
     */
    public Attachment(String name, Path file, boolean disableReplace) {
        if (file == null || Files.notExists(file)) {
            throw new IllegalArgumentException("附件文件(file)为空或不存在");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("attachment name(name)不能为空");
        }
        this.file = file;
        this.disableReplace = disableReplace;
        this.atmObj = new CT_Attachment();
        this.setName(name);
    }

    /**
     * [required attribute]
     * 设置 attachment name
     *
     * @param attachmentName attachment name
     * @return this
     */
    public Attachment setName(String attachmentName) {
        atmObj.setAttachmentName(attachmentName);
        return this;
    }

    /**
     * [required attribute]
     * 获取 attachment name
     *
     * @return attachment name
     */
    public String getName() {
        return atmObj.getAttachmentName();
    }

    /**
     * [optional attribute]
     * 设置 附件格式
     *
     * @param format 附件格式
     * @return this
     */
    public Attachment setFormat(String format) {
        atmObj.setFormat(format);
        return this;
    }

    /**
     * [optional attribute]
     * 获取 附件格式
     *
     * @return 附件格式
     */
    public String getFormat() {
        return atmObj.getFormat();
    }

    /**
     * [optional attribute]
     * set creation time
     *
     * @param creationDate creation date
     * @return this
     * @deprecated {@link  #setCreationDate(LocalDateTime)}
     */
    @Deprecated
    public Attachment setCreationDate(LocalDate creationDate) {
        atmObj.setCreationDate(creationDate.atStartOfDay());
        return this;
    }

    /**
     * [optional attribute]
     * set creation time
     *
     * @param creationDate creation date
     * @return this
     */
    public Attachment setCreationDate(LocalDateTime creationDate) {
        atmObj.setCreationDate(creationDate);
        return this;
    }

    /**
     * [optional attribute]
     * get creation time
     *
     * @return creation date
     * @deprecated {@link  #getCreationDateTime()}
     */
    @Deprecated
    public LocalDate getCreationDate() {
        return atmObj.getCreationDateTime().toLocalDate();
    }

    /**
     * [optional attribute]
     * get creation time
     *
     * @return creation date
     */
    public LocalDateTime getCreationDateTime() {
        return atmObj.getCreationDateTime();
    }

    /**
     * [optional attribute]
     * set modification time
     *
     * @param modDate modification date
     * @return this
     * @deprecated {@link #setModDateTime(LocalDateTime)}
     */
    @Deprecated
    public Attachment setModDate(LocalDate modDate) {
        atmObj.setModDate(modDate.atStartOfDay());
        return this;
    }

    /**
     * [optional attribute]
     * set modification time
     *
     * @param modDate modification date
     * @return this
     */
    public Attachment setModDateTime(LocalDateTime modDate) {
        atmObj.setModDate(modDate);
        return this;
    }

    /**
     * [optional attribute]
     * get modification time
     *
     * @return modification date
     * @deprecated {@link #getCreationDateTime()}
     */
    @Deprecated
    public LocalDate getModDate() {
        return atmObj.getModDateTime().toLocalDate();
    }

    /**
     * [optional attribute]
     * get modification time
     *
     * @return modification date
     */
    public LocalDateTime getModDateTime() {
        return atmObj.getCreationDateTime();
    }

    /**
     * [optional attribute]
     * 设置 附件是否可见
     * <p>
     * default value: true
     *
     * @param visible 附件是否可见
     * @return this
     */
    public Attachment setVisible(Boolean visible) {
        atmObj.setVisible(visible);
        return this;
    }

    /**
     * [optional attribute]
     * 获取 附件是否可见
     * <p>
     * default value: true
     *
     * @return 附件是否可见
     */
    public Boolean getVisible() {
        return atmObj.getVisible();
    }

    /**
     * [optional attribute]
     * 设置 附件用途
     * <p>
     * default value: none
     *
     * @param usage 附件用途
     * @return this
     */
    public Attachment setUsage(String usage) {
        atmObj.setUsage(usage);
        return this;
    }

    /**
     * [optional attribute]
     * 获取 附件用途
     * <p>
     * default value: none
     *
     * @return 附件用途
     */
    public String getUsage() {
        return atmObj.getUsage();
    }

    /**
     * get attachment file
     *
     * @return attachment file path
     */
    public Path getFile() {
        return file;
    }

    /**
     * 设置附件文件
     *
     * @param file 附件文件
     * @return this
     */
    public Attachment setFile(Path file) {
        this.file = file;
        return this;
    }

    public CT_Attachment getAttachment() {
        return atmObj;
    }

    /**
     * 设置是否禁用同名替换
     * <p>
     * default value: false (replace by same name)
     *
     * @param disableReplace 是否禁用同名替换
     * @return this
     */
    public Attachment setDisableReplace(boolean disableReplace) {
        this.disableReplace = disableReplace;
        return this;
    }

    /**
     * 获取是否禁用同名替换
     * <p>
     * default value: false (replace by same name)
     *
     * @return 是否禁用同名替换
     */
    public boolean isDisableReplace() {
        return disableReplace;
    }
}

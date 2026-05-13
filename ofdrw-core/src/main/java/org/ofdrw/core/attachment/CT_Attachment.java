package org.ofdrw.core.attachment;

import org.dom4j.Element;
import org.ofdrw.core.Const;
import org.ofdrw.core.OFDElement;
import org.ofdrw.core.basicType.STBase;
import org.ofdrw.core.basicType.ST_Loc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

/**
 * 附件
 * <p>
 * 20.2 附件 图 92 表 73
 *
 * @author Quan Guanyu
 * @since 2019-11-21 19:23:15
 */
public class CT_Attachment extends OFDElement {
    public CT_Attachment(Element proxy) {
        super(proxy);
    }

    public CT_Attachment() {
        super("Attachment");
    }

    /**
     * [required attribute]
     * 设置 附件标识
     *
     * @param id 附件标识
     * @return this
     */
    public CT_Attachment setID(String id) {
        if (id == null || id.trim().length() == 0) {
            throw new IllegalArgumentException("附件标识（ID）为空");
        }
        this.addAttribute("ID", id);
        return this;
    }

    /**
     * [required attribute]
     * 获取 附件标识
     *
     * @return 附件标识
     */
    public String getID() {
        String str = this.attributeValue("ID");
        if (str == null || str.trim().length() == 0) {
            throw new IllegalArgumentException("附件标识（ID）为空");
        }
        return str;
    }

    /**
     * [required attribute]
     * 设置 attachment name
     *
     * @param attachmentName attachment name
     * @return this
     */
    public CT_Attachment setAttachmentName(String attachmentName) {
        if (attachmentName == null || attachmentName.trim().length() == 0) {
            throw new IllegalArgumentException("attachment name（Name）为空");
        }
        this.addAttribute("Name", attachmentName);
        return this;
    }

    /**
     * [required attribute]
     * 获取 attachment name
     *
     * @return attachment name
     */
    public String getAttachmentName() {
        String str = this.attributeValue("Name");
        if (str == null || str.trim().length() == 0) {
            throw new IllegalArgumentException("attachment name（Name）为空");

        }
        return str;
    }

    /**
     * [optional attribute]
     * 设置 附件格式
     *
     * @param format 附件格式
     * @return this
     */
    public CT_Attachment setFormat(String format) {
        if (format == null || format.trim().length() == 0) {
            this.removeAttr("Format");
            return this;
        }
        this.addAttribute("Format", format);
        return this;
    }

    /**
     * [optional attribute]
     * 获取 附件格式
     *
     * @return 附件格式
     */
    public String getFormat() {
        return this.attributeValue("Format");
    }

    /**
     * [optional attribute]
     * set creation time
     *
     * @param creationDate creation date
     * @return this
     * @deprecated {@link #setCreationDate(LocalDateTime)}
     */
    @Deprecated
    public CT_Attachment setCreationDate(LocalDate creationDate) {
        if (creationDate == null) {
            this.removeAttr("CreationDate");
            return this;
        }
        this.addAttribute("CreationDate", creationDate.atStartOfDay().format(Const.DATETIME_FORMATTER));
        return this;
    }

    /**
     * [optional attribute]
     * set creation time
     *
     * @param creationDate creation date
     * @return this
     */
    public CT_Attachment setCreationDate(LocalDateTime creationDate) {
        if (creationDate == null) {
            this.removeAttr("CreationDate");
            return this;
        }
        this.addAttribute("CreationDate", creationDate.format(Const.DATETIME_FORMATTER));
        return this;
    }

    /**
     * [optional attribute]
     * get creation time
     *
     * @return creation date
     * @deprecated {@link #getCreationDateTime()}
     */
    @Deprecated
    public LocalDate getCreationDate() {
        String str = this.attributeValue("CreationDate");
        if (str == null || str.trim().length() == 0) {
            return null;
        }
        str = str.trim();
        try {
            // improve time conversion compatibility
            return parseLocalDateTime(str).toLocalDate();
        } catch (Exception ignore) {
            return LocalDate.parse(str, Const.DATE_FORMATTER);
        }
    }

    /**
     * [optional attribute]
     * get creation time
     *
     * @return creation date
     */
    public LocalDateTime getCreationDateTime() {
        String str = this.attributeValue("CreationDate");
        if (str == null || str.trim().length() == 0) {
            return null;
        }
        str = str.trim();
        try {
            // improve time conversion compatibility
            return parseLocalDateTime(str);
        } catch (Exception ignore) {
            return LocalDate.parse(str, Const.DATE_FORMATTER).atStartOfDay();
        }
    }

    /**
     * [optional attribute]
     * set modification time
     *
     * @param modDate modification date
     * @return this
     * @deprecated {@link #setModDate(LocalDateTime)}
     */
    @Deprecated
    public CT_Attachment setModDate(LocalDate modDate) {
        if (modDate == null) {
            this.removeAttr("ModDate");
            return this;
        }
        this.addAttribute("ModDate", modDate.atStartOfDay().format(Const.DATETIME_FORMATTER));
        return this;
    }

    /**
     * [optional attribute]
     * set modification time
     *
     * @param modDate modification date
     * @return this
     */
    public CT_Attachment setModDate(LocalDateTime modDate) {
        if (modDate == null) {
            this.removeAttr("ModDate");
            return this;
        }
        this.addAttribute("ModDate", modDate.format(Const.DATETIME_FORMATTER));
        return this;
    }

    /**
     * [optional attribute]
     * get modification time
     *
     * @return modification date
     * @deprecated {@link #getModDateTime()}
     */
    @Deprecated
    public LocalDate getModDate() {
        String str = this.attributeValue("ModDate");
        if (str == null || str.trim().length() == 0) {
            return null;
        }
        str = str.trim();
        try {
            // improve time conversion compatibility
            return parseLocalDateTime(str).toLocalDate();
        } catch (Exception ignore) {
            return LocalDate.parse(str, Const.DATE_FORMATTER);
        }
    }

    /**
     * [optional attribute]
     * get modification time
     *
     * @return modification date
     */
    public LocalDateTime getModDateTime() {
        String str = this.attributeValue("ModDate");
        if (str == null || str.trim().length() == 0) {
            return null;
        }
        str = str.trim();
        try {
            // improve time conversion compatibility
            return parseLocalDateTime(str);
        } catch (Exception ignore) {
            return LocalDate.parse(str, Const.DATE_FORMATTER).atStartOfDay();
        }
    }

    /**
     * 统一处理日期时间转换，便于对日期时间格式进行兼容处理
     *
     * @param dateTimeStr 日期时间string
     * @return 日期时间
     */
    private LocalDateTime parseLocalDateTime(String dateTimeStr) {
        List<DateTimeFormatter> formatters = Arrays.asList(
                Const.LOCAL_DATETIME_FORMATTER,
                // 兼容非标准日期格式解析
                DateTimeFormatter.ofPattern("[yyyy-MM-dd][yyyy/MM/dd] HH:mm:ss"),
                DateTimeFormatter.ofPattern("yyyy[-][/][MM][M][ M][-][/][dd][d][ d] [HH][H][ H]:[mm][m][ m]:[ss][s][ s]"),
                Const.DATETIME_FORMATTER
        );
        for (DateTimeFormatter formatter : formatters) {
            try {
                return LocalDateTime.parse(dateTimeStr, formatter);
            } catch (Exception ignore) {
                // ignored解析失败，继续下一轮循环
            }
        }
        throw new IllegalArgumentException("日期时间格式不正确：" + dateTimeStr);
    }

    /**
     * [optional attribute]
     * 设置 附件大小
     * <p>
     * 以KB为单位
     *
     * @param size 附件大小，以KB为单位
     * @return this
     */
    public CT_Attachment setSize(Double size) {
        if (size == null) {
            this.removeAttr("Size");
            return this;
        }
        this.addAttribute("Size", STBase.fmt(size));
        return this;
    }

    /**
     * [optional attribute]
     * 获取 附件大小
     * <p>
     * 以KB为单位
     *
     * @return 附件大小，以KB为单位
     */
    public Double getSize() {
        String str = this.attributeValue("Size");
        if (str == null || str.trim().length() == 0) {
            return null;
        }
        return Double.parseDouble(str);
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
    public CT_Attachment setVisible(Boolean visible) {
        if (visible == null) {
            this.removeAttr("Visible");
            return this;
        }
        this.addAttribute("Visible", Boolean.toString(visible));
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
        String str = this.attributeValue("Visible");
        if (str == null || str.trim().length() == 0) {
            return true;
        }
        return Boolean.parseBoolean(str);
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
    public CT_Attachment setUsage(String usage) {
        if (usage == null || usage.trim().length() == 0) {
            this.removeAttr("Usage");
            return this;
        }
        this.addAttribute("Usage", usage);
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
        String str = this.attributeValue("Usage");
        if (str == null || str.trim().length() == 0) {
            return "none";
        }
        return str;
    }


    /**
     * [optional]
     * 设置 附件内容在包内的路径
     *
     * @param fileLoc 附件内容在包内的路径
     * @return this
     */
    public CT_Attachment setFileLoc(ST_Loc fileLoc) {
        if (fileLoc == null) {
            this.removeOFDElemByNames("FileLoc");
            return this;
        }
        this.setOFDEntity("FileLoc", fileLoc);
        return this;
    }

    /**
     * [optional]
     * 获取 附件内容在包内的路径
     *
     * @return 附件内容在包内的路径
     */
    public ST_Loc getFileLoc() {
        Element e = this.getOFDElement("FileLoc");
        return e == null ? null : ST_Loc.getInstance(e);
    }
}

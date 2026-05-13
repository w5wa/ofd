package org.ofdrw.core;

import org.dom4j.Namespace;

import java.time.format.DateTimeFormatter;

/**
 * 静态变量
 *
 * @author Quan Guanyu
 * @since 2019-09-27 10:22:57
 */
public class Const {

    /**
     * namespace URI,《GB/T_33190-2016》 7.1 namespace
     */
    public static final String OFD_NAMESPACE_URI = "http://www.ofdspec.org/2016";
    /**
     * element node应使用namespace identifier
     * — GB/T 33190-2016, Section 7.1: Namespaces
     */
    public static final String OFD_VALUE = "ofd";
    /**
     * OFD namespace
     */
    public static final String OFD_Q = "ofd:";

    /**
     * 使用namespace为 http://www.ofdspec.org/2016，其表示符应为 ofd。
     * — GB/T 33190-2016, Section 7.1: Namespaces
     */
    public static final Namespace OFD_NAMESPACE = new Namespace("ofd", OFD_NAMESPACE_URI);
    public static final Namespace OFD_NAMESPACE_DEFAULT = new Namespace("", OFD_NAMESPACE_URI);

    /**
     * xs:date 类型日期格式化
     */
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    /**
     * xs:dateTime 类型时间日期格式化
     */
    public static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    /**
     * xs:dateTime 类型本地时间日期格式化
     */
    public static final DateTimeFormatter LOCAL_DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * OFD索引文件
     */
    public static final String INDEX_FILE = "OFD.xml";


}

package org.ofdrw.core.basicStructure.doc.permission;

import org.dom4j.Element;
import org.ofdrw.core.OFDElement;

/**
 * 打印权限
 * <p>
 * specific permission and copy settings are controlled by attributes Printable and Copies. If the Print node is absent,
 * printing is allowed by default with no copy limit
 * <p>
 * 7.5 Figure 9: Document Permission Declaration Structure
 *
 * @author Quan Guanyu
 * @since 2019-10-07 05:07:02
 */
public class Print extends OFDElement {
    public Print(Element proxy) {
        super(proxy);
    }

    public Print() {
        super("Print");
    }

    public Print(boolean printable,int copies) {
        this();
        this.setPrintable(printable)
                .setCopies(copies);
    }


    /**
     * [optional/required]
     * 设置 是否允许被打印
     * <p>
     * default value: true
     *
     * @param printable true - 允许被打印； false - 不允许被打印
     * @return this
     */
    public Print setPrintable(boolean printable) {
        this.addAttribute("Printable", Boolean.toString(printable));
        return this;
    }

    /**
     * [optional/required]
     * 获取 是否允许被打印
     * <p>
     * default value: true
     *
     * @return true - 允许被打印； false - 不允许被打印
     */
    public Boolean getPrintable() {
        String str = this.attributeValue("Printable");
        if (str == null || str.trim().length() == 0) {
            return true;
        }
        return Boolean.parseBoolean(str);
    }

    /**
     * [optional attribute]
     * 设置 打印份数
     * <p>
     * valid when Printable is true; if Printable is true
     * and Copies is not set, printing is unlimited; if Copies is negative,
     * printing is unlimited; when Copies is 0, printing is not allowed; when Copies
     * is greater than 0, it represents the actual number of permitted copies.
     * <p>
     * default value: -1
     *
     * @param copies 可打印的份数
     * @return this
     */
    public Print setCopies(int copies) {
        this.addAttribute("Copies", Integer.toString(copies));
        return this;
    }

    /**
     * [optional attribute]
     * 获取 打印份数
     * <p>
     * valid when Printable is true; if Printable is true
     * and Copies is not set, printing is unlimited; if Copies is negative,
     * printing is unlimited; when Copies is 0, printing is not allowed; when Copies
     * is greater than 0, it represents the actual number of permitted copies.
     * <p>
     * default value: -1
     *
     * @return 可打印的份数
     */
    public Integer getCopies() {
        String str = this.attributeValue("Copies");
        if (str == null || str.trim().length() == 0) {
            return -1;
        }
        return Integer.parseInt(str);
    }
}

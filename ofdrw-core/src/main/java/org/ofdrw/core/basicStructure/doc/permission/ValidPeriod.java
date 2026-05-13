package org.ofdrw.core.basicStructure.doc.permission;

import org.dom4j.Element;
import org.ofdrw.core.Const;
import org.ofdrw.core.OFDElement;

import java.time.LocalDateTime;

/**
 * 有效期
 * <p>
 * the access period for this document is determined by the start date and
 * end date; the start date cannot be later than the end date, and both start and end
 * dates must have at least one present. When start date is absent, there is no start date restriction;
 * when end date is absent, there is no end date restriction; when this node is absent,
 * neither start nor end date is restricted
 * <p>
 * 7.5 Figure 9: Document Permission Declaration Structure
 *
 * @author Quan Guanyu
 * @since 2019-10-07 05:21:06
 */
public class ValidPeriod extends OFDElement {
    public ValidPeriod(Element proxy) {
        super(proxy);
    }

    public ValidPeriod() {
        super("ValidPeriod");
    }

    public ValidPeriod(LocalDateTime startDate, LocalDateTime endDate) {
        this();
        this.setStartDate(startDate)
                .setEndDate(endDate);
    }

    /**
     * [optional attribute]
     * 设置 有效期开始日期
     *
     * @param startDate 有效期开始日期
     * @return this
     */
    public ValidPeriod setStartDate(LocalDateTime startDate) {
        if (startDate == null) {
            this.removeAttr("StartDate");
            return this;
        }
        this.addAttribute("StartDate", startDate.format(Const.DATETIME_FORMATTER));
        return this;
    }

    /**
     * [optional attribute]
     * 获取 有效期开始日期
     *
     * @return 有效期开始日期
     */
    public LocalDateTime getStartDate() {
        String str = this.attributeValue("StartDate");
        if (str == null || str.trim().length() == 0) {
            return null;
        }
        return LocalDateTime.parse(str, Const.DATETIME_FORMATTER);
    }


    /**
     * [optional attribute]
     * 设置 有效期结束日期
     *
     * @param endDate 有效期结束日期
     * @return this
     */
    public ValidPeriod setEndDate(LocalDateTime endDate) {
        if (endDate == null) {
            this.removeAttr("EndDate");
            return this;
        }
        this.addAttribute("EndDate", endDate.format(Const.DATETIME_FORMATTER));
        return this;
    }

    /**
     * [optional attribute]
     * 获取 有效期结束日期
     *
     * @return 有效期结束日期
     */
    public LocalDateTime getEndDate() {
        String str = this.attributeValue("EndDate");
        if (str == null || str.trim().length() == 0) {
            return null;
        }
        return LocalDateTime.parse(str, Const.DATETIME_FORMATTER);
    }
}

package org.ofdrw.core.pageDescription.color.colorSpace;

/**
 * color space的类型
 * <p>
 * 8.3.1 表 25 color space属性
 *
 * @author Quan Guanyu
 * @since 2019-10-11 08:07:31
 */
public enum OFDColorSpaceType {
    /**
     * 灰度
     */
    GRAY,
    /**
     * 红绿蓝
     */
    RGB,
    /**
     * 印刷颜色
     */
    CMYK;

    /**
     * get instance
     * 
     * @param type type string
     * @return instance
     * @throws IllegalArgumentException 未知的color space类型
     */
    public static OFDColorSpaceType getInstance(String type) {
        type = (type == null) ? "" : type.trim();

        // switch (type) {
        // case "GRAY":
        // return GRAY;
        // case "RGB":
        // return RGB;
        // case "CMYK":
        // return CMYK;
        // default:
        // throw new IllegalArgumentException("未知的color space类型：" + type);
        // }
        if (type.equalsIgnoreCase("GRAY")) {
            return GRAY;
        } else if (type.equalsIgnoreCase("RGB")) {
            return RGB;
        } else if (type.equalsIgnoreCase("CMYK")) {
            return CMYK;
        } else {
            throw new IllegalArgumentException("未知的color space类型：" + type);
        }
    }
}

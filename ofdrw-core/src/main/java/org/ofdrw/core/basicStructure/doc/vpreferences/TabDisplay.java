package org.ofdrw.core.basicStructure.doc.vpreferences;

/**
 * 标题栏显示模式
 * <p>
 * default value: FileName; when set to DocTitle but Title attribute is absent,
 * treat it as FileName
 * <p>
 * 7.5 Table 9 View Preferences
 *
 * @author Quan Guanyu
 * @since 2019-10-07 09:07:50
 */
public enum TabDisplay {
    /**
     * file name
     */
    FileName,
    /**
     * 呈现元数据中的 Title 属性
     */
    DocTitle;

    public static TabDisplay getInstance(String tabDisplay) {
        if (tabDisplay == null || tabDisplay.trim().length() == 0) {
            return FileName;
        }
        // switch (tabDisplay) {
        //     case "FileName":
        //         return FileName;
        //     case "DocTitle":
        //         return DocTitle;
        //     default:
        //         throw new IllegalArgumentException("未知的标题栏显示模式： " + tabDisplay);
        // }
        if (tabDisplay.equalsIgnoreCase("FileName")) {
            return FileName;
        } else if (tabDisplay.equalsIgnoreCase("DocTitle")) {
            return DocTitle;
        } else {
            throw new IllegalArgumentException("未知的标题栏显示模式： " + tabDisplay);
        }
    }
}

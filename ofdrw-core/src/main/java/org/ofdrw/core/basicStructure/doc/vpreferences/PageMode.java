package org.ofdrw.core.basicStructure.doc.vpreferences;

/**
 * 窗口模式
 * <p>
 * 7.5 表 9 视图首选项属性
 * <p>
 * default value: None
 *
 * @author Quan Guanyu
 * @since 2019-10-07 06:33:01
 */
public enum PageMode {
    /**
     * 常规模式
     */
    None,
    /**
     * 开开后全文显示
     */
    FullScreen,
    /**
     * 同时呈现文档大纲
     */
    UseOutlines,
    /**
     * 同时呈现缩略图
     */
    UseThumbs,
    /**
     * 同时呈现语义结构
     */
    UseCustomTags,
    /**
     * 同时呈现layer
     */
    UseLayers,
    /**
     * 同时呈现附件
     */
    UseAttatchs,

    /**
     * 同时呈现书签
     */
    UseBookmarks;

    /**
     * 获取窗口模式instance
     *
     * @param mode 模式名称
     * @return instance
     */
    public static PageMode getInstance(String mode) {
        mode = mode == null ? "" : mode.trim();
        // switch (mode) {
        //     case "":
        //     case "None":
        //         return None;
        //     case "FullScreen":
        //         return FullScreen;
        //     case "UseOutlines":
        //         return UseOutlines;
        //     case "UseThumbs":
        //         return UseThumbs;
        //     case "UseCustomTags":
        //         return UseCustomTags;
        //     case "UseLayers":
        //         return UseLayers;
        //     case "UseAttatchs":
        //         return UseAttatchs;
        //     case "UseBookmarks":
        //         return UseBookmarks;
        //     default:
        //         throw new IllegalArgumentException("未知的窗口模式：" + mode);
        // }
        if (mode.equals("") || mode.equalsIgnoreCase("None")) {
            return None;
        } else if (mode.equalsIgnoreCase("FullScreen")) {
            return FullScreen;
        } else if (mode.equalsIgnoreCase("UseOutlines")) {
            return UseOutlines;
        } else if (mode.equalsIgnoreCase("UseThumbs")) {
            return UseThumbs;
        } else if (mode.equalsIgnoreCase("UseCustomTags")) {
            return UseCustomTags;
        } else if (mode.equalsIgnoreCase("UseLayers")) {
            return UseLayers;
        } else if (mode.equalsIgnoreCase("UseAttatchs")) {
            return UseAttatchs;
        } else if (mode.equalsIgnoreCase("UseBookmarks")) {
            return UseBookmarks;
        } else {
            throw new IllegalArgumentException("未知的窗口模式：" + mode);
        }
    }
}

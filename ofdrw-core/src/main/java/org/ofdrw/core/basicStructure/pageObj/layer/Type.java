package org.ofdrw.core.basicStructure.pageObj.layer;

/**
 * layer类型
 * <p>
 * 统称类型分为foreground layer、body layer、background layer，这些层按照出现的
 * 先后顺序依次进行渲染，每一层的默认颜色采用透明。
 * <p>
 * 层的渲染顺序如下图 （图 16 layer渲染顺序）
 * <code>
 * ---------- 最上层
 * foreground layer (5)
 * ----------
 * [前景模板] (4)
 * ----------
 * body layer (3)
 * ----------
 * [body layer] (2)
 * ----------
 * background layer (1)
 * ----------
 * [background layer] (0)
 * ---------- 最下层
 * </code>
 * <p>
 * 7.7 页对象
 *
 * @author Quan Guanyu
 * @since 2019-10-09 10:01:01
 */
public enum Type {
    /**
     * foreground layer
     */
    Foreground(5),
    /**
     * body layer
     */
    Body(3),
    /**
     * background layer
     */
    Background(1);

    private int order;


    Type(int order) {
        this.order = order;
    }

    /**
     * 获取layer类型instance
     *
     * @param type layertype string
     * @return layer类型
     */
    public static Type getInstance(String type) {
        type = (type == null) ? "" : type.trim();
        // switch (type) {
        //     case "":
        //     case "Body":
        //         return Body;
        //     case "Foreground":
        //         return Foreground;
        //     case "Background":
        //         return Background;
        //     default:
        //         throw new IllegalArgumentException("未知的layer类型：" + type);
        // }
        if (type.equalsIgnoreCase("Body") || "".equals(type)) {
            return Body;
        } else if (type.equalsIgnoreCase("Foreground")) {
            return Foreground;
        } else if (type.equalsIgnoreCase("Background")) {
            return Background;
        } else {
            throw new IllegalArgumentException("未知的layer类型：" + type);
        }
    }

    /**
     * 获取layer次序
     *
     * @return layer次序
     */
    public int order() {
        return order;
    }
}

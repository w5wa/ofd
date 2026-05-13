package org.ofdrw.core.graph.pathObj;

import org.dom4j.Element;
import org.ofdrw.core.pageDescription.color.color.CT_AxialShd;
import org.ofdrw.core.pageDescription.color.color.CT_Color;

/**
 * outline color
 * <p>
 * 9.1 表 35
 *
 * @author Quan Guanyu
 * @since 2019-10-27 03:23:49
 */
public class StrokeColor extends CT_Color {
    public StrokeColor(Element proxy) {
        super(proxy);
    }

    public StrokeColor() {
        super("StrokeColor");
    }
}

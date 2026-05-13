package org.ofdrw.core.image;

import org.dom4j.Element;
import org.ofdrw.core.pageDescription.color.color.CT_Color;

/**
 * border color
 * <p>
 * see 8.3.2 Basic Colors for border color description
 * <p>
 * default: black
 *
 * @author Quan Guanyu
 * @since 2019-10-27 03:36:42
 */
public class BorderColor extends CT_Color {
    public BorderColor(Element proxy) {
        super(proxy);
    }

    public BorderColor() {
        super("BorderColor");
    }
}

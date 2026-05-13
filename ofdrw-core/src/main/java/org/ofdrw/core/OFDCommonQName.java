package org.ofdrw.core;

import org.dom4j.QName;

/**
 * OFD common  qualified name
 * <p>
 * as long as the names are the same并且namespace prefixkeep consistent就认为是同一种  qualified name
 *
 * @author Quan Guanyu
 * @since 2020-09-15 21:14:27
 */
public class OFDCommonQName extends QName {
    // common OFD namespace前缀
    public static final String CommonOFDNameSpacePrefix = "http://www.ofdspec.org";

    /**
     * @param name OFDelement name
     */
    public OFDCommonQName(String name) {
        super(name, Const.OFD_NAMESPACE);
    }

    /**
     * Name相同并且，as long asnamespace prefix相同那么
     * 那么determined to be equalqualified name
     *
     * @param object object to compare
     * @return true - same; false - different
     */
    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (object instanceof QName) {
            QName that = (QName) object;
            return getName().equals(that.getName())
                    && that.getNamespaceURI().startsWith(CommonOFDNameSpacePrefix);
        }
        return false;
    }
}

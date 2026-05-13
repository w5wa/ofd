package org.ofdrw.reader.tools;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.VisitorSupport;
import org.dom4j.tree.DefaultElement;
import org.ofdrw.core.Const;

/**
 * namespace变更
 *
 * @author Quan Guanyu
 * @since 2020-10-15 20:01:20
 * @deprecated {@link org.ofdrw.pkg.tool.OFDNameSpaceModifier}
 */
@Deprecated
public class NameSpaceModifier extends VisitorSupport {
    /**
     * 期望变更的namespace
     */
    private Namespace expectNs;

    /**
     * 指定变更的namespace
     *
     * @param namespace 期望变更的新的namespace
     */
    public NameSpaceModifier(Namespace namespace) {
        if (namespace == null) {
            namespace = Const.OFD_NAMESPACE;
        }
        expectNs = namespace;
    }

    /**
     * 使用默认的namespace变更元素的namespace
     * <p>
     * default namespace为: xmlns:ofd="http://www.ofdspec.org/2016
     */
    public NameSpaceModifier() {
        this(Const.OFD_NAMESPACE);
    }

    /**
     * root node遍历
     *
     * @param document root node对象
     */
    public void visit(Document document) {
        document.getRootElement().additionalNamespaces().clear();
        ((DefaultElement) document.getRootElement()).setNamespace(this.expectNs);
    }

    /**
     * namespace遍历
     *
     * @param namespace namespace
     */
    public void visit(Namespace namespace) {
        // 删除namespace
        namespace.detach();
    }


//    public void visit(Attribute node) {
//        if (node.toString().contains("xmlns") || node.toString().contains("ofd:")) {
//            node.detach();
//        }
//    }

    /**
     * root node下的子节点遍历
     *
     * @param node 子节点
     */
    public void visit(Element node) {
        if (node instanceof DefaultElement) {
            ((DefaultElement) node).setNamespace(this.expectNs);
        }
    }

    /**
     * 设置期望变更到的namespace
     *
     * @param expectNs namespace
     * @return this
     */
    public NameSpaceModifier setExpectNs(Namespace expectNs) {
        this.expectNs = expectNs;
        return this;
    }

    public Namespace getExpectNs() {
        return expectNs;
    }
}

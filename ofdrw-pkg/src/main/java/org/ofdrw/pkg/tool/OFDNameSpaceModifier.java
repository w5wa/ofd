package org.ofdrw.pkg.tool;

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
 */
public class OFDNameSpaceModifier extends VisitorSupport {
    /**
     * 期望变更的namespace
     */
    private Namespace expectNs;

    /**
     * 指定变更的namespace
     *
     * @param namespace 期望变更的新的namespace
     */
    public OFDNameSpaceModifier(Namespace namespace) {
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
    public OFDNameSpaceModifier() {
        this(Const.OFD_NAMESPACE);
    }

    /**
     * root node遍历
     *
     * @param document root node对象
     */
    public void visit(Document document) {
        final DefaultElement rootElement = (DefaultElement) document.getRootElement();
        if (rootElement == null) {
            return;
        }
        // 如果namespace不同，那么更新namespace
        if(!nsEqual(rootElement)){
            rootElement.setNamespace(this.expectNs);
            rootElement.additionalNamespaces().clear();
        }
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
            final DefaultElement element = (DefaultElement) node;
            if (!nsEqual(element)){
                element.setNamespace(this.expectNs);
            }
        }
    }

    public Namespace getExpectNs() {
        return expectNs;
    }

    /**
     * namespace是否一致
     *
     * @param e 需要比较的元素
     * @return true - 一致；false - 不一致
     */
    private boolean nsEqual(Element e) {
        final Namespace n = e.getNamespace();
        return expectNs.getPrefix().equals(n.getPrefix()) && expectNs.getText().equals(n.getText());
    }
}

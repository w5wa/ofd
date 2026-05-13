package org.ofdrw.core.action;

import org.dom4j.Element;
import org.ofdrw.core.OFDElement;

import java.util.ArrayList;
import java.util.List;

/**
 * action sequence
 * <p>
 * Figure 19: Outline Node Structure
 *
 * @author Quan Guanyu
 * @since 2019-10-05 11:32:31
 */
public class Actions extends OFDElement {
    public Actions(Element proxy) {
        super(proxy);
    }

    public Actions() {
        super("Actions");
    }

    /**
     * [required]
     * 增加 到动作列表
     * <p>
     * 当此outline node被激活时将执行的动作，关于动作的描述详见第 14 章
     *
     * @param action 动作
     * @return this
     */
    public Actions addAction(CT_Action action) {
        this.add(action);
        return this;
    }

    /**
     * [required]
     * 获取 动作列表
     * <p>
     * 当此outline node被激活时将依次执行的动作，关于动作的描述详见第 14 章
     *
     * @return 动作
     */
    public List<CT_Action> getActions() {
        List<Element> elements = this.elements();
        List<CT_Action> res = new ArrayList<>(elements.size());
        elements.forEach(item -> res.add(new CT_Action(item)));
        return res;
    }
}

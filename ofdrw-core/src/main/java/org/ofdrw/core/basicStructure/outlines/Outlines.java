package org.ofdrw.core.basicStructure.outlines;

import org.dom4j.Element;
import org.ofdrw.core.OFDElement;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * 大纲按照树形结构进行组织
 * <p>
 * 图 18 outline node结构
 *
 * @author Quan Guanyu
 * @since 2019-10-05 11:12:39
 */
public class Outlines extends OFDElement implements Iterable<CT_OutlineElem> {
    public Outlines(Element proxy) {
        super(proxy);
    }

    public Outlines() {
        super("Outlines");
    }

    /**
     * [required]
     * add outline node
     *
     * @param outlineElem outline node
     * @return this
     */
    public Outlines addOutlineElem(CT_OutlineElem outlineElem) {
        this.add(outlineElem);
        return this;
    }

    /**
     * [required]
     * add outline node
     *
     * @return outline node
     */
    public List<CT_OutlineElem> getOutlineElems() {
        return this.getOFDElements("OutlineElem",CT_OutlineElem::new);
    }

    @Override
    public Iterator<CT_OutlineElem> iterator() {
        return getOutlineElems().iterator();
    }

    @Override
    public void forEach(Consumer<? super CT_OutlineElem> action) {
        getOutlineElems().forEach(action);
    }

    /**
     * 未实现 不可使用
     *
     * @return null
     */
    @Deprecated
    @Override
    public Spliterator<CT_OutlineElem> spliterator() {
        return null;
    }
}

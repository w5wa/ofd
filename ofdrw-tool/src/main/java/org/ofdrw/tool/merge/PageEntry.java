package org.ofdrw.tool.merge;


import java.util.Arrays;
import java.util.List;

/**
 * 页面项目
 *
 * @author Quan Guanyu
 * @since 2021-11-08 20:52:49
 */
public class PageEntry {

    /**
     * 关联document context
     */
    public DocContext docCtx;

    /**
     * page index号（page number从1开始）
     */
    public Integer pageIndex;

    /**
     * 是否复制模板
     * <p>
     * 默认：true 复制模板
     */
    public boolean copyTemplate = true;

    /**
     * 是否复注释
     * <p>
     * 默认：true 复制注释
     */
    public boolean copyAnnotations = true;


    /**
     * 需要混合到指定页面的其他文档页面（将一页追加到指定页的上面）
     */
    public List<PageEntry> tbMixPages =null;

    /**
     * 创建迁移page object
     *
     * @param pageIndex page index号（page number从1开始）
     * @param docCtx    上下文
     */
    public PageEntry(Integer pageIndex, DocContext docCtx) {
        this.docCtx = docCtx;
        this.pageIndex = pageIndex;
    }


    /**
     * 创建迁移page object
     *
     * @param pageIndex page index号（page number从1开始）
     * @param docCtx    上下文
     * @param tbMixPages 需要混合到指定页面的其他文档页面。
     */
    public PageEntry(Integer pageIndex, DocContext docCtx, PageEntry... tbMixPages) {
        this.docCtx = docCtx;
        this.pageIndex = pageIndex;
        if (tbMixPages != null && tbMixPages.length > 0) {
            this.tbMixPages = Arrays.asList(tbMixPages);
        }
    }
}

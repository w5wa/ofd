package org.ofdrw.reader.keyword;

import org.ofdrw.core.basicType.ST_Box;

/**
 * keyword位置
 *
 * @author minghu-zhang
 * @since 16:25 2020/9/26
 */
public class KeywordPosition {
    /**
     * keyword所在page number
     */
    private int page;
    /**
     * 矩形区域
     */
    private ST_Box box;
    /**
     * 所属keyword
     */
    private String keyword;

    public KeywordPosition(int page, ST_Box box) {
        this.page = page;
        this.box = box;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public ST_Box getBox() {
        return box;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public void setBox(ST_Box box) {
        this.box = box;
    }

    @Override
    public String toString() {
        return "KeywordPosition{" +
                "page=" + page +
                ", box=" + box +
                ", keyword='" + keyword + '\'' +
                '}';
    }
}

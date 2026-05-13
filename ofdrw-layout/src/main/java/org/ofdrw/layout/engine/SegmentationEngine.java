package org.ofdrw.layout.engine;

import org.ofdrw.layout.PageLayout;
import org.ofdrw.layout.element.BR;
import org.ofdrw.layout.element.Div;
import org.ofdrw.layout.element.Position;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * 分segment引擎
 * <p>
 * 用于将流式文档中的元素划分为segment。
 *
 * @author Quan Guanyu
 * @since 2020-02-29 11:39:29
 */
public class SegmentationEngine {

    private PageLayout pageLayout;

    private SegmentationEngine() {
    }

    public SegmentationEngine(PageLayout pageLayout) {
        this.pageLayout = pageLayout;
    }

    /**
     * 将输入的flow layout element queue分segment
     *
     * @param streamLayoutQueue flow layout element queue
     * @return 分完segment的布局队列
     */
    public List<Segment> process(List<Div> streamLayoutQueue) {
        // 可用于布局的width
        double width = pageLayout.contentWidth();
        if (streamLayoutQueue == null || streamLayoutQueue.isEmpty()) {
            return Collections.emptyList();
        }
        LinkedList<Segment> res = new LinkedList<>();
        LinkedList<Div> seq = new LinkedList<>(streamLayoutQueue);
        Segment segment = new Segment(width);
        while (!seq.isEmpty()) {
            Div div = seq.pop();
            if (div.getPosition() == Position.Absolute) {
                continue;
            }
            if (div instanceof BR) {
                if (segment.isEmpty()) {
                    // 如果segment为空，那么不需要换行
                    continue;
                }
                // 换行符
                res.add(segment);
                segment = new Segment(width);
                continue;
            }
            // 尝试将元素加入segment中
            boolean addSuccess = segment.tryAdd(div);
            // 如果segment已经满了，那么加入了segment队列中
            if (!addSuccess && !segment.isEmpty()) {
                // segment已经无法再容纳元素： 无法加入元素且不为空
                res.add(segment);
                segment =  new Segment(width);
                seq.push(div);
            }
        }
        // 处理最后一个segment的情况
        if (!segment.isEmpty()) {
            res.add(segment);
        }
        return res;
    }
}

package org.ofdrw.core.action.actionType;

import org.dom4j.Element;
import org.ofdrw.core.OFDElement;
import org.ofdrw.core.basicType.ST_RefID;

/**
 * 播放音频动作
 * <p>
 * Sound 动作表明播放一segment音频
 * <p>
 * 图 78 播放音频动作结构
 *
 * @author Quan Guanyu
 * @since 2019-10-05 09:48:46
 */
public class Sound extends OFDElement implements OFDAction {
    public Sound(Element proxy) {
        super(proxy);
    }

    public Sound() {
        super("Sound");
    }

    public Sound(ST_RefID resourceID) {
        this();
        this.setResourceID(resourceID);
    }

    /**
     * [required attribute]
     * 设置 引用resource file中的音频资源标识符
     *
     * @param resourceId 引用resource file中的音频资源标识符
     * @return this
     */
    public Sound setResourceID(ST_RefID resourceId) {
        this.addAttribute("ResourceID", resourceId.toString());
        return this;
    }

    /**
     * [required attribute]
     * 获取 引用resource file中的音频资源标识符
     *
     * @return 引用resource file中的音频资源标识符
     */
    public ST_RefID getResourceID() {
        return ST_RefID.getInstance(this.attributeValue("ResourceID"));
    }

    /**
     * [optional attribute]
     * 设置 播放音量，取值范围[0,100]
     *
     * @param volume 播放音量，取值范围[0,100]
     * @return this
     */
    public Sound setVolume(int volume) {
        volume = Math.max(volume, 0);
        volume %= 100;
        this.addAttribute("Volume", Integer.toString(volume));
        return this;
    }

    /**
     * [optional attribute]
     * 获取 播放音量，取值范围[0,100]
     *
     * @return 播放音量，取值范围[0,100]
     */
    public Integer getVolume() {
        String str = this.attributeValue("Volume");
        return Integer.parseInt(str);
    }

    /**
     * [optional attribute]
     * 设置  此音频是否需要同步播放
     * <p>
     * 如果此属性为 true，则 Synchronous 值无效
     * <p>
     * default value: false
     *
     * @param repeat true - 同步； false - 异步
     * @return this
     */
    public Sound setRepeat(Boolean repeat) {
        if (repeat == null) {
            this.removeAttr("Repeat");
            return this;
        }
        this.addAttribute("Repeat", Boolean.toString(repeat));
        return this;
    }

    /**
     * [optional attribute]
     * 获取  此音频是否需要同步播放
     * <p>
     * 如果此属性为 true，则 Synchronous 值无效
     * <p>
     * default value: false
     *
     * @return true - 同步； false - 异步
     */
    public Boolean getRepeat() {
        String str = this.attributeValue("Repeat");
        if (str == null || str.trim().length() == 0) {
            return false;
        }
        return Boolean.parseBoolean(str);
    }


    /**
     * [optional attribute]
     * 设置 是否同步播放
     * <p>
     * true 表示后续动作应等待此音频播放结束后才能开始，
     * false 表示立刻返回并开始下一个动作
     *
     * @param synchronous true - 同步顺序播放；false - 立刻返回开始下一个动作
     * @return this
     */
    public Sound setSynchronous(Boolean synchronous) {
        if (synchronous == null) {
            this.removeAttr("Synchronous");
            return this;
        }
        this.addAttribute("Synchronous", Boolean.toString(synchronous));
        return this;
    }

    /**
     * [optional attribute]
     * 获取 是否同步播放
     * <p>
     * true 表示后续动作应等待此音频播放结束后才能开始，
     * false 表示立刻返回并开始下一个动作
     *
     * @return true - 同步顺序播放；false - 立刻返回开始下一个动作
     */
    public Boolean getSynchronous() {
        String str = this.attributeValue("Synchronous");
        if (str == null || str.trim().length() == 0) {
            return false;
        }
        return Boolean.parseBoolean(str);
    }

}

package org.ofdrw.core.action.actionType.actionMovie;

/**
 * 放映参数属性
 * <p>
 * 表 59 放映参数属性
 *
 * @author Quan Guanyu
 * @since 2019-10-06 05:56:41
 */
public enum PlayType {
    /**
     * 播放
     */
    Play,
    /**
     * 停止
     */
    Stop,
    /**
     * 暂停
     */
    Pause,
    /**
     * 继续
     */
    Resume;

    /**
     * 根据string类型获取 instance
     *
     * @param type 放映参数string
     * @return instance
     */
    public static PlayType getInstance(String type) {
        type = type == null ? "" : type.trim();
        // switch (type) {
        //     case "Play":
        //         return Play;
        //     case "Stop":
        //         return Stop;
        //     case "Pause":
        //         return Pause;
        //     case "Resume":
        //         return Resume;
        //     default:
        //         throw new IllegalArgumentException("未知的放映参数： " + type);
        // }
        if (type.equalsIgnoreCase("Play")) {
            return Play;
        } else if (type.equalsIgnoreCase("Stop")) {
            return Stop;
        } else if (type.equalsIgnoreCase("Pause")) {
            return Pause;
        } else if (type.equalsIgnoreCase("Resume")) {
            return Resume;
        } else {
            throw new IllegalArgumentException("未知的放映参数： " + type);
        }
    }
}

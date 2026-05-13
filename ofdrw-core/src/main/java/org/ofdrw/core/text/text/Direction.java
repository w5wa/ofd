package org.ofdrw.core.text.text;

/**
 * 方向角度
 * <p>
 * 11.3 文字定位 表 47
 *
 * @author Quan Guanyu
 * @since 2019-10-18 09:45:46
 */
public enum Direction {
    /**
     * 可选rotation angle 0、90、180、270
     */
    Angle_0(0),
    Angle_90(90),
    Angle_180(180),
    Angle_270(270);

    /**
     * rotation angle
     */
    private Integer angle;
    Direction(int angle) {
        this.angle = angle;
    }

    public static Direction getInstance(Integer angleStr) {
        return getInstance(String.valueOf(angleStr));
    }

    public static Direction getInstance(String angleStr) {
        if (angleStr == null || angleStr.trim().length() == 0) {
            angleStr = "0";
        }
        switch (angleStr) {
            case "":
            case "0":
                return Angle_0;
            case "90":
                return Angle_90;
            case "180":
                return Angle_180;
            case "270":
                return Angle_270;
            default:
                throw new NumberFormatException("未知rotation angle：" + angleStr);
        }
    }

    @Override
    public String toString() {
        return angle.toString();
    }
}

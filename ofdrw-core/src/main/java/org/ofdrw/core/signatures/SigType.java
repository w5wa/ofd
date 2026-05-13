package org.ofdrw.core.signatures;

/**
 * 签名节点的类型
 * <p>
 * 目前规定了两个可选值
 *
 * 18.1 Signature List - Figure 85 Table 66
 *
 * @author Quan Guanyu
 * @since 2019-11-20 06:59:41
 */
public enum SigType {
    /**
     * 安全seal/signature
     * <p>
     * default value
     */
    Seal,
    /**
     * 纯number签名
     */
    Sign;

    public static SigType getInstance(String str) {
        str = (str == null) ? "" : str.trim();
        // switch (str) {
        //     case "":
        //     case "Seal":
        //         return Seal;
        //     case "Sign":
        //         return Sign;
        //     default:
        //         throw new IllegalArgumentException("未知的签名节点的类型：" + str);
        // }
        if  (str.equals("") || str.equalsIgnoreCase("Seal")) {
            return Seal;
        } else if (str.equalsIgnoreCase("Sign")) {
            return Sign;
        } else {
            throw new IllegalArgumentException("未知的签名节点的类型：" + str);
        }
    }
}

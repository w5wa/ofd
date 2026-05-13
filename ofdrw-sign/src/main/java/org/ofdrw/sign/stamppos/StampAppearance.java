package org.ofdrw.sign.stamppos;

import org.ofdrw.core.signatures.appearance.StampAnnot;
import org.ofdrw.reader.OFDReader;
import org.ofdrw.sign.SignIDProvider;

import java.util.List;

/**
 * seal/signature外观位置提供者
 *
 * @author Quan Guanyu
 * @since 2020-04-18 10:47:41
 */
public interface StampAppearance {

    /**
     * 获取seal/signature外观
     *
     * @param ctx OFD虚拟容器
     * @param idProvider signature ID提供器
     * @return seal/signature外观列表
     */
    List<StampAnnot> getAppearance(OFDReader ctx, SignIDProvider idProvider);
}

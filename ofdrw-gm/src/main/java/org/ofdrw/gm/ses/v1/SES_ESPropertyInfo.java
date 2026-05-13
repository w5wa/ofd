package org.ofdrw.gm.ses.v1;

import org.bouncycastle.asn1.*;

import java.util.Enumeration;


/**
 * seal attribute information
 *
 * @author Quan Guanyu
 * @since 2020-04-19 15:03:38
 */
public class SES_ESPropertyInfo extends ASN1Object {
    /**
     * 单位seal/stamp类型
     */
    public static final ASN1Integer OrgType = new ASN1Integer(1);
    /**
     * 个人seal/stamp类型
     */
    public static final ASN1Integer PersonType = new ASN1Integer(1);

    /**
     * seal/stamp类型
     * <p>
     * 1 - 单位seal/stamp
     * 2 - 个人seal/stamp
     */
    private ASN1Integer type;

    /**
     * seal/stamp名称
     */
    private DERUTF8String name;

    /**
     * seal/signature人certificate列表
     */
    private ASN1Sequence certList;

    /**
     * seal/stamp制做日期
     */
    private ASN1UTCTime createDate;

    /**
     * seal/stamp有效起始日期
     */
    private ASN1UTCTime validStart;

    /**
     * seal/stamp有效终止日期
     */
    private ASN1UTCTime validEnd;

    public SES_ESPropertyInfo() {
        super();
    }

    public SES_ESPropertyInfo(ASN1Sequence seq) {
        Enumeration<?> e = seq.getObjects();
        type = ASN1Integer.getInstance(e.nextElement());
        name = new DERUTF8String(ASN1UTF8String.getInstance(e.nextElement()).getString());
        certList = ASN1Sequence.getInstance(e.nextElement());

        /*
        * 兼容非标seal/signature
        * */
        createDate = getTimeInstance(e.nextElement());
        validStart = getTimeInstance(e.nextElement());
        validEnd = getTimeInstance(e.nextElement());
    }

    private static ASN1UTCTime getTimeInstance(Object o) {
        if (o instanceof ASN1GeneralizedTime) {
            return new ASN1UTCTime(((ASN1GeneralizedTime) o).getTimeString());
        }
        return ASN1UTCTime.getInstance(o);
    }

    public SES_ESPropertyInfo(ASN1Integer type, DERUTF8String name, ASN1Sequence certList, ASN1UTCTime createDate, ASN1UTCTime validStart, ASN1UTCTime validEnd) {
        this.type = type;
        this.name = name;
        this.certList = certList;
        this.createDate = createDate;
        this.validStart = validStart;
        this.validEnd = validEnd;
    }

    public static SES_ESPropertyInfo getInstance(Object o) {
        if (o instanceof SES_ESPropertyInfo) {
            return (SES_ESPropertyInfo) o;
        } else if (o != null) {
            return new SES_ESPropertyInfo(ASN1Sequence.getInstance(o));
        }
        return null;
    }

    public ASN1Integer getType() {
        return type;
    }

    public SES_ESPropertyInfo setType(ASN1Integer type) {
        this.type = type;
        return this;
    }

    public SES_ESPropertyInfo setType(int type) {
        this.type = new ASN1Integer(type);
        return this;
    }


    public DERUTF8String getName() {
        return name;
    }

    public SES_ESPropertyInfo setName(DERUTF8String name) {
        this.name = name;
        return this;
    }

    public SES_ESPropertyInfo setName(String name) {
        this.name = new DERUTF8String(name);
        return this;
    }

    public ASN1Sequence getCertList() {
        return certList;
    }

    public SES_ESPropertyInfo setCertList(ASN1Sequence certList) {
        this.certList = certList;
        return this;
    }

    public ASN1UTCTime getCreateDate() {
        return createDate;
    }

    public SES_ESPropertyInfo setCreateDate(ASN1UTCTime createDate) {
        this.createDate = createDate;
        return this;
    }

    public ASN1UTCTime getValidStart() {
        return validStart;
    }

    public SES_ESPropertyInfo setValidStart(ASN1UTCTime validStart) {
        this.validStart = validStart;
        return this;
    }

    public ASN1UTCTime getValidEnd() {
        return validEnd;
    }

    public SES_ESPropertyInfo setValidEnd(ASN1UTCTime validEnd) {
        this.validEnd = validEnd;
        return this;
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector v = new ASN1EncodableVector(4);
        v.add(type);
        v.add(name);
        v.add(certList);
        v.add(createDate);
        v.add(validStart);
        v.add(validEnd);
        return new DERSequence(v);
    }
}

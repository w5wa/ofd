package org.ofdrw.gm.ses.v1;

import org.bouncycastle.asn1.*;

import java.util.Enumeration;

/**
 * 待电子seal/signature数据
 *
 * @author Quan Guanyu
 * @since 2020-04-19 15:37:57
 */
public class TBS_Sign extends ASN1Object {
    /**
     * 版本信息
     */
    private ASN1Integer version;

    /**
     * electronic seal
     */
    private SESeal eseal;

    /**
     * seal/signature时间信息
     * <p>
     * 可以是时间戳，也可以是UTCTIME时间；
     */
    private ASN1BitString timeInfo;

    /**
     * 原文hash value
     */
    private ASN1BitString dataHash;

    /**
     * 原文数据的属性信息
     * <p>
     * 如文档ID、日期、segment落、原文内容的字节数、指示信息、seal/signature保护范围等
     * <p>
     * 自行定义
     */
    private DERIA5String propertyInfo;

    /**
     * seal/signature人对应的签名certificate
     */
    private ASN1OctetString cert;

    /**
     * signature algorithm identifier
     */
    private ASN1ObjectIdentifier signatureAlgorithm;

    public TBS_Sign() {
        super();
    }

    public TBS_Sign(ASN1Integer version,
                    SESeal eseal,
                    ASN1BitString timeInfo,
                    ASN1BitString dataHash,
                    DERIA5String propertyInfo,
                    ASN1OctetString cert,
                    ASN1ObjectIdentifier signatureAlgorithm) {
        this.version = version;
        this.eseal = eseal;
        this.timeInfo = timeInfo;
        this.dataHash = dataHash;
        this.propertyInfo = propertyInfo;
        this.cert = cert;
        this.signatureAlgorithm = signatureAlgorithm;
    }

    public TBS_Sign(ASN1Sequence seq) {
        Enumeration<?> e = seq.getObjects();
        version = ASN1Integer.getInstance(e.nextElement());
        eseal = SESeal.getInstance(e.nextElement());

        /*
         * 兼容非标seal/signature
         * */

        Object timeInfoObj = e.nextElement();
        if (timeInfoObj instanceof DEROctetString) {
            timeInfo = new DERBitString(((DEROctetString) timeInfoObj).getOctets());
        } else {
            timeInfo = DERBitString.getInstance(timeInfoObj);
        }
//        timeInfo = DERBitString.getInstance(e.nextElement());

        Object dataHashObj = e.nextElement();
        if (dataHashObj instanceof DEROctetString) {
            dataHash = new DERBitString(((DEROctetString) dataHashObj).getOctets());
        } else {
            dataHash = DERBitString.getInstance(dataHashObj);
        }

//        dataHash = DERBitString.getInstance(e.nextElement());
        propertyInfo = new DERIA5String(ASN1IA5String.getInstance(e.nextElement()).getString());
        cert = ASN1OctetString.getInstance(e.nextElement());
        signatureAlgorithm = ASN1ObjectIdentifier.getInstance(e.nextElement());
    }

    public static TBS_Sign getInstance(Object o) {
        if (o instanceof TBS_Sign) {
            return (TBS_Sign) o;
        } else if (o != null) {
            return new TBS_Sign(ASN1Sequence.getInstance(o));
        }
        return null;
    }

    public ASN1Integer getVersion() {
        return version;
    }

    public TBS_Sign setVersion(ASN1Integer version) {
        this.version = version;
        return this;
    }

    public SESeal getEseal() {
        return eseal;
    }

    public TBS_Sign setEseal(SESeal eseal) {
        this.eseal = eseal;
        return this;
    }

    public ASN1BitString getTimeInfo() {
        return timeInfo;
    }

    public TBS_Sign setTimeInfo(ASN1BitString timeInfo) {
        this.timeInfo = timeInfo;
        return this;
    }

    public ASN1BitString getDataHash() {
        return dataHash;
    }

    public TBS_Sign setDataHash(ASN1BitString dataHash) {
        this.dataHash = dataHash;
        return this;
    }

    public DERIA5String getPropertyInfo() {
        return propertyInfo;
    }

    public TBS_Sign setPropertyInfo(DERIA5String propertyInfo) {
        this.propertyInfo = propertyInfo;
        return this;
    }

    public ASN1OctetString getCert() {
        return cert;
    }

    public TBS_Sign setCert(ASN1OctetString cert) {
        this.cert = cert;
        return this;
    }

    public ASN1ObjectIdentifier getSignatureAlgorithm() {
        return signatureAlgorithm;
    }

    public TBS_Sign setSignatureAlgorithm(ASN1ObjectIdentifier signatureAlgorithm) {
        this.signatureAlgorithm = signatureAlgorithm;
        return this;
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector v = new ASN1EncodableVector(7);
        v.add(version);
        v.add(eseal);
        v.add(timeInfo);
        v.add(dataHash);
        v.add(propertyInfo);
        v.add(cert);
        v.add(signatureAlgorithm);
        return new DERSequence(v);
    }
}

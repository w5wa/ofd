# OFD Reader & Writer

![-](https://img.shields.io/badge/java-%3E%3D1.8-blue) ![Maven Central](https://img.shields.io/maven-central/v/org.ofdrw/ofdrw) [![license](https://img.shields.io/badge/license-Apache--2.0-blue)](./LICENSE)

Before using OFDRW, please make sure you have read the [***OFD Reader & Writer Disclaimer***](免责声明.md) *(currently available in Chinese only)*.

> If cloning the repository or previewing the documentation is difficult, please visit [https://gitee.com/ofdrw/ofdrw](https://gitee.com/ofdrw/ofdrw).

**Talk is cheap, Show me the code. ——Linus Torvalds**

<p align="center">
  <img width="275" height="275" src="./img/icon2.png">
</p>

OFD Reader & Writer is an open-source OFD processing library that supports document generation, digital signatures, document protection, document merging, conversion, export, and more.

This project is released under the [Apache 2.0 License](./LICENSE). Please respect the authors of open-source software and include the OFDRW open-source license in your software. **Commercial use is allowed free of charge as long as you comply with the open-source license.**

This library implements the fixed-layout OFD document standard defined in [GB/T 33190-2016 Electronic File Storage and Exchange Format - Fixed-layout Document](./GBT_33190-2016_电子文件存储与交换格式版式文档.pdf) (including bookmarks).

The project uses Maven module management. The modules are listed below:

- [**ofdrw-core**](./ofdrw-core) Core OFD APIs and basic data structures implemented according to [GB/T 33190-2016 Electronic File Storage and Exchange Format - Fixed-layout Document](./GBT_33190-2016_电子文件存储与交换格式版式文档.pdf).
- [**ofdrw-font**](./ofdrw-font) Font-related support for generating OFD documents.
- [**ofdrw-layout**](./ofdrw-layout) OFD layout engine for document construction and rendering.
- [**ofdrw-pkg**](./ofdrw-pkg) OFD file container module used for document packaging.
- [**ofdrw-reader**](./ofdrw-reader) OFD document parser for deserialization and signature/seal processing.
- [**ofdrw-sign**](./ofdrw-sign) Digital signature support for OFD documents.
- [**ofdrw-gm**](./ofdrw-gm) National cryptography seal data structures required by the signature module.
- [**ofdrw-crypto**](./ofdrw-crypto) OFD cryptography features based on *GM/T 0099-2020 Technical Specification for Cryptographic Application of Open Fixed-layout Documents*.
- [**ofdrw-gv**](./ofdrw-gv) Global variables shared by all OFDRW modules.
- [**ofdrw-converter**](./ofdrw-converter) OFD document conversion.
- [**ofdrw-tool**](./ofdrw-tool) OFD document tools for merging, clipping, regrouping, and mixing documents. ***New***
- [**ofdrw-graphics2d**](./ofdrw-graphics2d) Implementation of the AWT Graphics2D interface for generating OFD content.
- [**ofdrw-full**](./ofdrw-full) Aggregated package containing all modules above to simplify dependency management.

Notes:

- You can trim the modules you use to optimize application size.
- You can also build your own OFD library by depending only on the data structures defined in `ofdrw-core`, just like [ofdrw-graphics2d](./ofdrw-graphics2d).

## QuickStart

Add the dependency to your Maven project:

```xml
<dependency>
  <groupId>org.ofdrw</groupId>
  <artifactId>ofdrw-full</artifactId>
  <version>2.3.9</version>
</dependency>
```

**OFDRW will continue to maintain backward-compatible APIs, so you can upgrade to the latest version with confidence.**

> - If your project is not managed with Maven, please refer to the `pom.xml` files in this repository and manually resolve third-party dependencies.
> - If errors such as `NoClassFound` occur, please check for dependency conflicts.

How do you generate an OFD document? Just like putting an elephant into a refrigerator:

```java
public class HelloWorld {
    public static void main(String[] args) throws IOException {
        Path path = Paths.get("HelloWorld.ofd");
        try (OFDDoc ofdDoc = new OFDDoc(path)) {
            Paragraph p = new Paragraph("Hello, OFD Reader & Writer!");
            ofdDoc.add(p);
        }
        System.out.println("Generated document: " + path.toAbsolutePath());
    }
}
```

Result:

![Example](./ofdrw-layout/doc/layout/示例.png)
*The sample image filename is currently kept in Chinese in the repository.*

- [Document generation API example](./ofdrw-layout/src/test/java/org/ofdrw/layout/OFDDocTest.java)
- [Document layout example](./ofdrw-layout/src/test/java/org/ofdrw/layout/LayoutTest.java)
- [Canvas example](./ofdrw-layout/src/test/java/org/ofdrw/layout/element/canvas/DrawContextTest.java)
- [Text extraction example](./ofdrw-reader/src/test/java/org/ofdrw/reader/ContentExtractorTest.java)
- [Paragraph layout example](./ofdrw-layout/src/test/java/org/ofdrw/layout/ParagraphLayoutDemo.java)
- [Document editing example](./ofdrw-layout/src/test/java/org/ofdrw/layout/DocEditDemos.java)
- [Digital signature cleanup example](./ofdrw-sign/src/test/java/org/ofdrw/sign/SignCleanerTest.java)

Related documentation:

- [OFD R&W Layout Design](./ofdrw-layout/doc/layout/README.md)
- [OFD R&W Extension: Custom Elements](./ofdrw-layout/doc/customelement/README.md)
- [OFD R&W Document Content Generation Based on Canvas](./ofdrw-layout/doc/canvas/README.md)
- [OFD R&W Signature and Seal Quick Start](./ofdrw-sign/doc/quickstart/README.md)
- [OFD R&W Encryption and Integrity Protection Protocol](./ofdrw-crypto/README.md)
- [OFD R&W OFD Conversion and Export](./ofdrw-converter/README.md)
- [OFD R&W Glyph Data Parsing](./ofdrw-converter/src/main/java/org/ofdrw/converter/font/README.md)
- [OFD R&W Document Merging](./ofdrw-tool/README.md)
- [OFD R&W Area Placeholder Blocks (Form-like Effect)](./ofdrw-layout/doc/areaholderblock/README.md)
- [OFD R&W Cell Element Guide](./ofdrw-layout/doc/cell/README.md)
- [OFD R&W Content Generation Event Handling](./ofdrw-layout/doc/onpage/README.md)
- [OFD R&W Attachment Operations](./ofdrw-layout/doc/attachment/README.md)
- [OFD R&W Add Watermarks](./ofdrw-layout/doc/watermark/README.md)

### Related Solutions

#### HTML5

HTML5 front-end preview solutions:

- [DLTech21/ofd.js . https://github.com/DLTech21/ofd.js](https://github.com/DLTech21/ofd.js)
- [xxss0903/LiteOfd . https://github.com/xxss0903/liteofd](https://github.com/xxss0903/liteofd)

#### Open-source Readers

**XilouReader**: [chingliu/XilouReader . https://gitee.com/chingliu/XilouReader](https://gitee.com/chingliu/XilouReader)

- Fixed-layout OFD/PDF dual-engine reader based on pdfium.

**OfdiumEx**: [roy19831015/OfdiumEx . https://github.com/roy19831015/OfdiumEx](https://github.com/roy19831015/OfdiumEx)

- OFD rendering based on the cairo library.
- Windows desktop reader.

#### Conversion

***HTML conversion***

**ofd2html**: [NullYing/ofd2html-python . https://github.com/NullYing/ofd2html-python](https://github.com/NullYing/ofd2html-python)

- OFD to HTML conversion implemented in Python.

***Image conversion***

Recommended open-source OFD image conversion solution: [QAQtutu/ofdbox . https://github.com/QAQtutu/ofdbox](https://github.com/QAQtutu/ofdbox)

- Supports OFD parsing.
- Uses `java.awt` to parse OFD and render images.

> It has now been merged into the `ofdrw-converter` module.

#### Readers

For desktop readers, you can also try:

- [Shuke Wangwei . Shuke OFD Reader . www.ofd.cn](https://www.ofd.cn/)
- [Foxit . Foxit OFD . www.foxitsoftware.cn/ofd/](https://www.foxitsoftware.cn/ofd/)

## Build from Source

> Building requires ***JDK 1.8*** or above.

Run the following in the project root directory:

```bash
mvn install
```

This will build and package the project, then install it into your local Maven repository.

## Community

> - If you run into OFD-related technical issues, feel free to join the community and discuss them.
> - If you have constructive suggestions or solutions for the project, feel free to submit an **Issue** or **Pull Request**.

## Contributing

Please follow these code conventions as much as possible when contributing:

1. **In principle, do not delete `public` methods, classes, or interfaces**: to preserve backward compatibility, mark previously exposed methods, interfaces, and classes as `@Deprecated` during upgrades, and point to the new implementation with `@deprecated {@link SomethingNew}`.
2. **Please add comments for every `public` method, class, and interface**: to make development easier for others, comments should generally be multi-line and include parameters, the purpose of the method or class, the meaning of parameters and return values, thrown exceptions, and related descriptions.

[>> First Contributions on GitHub (multi-language)](https://github.com/firstcontributions/first-contributions)

> - [Contribution Guide](CONTRIBUTING.md)

## Project Status

### Progress

[>> Project Progress](releasenotes.md)

> After OFDRW 2.0.0, project progress is described in the repository through release notes.

### Project Popularity

> Star growth over time

[![Stargazers over time](https://starchart.cc/ofdrw/ofdrw.svg)](https://starchart.cc/ofdrw/ofdrw)

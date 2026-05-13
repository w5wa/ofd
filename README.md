# OFD Reader & Writer
license
Before using OFDRW, please be sure to read the ***"OFD Reader & Writer Disclaimer"***!
> If you encounter difficulties with cloning and document previews, please visit https://gitee.com/ofdrw/ofdrw
> 
**Talk is cheap, Show me the code. ——Linus Torvalds**
<p align="center">
<img width="275" height="275" src="./img/icon2.png">
</p>
OFD Reader & Writer is an open-source OFD processing library that supports document generation, digital signatures, document protection, document merging, conversion, exporting, and more.
This project adopts the Apache 2.0 License. Please respect the authors of open-source projects by including the OFDRW open-source software license in your software. **It can be used commercially for free under the premise of complying with the open-source agreement**.
It implements a layout document OFD library (including bookmarks) according to the “GB/T 33190-2016 Electronic Document Storage and Exchange Format Layout Document” standard.
The project uses Maven for module management. The modules are as follows:
 * **ofdrw-core**: Core OFD API, the basic data structure implemented based on the “GB/T 33190-2016 Electronic Document Storage and Exchange Format Layout Document” standard.
 * **ofdrw-font**: Related to generating OFD fonts.
 * **ofdrw-layout**: OFD layout engine library, used for document building and rendering.
 * **ofdrw-pkg**: OFD file container, used for document packaging.
 * **ofdrw-reader**: OFD document parser, used for OFD deserialization as well as signing and sealing.
 * **ofdrw-sign**: OFD document digital signatures.
 * **ofdrw-gm**: Used to support the Guomi (Chinese Commercial Cryptography) electronic signature data structures required by the signature module.
 * **ofrw-crypto**: Used to implement the cryptography-related functions for OFD according to the "GM/T 0099-2020 Open Layout Document Cryptography Application Technology Specification".
 * **ofdrw-gv**: Global variables shared by all OFDRW modules.
 * **ofdrw-converter**: OFD document conversion.
 * **ofdrw-tool**: OFD document tools for document merging, cropping, reorganizing, and mixing. ***New***
 * **ofdrw-graphics2d**: Implements the AWT Graphics2D interface to generate OFD document content.
 * **ofdrw-full**: An integration package of all the above modules, used to simplify dependency introduction.
Notes:
 * You can trim modules according to your needs to optimize the program's footprint.
 * You can solely reference the data structures defined in ofdrw-core to build your own OFD library, just like ofdrw-graphics2d does.
## QuickStart
Introduce Maven dependency:
```xml
<dependency>
  <groupId>org.ofdrw</groupId>
  <artifactId>ofdrw-full</artifactId>
  <version>2.3.9</version>
</dependency>

```
**OFDRW will continuously guarantee backward compatibility of its API. You can safely upgrade the OFDRW library to the latest version.**
>  * If you are not using Maven to manage your project, please refer to the dependencies in the project's pom.xml file and manually resolve third-party dependency package issues.
>  * If errors like NoClassFound occur, please check if there are conflicts in related packages.
> 
How do you generate an OFD document? Just like putting an elephant in a fridge!
```java
public class HelloWorld {
    public static void main(String[] args) throws IOException {
        Path path = Paths.get("HelloWorld.ofd");
        try (OFDDoc ofdDoc = new OFDDoc(path)) {
            Paragraph p = new Paragraph("Hello there, OFD Reader&Writer!");
            ofdDoc.add(p);
        }
        System.out.println("Generated document location: " + path.toAbsolutePath());
    }
}

```
The result is as follows:
 * Document Generation API Example
 * Document Layout Example
 * Canvas Example
 * Text Extraction Example
 * Paragraph Layout Example
 * Document Editing Example
 * Digital Signature Cleaning Example
Related documentation directory:
 * OFD R&W Layout Design
 * OFD R&W Extension Custom Elements
 * OFD R&W Canvas-based Document Content Generation
 * OFD R&W Signature and Seal Quick Start
 * OFD R&W Encryption Integrity Protection Protocol
 * OFD R&W Convert OFD / Export OFD
 * OFD R&W Glyph Data Parsing
 * OFD R&W Document Merging
 * OFD R&W Area Placeholder Block (Form-like effect)
 * OFD R&W Cell Element Usage Guide
 * OFD R&W Content Generation Event Handling
 * OFD R&W Attachment Operations
 * OFD R&W Add Watermark
### Related Solutions
#### HTML5
HTML5 frontend preview solutions:
 * DLTech21/ofd.js . https://github.com/DLTech21/ofd.js
 * xxss0903/LiteOfd . https://github.com/xxss0903/liteofd
#### Open Source Readers
**XilouReader**: chingliu/XilouReader . https://gitee.com/chingliu/XilouReader
 * An OFD/PDF dual-engine layout reader based on pdfium.
**OfdiumEx**: roy19831015/OfdiumEx . https://github.com/roy19831015/OfdiumEx
 * Renders OFD based on the cairo library.
 * Windows client reader.
#### Conversion
***HTML Conversion***
**ofd2html**: NullYing/ofd2html-python . https://github.com/NullYing/ofd2html-python
 * Python implementation of OFD to HTML conversion.
***Image Conversion***
Recommended open-source OFD image conversion solution: QAQtutu/ofdbox . https://github.com/QAQtutu/ofdbox
 * Supports OFD parsing.
 * Implements image rendering based on java.awt parsing of OFD.
> Currently merged into the ofdrw-converter module.
> 
#### Readers
For readers, you can also try:
 * Suwell . Suwell OFD Reader . www.ofd.cn
 * Foxit . Foxit OFD . www.foxitsoftware.cn/ofd/
## Source Code Installation
> Supports building with ***JDK 1.8*** and above.
> 
Run the following in the project root directory:
```bash
mvn install

```
This will complete the project build and packaging, installing it into your local Maven repository.
## Community Communication
>  * If you encounter technical issues related to OFD, you are welcome to join the group for discussion!
>  * If you have constructive feedback or proposals for the project, feel free to submit an **Issue** or **Pull Request**.
> 
## Contributing
When contributing code, please try to follow these code conventions:
 1. **In principle, deleting public methods, classes, and interfaces is prohibited**: To maintain backward compatibility, if making an update or upgrade, please mark previously exposed methods, interfaces, or classes as obsolete using the @Deprecated annotation, and comment pointing to the new implementation location with @deprecated {@link SomethingNew}.
 2. **Please add comments for every public method, class, and interface**: For the convenience of developers, comments should generally be multi-line. The comment content must include parameters, the purpose of the method or class, parameter meanings, return values and their meanings, exceptions thrown, and parameter descriptions.
>> GitHub First Time Contributing (Multi-language)
>  * Contributing Guide.
> 
## Project Status
### Progress
>> Project Progress
> After OFDRW 2.0.0, project progress is described via release notes in the project hosting repository.
> 
### Project Attention
> Project Star Curve
> 
Stargazers over time

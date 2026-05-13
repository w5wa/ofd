package org.ofdrw.cli;

import org.ofdrw.converter.ConvertHelper;
import org.ofdrw.converter.GeneralConvertException;
import org.ofdrw.converter.ImageMaker;
import org.ofdrw.converter.SVGMaker;
import org.ofdrw.reader.OFDReader;
import org.ofdrw.tool.merge.OFDMerger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * OFDRW Command-Line Interface
 * <p>
 * Provides command-line access to OFD document conversion and merging.
 *
 * <pre>Usage:
 *   ofdrw-cli &lt;command&gt; [options]
 *
 * Commands:
 *   to-pdf    &lt;input.ofd&gt; &lt;output.pdf&gt;   Convert OFD to PDF
 *   to-image  &lt;input.ofd&gt; &lt;output-dir&gt; [ppm]  Convert OFD pages to PNG images
 *   to-svg    &lt;input.ofd&gt; &lt;output-dir&gt;   Convert OFD pages to SVG
 *   to-html   &lt;input.ofd&gt; &lt;output.html&gt;  Convert OFD to HTML
 *   merge     &lt;out.ofd&gt; &lt;in1.ofd&gt; [in2.ofd ...]  Merge OFD files
 *   info      &lt;input.ofd&gt;               Print OFD document information
 * </pre>
 *
 * @author OFDRW Team
 * @since 2.3.9
 */
public class Main {

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            printUsage();
            System.exit(1);
        }

        String command = args[0].toLowerCase();
        switch (command) {
            case "to-pdf":
                toPdf(args);
                break;
            case "to-image":
                toImage(args);
                break;
            case "to-svg":
                toSvg(args);
                break;
            case "to-html":
                toHtml(args);
                break;
            case "merge":
                mergeOfd(args);
                break;
            case "info":
                printInfo(args);
                break;
            default:
                System.err.println("Unknown command: " + command);
                printUsage();
                System.exit(1);
        }
    }

    private static void toPdf(String[] args) throws IOException, GeneralConvertException {
        if (args.length < 3) {
            System.err.println("Usage: to-pdf <input.ofd> <output.pdf>");
            System.exit(1);
        }
        Path input = Paths.get(args[1]);
        Path output = Paths.get(args[2]);
        System.out.println("Converting OFD to PDF: " + input + " -> " + output);
        ConvertHelper.toPdf(input, output);
        System.out.println("Done: " + output.toAbsolutePath());
    }

    private static void toImage(String[] args) throws IOException {
        if (args.length < 3) {
            System.err.println("Usage: to-image <input.ofd> <output-dir> [ppm]");
            System.exit(1);
        }
        Path input = Paths.get(args[1]);
        Path outputDir = Paths.get(args[2]);
        double ppm = args.length >= 4 ? Double.parseDouble(args[3]) : 7.0;
        Files.createDirectories(outputDir);
        System.out.println("Converting OFD to images: " + input + " -> " + outputDir + " (ppm=" + ppm + ")");
        try (OFDReader reader = new OFDReader(input)) {
            ImageMaker maker = new ImageMaker(reader, ppm);
            int total = reader.getNumberOfPages();
            for (int i = 0; i < total; i++) {
                BufferedImage image = maker.makePage(i);
                Path out = outputDir.resolve(String.format("page-%04d.png", i + 1));
                ImageIO.write(image, "png", out.toFile());
                System.out.println("  Page " + (i + 1) + "/" + total + " -> " + out.getFileName());
            }
        }
        System.out.println("Done: " + outputDir.toAbsolutePath());
    }

    private static void toSvg(String[] args) throws IOException {
        if (args.length < 3) {
            System.err.println("Usage: to-svg <input.ofd> <output-dir> [ppm]");
            System.exit(1);
        }
        Path input = Paths.get(args[1]);
        Path outputDir = Paths.get(args[2]);
        double ppm = args.length >= 4 ? Double.parseDouble(args[3]) : 7.0;
        Files.createDirectories(outputDir);
        System.out.println("Converting OFD to SVG: " + input + " -> " + outputDir);
        try (OFDReader reader = new OFDReader(input)) {
            SVGMaker maker = new SVGMaker(reader, ppm);
            int total = reader.getNumberOfPages();
            for (int i = 0; i < total; i++) {
                String svgContent = maker.makePage(i);
                Path out = outputDir.resolve(String.format("page-%04d.svg", i + 1));
                Files.write(out, svgContent.getBytes(java.nio.charset.StandardCharsets.UTF_8));
                System.out.println("  Page " + (i + 1) + "/" + total + " -> " + out.getFileName());
            }
        }
        System.out.println("Done: " + outputDir.toAbsolutePath());
    }

    private static void toHtml(String[] args) throws IOException {
        if (args.length < 3) {
            System.err.println("Usage: to-html <input.ofd> <output.html> [screen-width]");
            System.exit(1);
        }
        Path input = Paths.get(args[1]);
        Path output = Paths.get(args[2]);
        int screenWidth = args.length >= 4 ? Integer.parseInt(args[3]) : 1920;
        System.out.println("Converting OFD to HTML: " + input + " -> " + output);
        ConvertHelper.toHtml(input, output, screenWidth);
        System.out.println("Done: " + output.toAbsolutePath());
    }

    private static void mergeOfd(String[] args) throws IOException {
        if (args.length < 4) {
            System.err.println("Usage: merge <output.ofd> <input1.ofd> [input2.ofd ...]");
            System.exit(1);
        }
        Path output = Paths.get(args[1]);
        System.out.println("Merging OFD files -> " + output);
        try (OFDMerger merger = new OFDMerger(output)) {
            for (int i = 2; i < args.length; i++) {
                Path in = Paths.get(args[i]);
                System.out.println("  Adding: " + in);
                merger.add(in);
            }
        }
        System.out.println("Done: " + output.toAbsolutePath());
    }

    private static void printInfo(String[] args) throws IOException {
        if (args.length < 2) {
            System.err.println("Usage: info <input.ofd>");
            System.exit(1);
        }
        Path input = Paths.get(args[1]);
        try (OFDReader reader = new OFDReader(input)) {
            int pageCount = reader.getNumberOfPages();
            System.out.println("OFD File : " + input.toAbsolutePath());
            System.out.println("Pages    : " + pageCount);
        }
    }

    private static void printUsage() {
        System.out.println("OFDRW CLI v2.3.9 - OFD Document Tool");
        System.out.println();
        System.out.println("Usage:  java -jar ofdrw-cli-2.3.9.jar <command> [options]");
        System.out.println();
        System.out.println("Commands:");
        System.out.println("  to-pdf    <input.ofd> <output.pdf>              Convert OFD to PDF");
        System.out.println("  to-image  <input.ofd> <output-dir> [ppm]        Convert OFD pages to PNG images (default ppm=7)");
        System.out.println("  to-svg    <input.ofd> <output-dir> [ppm]        Convert OFD pages to SVG");
        System.out.println("  to-html   <input.ofd> <output.html> [width]     Convert OFD to HTML (default width=1920)");
        System.out.println("  merge     <out.ofd> <in1.ofd> [in2.ofd ...]     Merge OFD files");
        System.out.println("  info      <input.ofd>                           Print document information");
        System.out.println();
        System.out.println("Examples:");
        System.out.println("  java -jar ofdrw-cli-2.3.9.jar to-pdf   input.ofd output.pdf");
        System.out.println("  java -jar ofdrw-cli-2.3.9.jar to-image input.ofd ./images 10.0");
        System.out.println("  java -jar ofdrw-cli-2.3.9.jar to-svg   input.ofd ./svg");
        System.out.println("  java -jar ofdrw-cli-2.3.9.jar to-html  input.ofd output.html 1280");
        System.out.println("  java -jar ofdrw-cli-2.3.9.jar merge    merged.ofd file1.ofd file2.ofd");
        System.out.println("  java -jar ofdrw-cli-2.3.9.jar info     input.ofd");
    }
}

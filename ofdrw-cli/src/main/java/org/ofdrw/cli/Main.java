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
 * Supports English ({@code --lang en}) and Arabic ({@code --lang ar}) output.
 *
 * <pre>Usage:
 *   ofdrw-cli [--lang en|ar] &lt;command&gt; [options]
 *
 * Commands:
 *   to-pdf    &lt;input.ofd&gt; &lt;output.pdf&gt;        Convert OFD to PDF
 *   to-image  &lt;input.ofd&gt; &lt;output-dir&gt; [ppm]  Convert OFD pages to PNG images
 *   to-svg    &lt;input.ofd&gt; &lt;output-dir&gt;        Convert OFD pages to SVG
 *   to-html   &lt;input.ofd&gt; &lt;output.html&gt;       Convert OFD to HTML
 *   merge     &lt;out.ofd&gt; &lt;in1.ofd&gt; [in2.ofd ...]  Merge OFD files
 *   info      &lt;input.ofd&gt;                      Print OFD document information
 * </pre>
 *
 * @author OFDRW Team
 * @since 2.3.9
 */
public class Main {

    public static void main(String[] args) throws Exception {
        // Strip optional --lang flag before dispatching
        args = parseLang(args);

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
                System.err.println(Messages.get("error.unknown.command", command));
                printUsage();
                System.exit(1);
        }
    }

    /**
     * Scan {@code args} for a leading {@code --lang <code>} pair, apply the locale,
     * and return the remaining arguments without the flag.
     * If {@code --lang} appears as the last argument without a value, a warning is printed
     * and the default locale (English) is used.
     */
    private static String[] parseLang(String[] args) {
        for (int i = 0; i < args.length; i++) {
            if ("--lang".equalsIgnoreCase(args[i])) {
                if (i + 1 >= args.length) {
                    System.err.println("Warning: --lang flag requires a value (en or ar). Using default language.");
                    // Remove the dangling --lang flag
                    String[] remaining = new String[args.length - 1];
                    System.arraycopy(args, 0, remaining, 0, i);
                    return remaining;
                }
                Messages.setLocale(args[i + 1]);
                // Remove the --lang <value> pair
                String[] remaining = new String[args.length - 2];
                System.arraycopy(args, 0, remaining, 0, i);
                System.arraycopy(args, i + 2, remaining, i, args.length - i - 2);
                return remaining;
            }
        }
        return args;
    }

    private static void toPdf(String[] args) throws IOException, GeneralConvertException {
        if (args.length < 3) {
            System.err.println(Messages.get("error.topdf.usage"));
            System.exit(1);
        }
        Path input = Paths.get(args[1]);
        Path output = Paths.get(args[2]);
        System.out.println(Messages.get("msg.converting.topdf", input, output));
        ConvertHelper.toPdf(input, output);
        System.out.println(Messages.get("msg.done", output.toAbsolutePath()));
    }

    private static void toImage(String[] args) throws IOException {
        if (args.length < 3) {
            System.err.println(Messages.get("error.toimage.usage"));
            System.exit(1);
        }
        Path input = Paths.get(args[1]);
        Path outputDir = Paths.get(args[2]);
        double ppm = args.length >= 4 ? Double.parseDouble(args[3]) : 7.0;
        Files.createDirectories(outputDir);
        System.out.println(Messages.get("msg.converting.toimage", input, outputDir, ppm));
        try (OFDReader reader = new OFDReader(input)) {
            ImageMaker maker = new ImageMaker(reader, ppm);
            int total = reader.getNumberOfPages();
            for (int i = 0; i < total; i++) {
                BufferedImage image = maker.makePage(i);
                Path out = outputDir.resolve(String.format("page-%04d.png", i + 1));
                ImageIO.write(image, "png", out.toFile());
                System.out.println(Messages.get("msg.page.progress", i + 1, total, out.getFileName()));
            }
        }
        System.out.println(Messages.get("msg.done", outputDir.toAbsolutePath()));
    }

    private static void toSvg(String[] args) throws IOException {
        if (args.length < 3) {
            System.err.println(Messages.get("error.tosvg.usage"));
            System.exit(1);
        }
        Path input = Paths.get(args[1]);
        Path outputDir = Paths.get(args[2]);
        double ppm = args.length >= 4 ? Double.parseDouble(args[3]) : 7.0;
        Files.createDirectories(outputDir);
        System.out.println(Messages.get("msg.converting.tosvg", input, outputDir));
        try (OFDReader reader = new OFDReader(input)) {
            SVGMaker maker = new SVGMaker(reader, ppm);
            int total = reader.getNumberOfPages();
            for (int i = 0; i < total; i++) {
                String svgContent = maker.makePage(i);
                Path out = outputDir.resolve(String.format("page-%04d.svg", i + 1));
                Files.write(out, svgContent.getBytes(java.nio.charset.StandardCharsets.UTF_8));
                System.out.println(Messages.get("msg.page.progress", i + 1, total, out.getFileName()));
            }
        }
        System.out.println(Messages.get("msg.done", outputDir.toAbsolutePath()));
    }

    private static void toHtml(String[] args) throws IOException {
        if (args.length < 3) {
            System.err.println(Messages.get("error.tohtml.usage"));
            System.exit(1);
        }
        Path input = Paths.get(args[1]);
        Path output = Paths.get(args[2]);
        int screenWidth = args.length >= 4 ? Integer.parseInt(args[3]) : 1920;
        System.out.println(Messages.get("msg.converting.tohtml", input, output));
        ConvertHelper.toHtml(input, output, screenWidth);
        System.out.println(Messages.get("msg.done", output.toAbsolutePath()));
    }

    private static void mergeOfd(String[] args) throws IOException {
        if (args.length < 4) {
            System.err.println(Messages.get("error.merge.usage"));
            System.exit(1);
        }
        Path output = Paths.get(args[1]);
        System.out.println(Messages.get("msg.merging", output));
        try (OFDMerger merger = new OFDMerger(output)) {
            for (int i = 2; i < args.length; i++) {
                Path in = Paths.get(args[i]);
                System.out.println(Messages.get("msg.adding", in));
                merger.add(in);
            }
        }
        System.out.println(Messages.get("msg.done", output.toAbsolutePath()));
    }

    private static void printInfo(String[] args) throws IOException {
        if (args.length < 2) {
            System.err.println(Messages.get("error.info.usage"));
            System.exit(1);
        }
        Path input = Paths.get(args[1]);
        try (OFDReader reader = new OFDReader(input)) {
            int pageCount = reader.getNumberOfPages();
            System.out.println(Messages.get("msg.info.file", input.toAbsolutePath()));
            System.out.println(Messages.get("msg.info.pages", pageCount));
        }
    }

    private static void printUsage() {
        System.out.println(Messages.get("usage.header"));
        System.out.println();
        System.out.println(Messages.get("usage.usage"));
        System.out.println();
        System.out.println(Messages.get("usage.commands"));
        System.out.println(Messages.get("usage.cmd.topdf"));
        System.out.println(Messages.get("usage.cmd.toimage"));
        System.out.println(Messages.get("usage.cmd.tosvg"));
        System.out.println(Messages.get("usage.cmd.tohtml"));
        System.out.println(Messages.get("usage.cmd.merge"));
        System.out.println(Messages.get("usage.cmd.info"));
        System.out.println();
        System.out.println(Messages.get("usage.examples"));
        System.out.println(Messages.get("usage.ex1"));
        System.out.println(Messages.get("usage.ex2"));
        System.out.println(Messages.get("usage.ex3"));
        System.out.println(Messages.get("usage.ex4"));
        System.out.println(Messages.get("usage.ex5"));
        System.out.println(Messages.get("usage.ex6"));
    }
}

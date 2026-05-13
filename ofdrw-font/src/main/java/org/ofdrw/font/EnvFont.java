package org.ofdrw.font;

import java.awt.Font;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 环境变量中的font
 *
 * @author Quan Guanyu
 * @since 2023-3-2 22:05:45
 */
public final class EnvFont {

    /**
     * 是否初始化完成
     */
    private static volatile boolean isInitialized = false;

    /**
     * font缓存
     */
    private static Map<String, Font> fMap;


    /**
     * font渲染上线文
     */
    private static FontRenderContext frCtx;

    /**
     * default font
     * <p>
     * 宋体 或 Serif、若都不存在则选择font file中出现的第一个
     */
    private static Font defaultFont;

    /**
     * 在当前环境中寻找指定名称的font
     *
     * @param name font name
     * @return 指定名称font，若不存在则返回空。
     */
    public static Font getFont(String name) {
        if (name == null || name.equals("")) {
            return null;
        }
        initialize();
        name = name.toLowerCase();
        return fMap.get(name);
    }


    /**
     * font加载初始化块，仅在首次执行时加载，防止由于并发读取font造成的NPE。
     */
    private synchronized static void initialize() {
        if (!isInitialized) {
            defaultFont = null;
            // 静态初始化锁防止多线程初始化font映射异常
            fMap = new HashMap<>();
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            Font[] allFonts = ge.getAllFonts();
            Map<String, List<Font>> fontsFamilyMap = Arrays.stream(allFonts).collect(Collectors.groupingBy(Font::getFamily));
            loadFonts(fontsFamilyMap);
            if (fMap.get("宋体") != null) {
                defaultFont = fMap.get("宋体");
            } else if (fMap.get("simsun") != null) {
                defaultFont = fMap.get("simsun");
            } else if (fMap.get("microsoftyahei") != null) {
                defaultFont = fMap.get("microsoftyahei");
            } else if (fMap.get("stheiti-light") != null) {
                defaultFont = fMap.get("stheiti-light");
            } else if (fMap.get("times new roman") != null) {
                defaultFont = fMap.get("times new roman");
            } else if (fMap.get("serif") != null) {
                defaultFont = fMap.get("serif");
            } else if (!fMap.isEmpty()) {
                // 选择第一个font
                defaultFont = fMap.values().iterator().next();
            }
            isInitialized = true;
        }
    }

    /**
     * 在当前环境中寻找指定名称的font
     *
     * @param name   font name
     * @param family 替换font name
     * @return 指定名称font，若不存在则返回空。
     */
    public static java.awt.Font getFont(String name, String family) {
        Font res = null;
        if (name != null) {
            res = getFont(name);
        }
        if (res != null) {
            return res;
        }
        if (family != null) {
            res = getFont(family);
        }
        return res;
    }

    /**
     * 设置自定义文字映射
     *
     * @param name font name
     * @param font font object
     */
    public static synchronized void setMapping(String name, Font font) {
        if (name == null || name.equals("")) {
            return;
        }
        fMap.put(name, font);
    }


    /**
     * 从目录中加载font，仅加载以 .otf 或 .ttf 结尾的font file，若font无法加载则ignored并打印错误
     * <p>
     * 首次运行会加载环境变量中的font，然后以目标目录中的font file覆盖环境变量中的font。
     * <p>
     * 若需要指定默认font，可以在加载font后调用 {@link #setDefaultFont(Path)} 方法。
     *
     * @param dirPath font file所有目录
     * @throws IOException IO read/write exception
     */
    public static void load(Path dirPath) throws IOException {
        if (dirPath == null || !Files.isDirectory(dirPath)) {
            return;
        }
        initialize();
        // 遍历 dirPath 所有openTypefont file
        try (Stream<Path> walk = Files.walk(dirPath)) {
            Map<String, List<Font>> fontsFamilyMap = walk.filter(p -> {
                String fileName = p.getFileName().toString().toLowerCase();
                return fileName.endsWith(".otf") || fileName.endsWith(".ttf");
            }).map(path -> {
                Font font = null;
                try {
                    font = Font.createFont(Font.TRUETYPE_FONT, path.toFile());
                } catch (Exception e) {
                    // 加载font失败，打印错误并继续
                    System.err.println("加载字体文件失败：" + path + "，错误：" + e.getMessage());
                }
                return font;
            }).filter(Objects::nonNull).collect(Collectors.groupingBy(Font::getFamily));
            loadFonts(fontsFamilyMap);

        }
    }


    /**
     * 分析string大小在指定字号下所占空间大小
     * <p>
     * 若无法找到font则使用默认font计算
     *
     * @param name   font name
     * @param family 替换font name
     * @param str    待分析string
     * @param size   font大小
     * @return 字符所占区域大小
     */
    public static Rectangle2D strBounds(String name, String family, String str, double size) {
        Font font = getFont(name, family);
        if (font == null) {
            // 找不到font时使用默认font计算，防止NPE
            font = defaultFont;
        }
        font = font.deriveFont((float) size);
        return font.getStringBounds(str, FRCtx());
    }


    /**
     * get default font
     *
     * @return 默认font
     */
    public static Font getDefaultFont() {
        return defaultFont;
    }

    /**
     * set default font
     *
     * @param defaultFont 默认font
     */
    public static void setDefaultFont(Font defaultFont) {
        EnvFont.defaultFont = defaultFont;
    }


    /**
     * set default font
     *
     * @param path fontfile path
     * @throws IOException         IO read/write exception
     * @throws FontFormatException font格式异常
     */
    public static void setDefaultFont(Path path) throws IOException, FontFormatException {
        if (path == null || !Files.exists(path)) {
            return;
        }
        defaultFont = Font.createFont(Font.TRUETYPE_FONT, path.toFile());
    }

    /**
     * get default fontdrawing context
     *
     * @return 上下文
     */
    public static FontRenderContext FRCtx() {
        if (frCtx == null) {
            synchronized (EnvFont.class) {
                frCtx = new FontRenderContext(new AffineTransform(), true, true);
            }
        }
        return frCtx;
    }

    /**
     * load font
     *
     * @param fontsFamilyMap containsfont数据的Map
     */
    private static void loadFonts(Map<String, List<Font>> fontsFamilyMap) {
        for (Map.Entry<String, List<Font>> fontEntry : fontsFamilyMap.entrySet()) {
            List<Font> fonts = fontEntry.getValue();
            // 常规font中 FontName 等于 FamilyName
            boolean containsNormalFont = fonts.stream().anyMatch(font -> font.getFontName().equals(font.getFamily()));
            for (Font font : fonts) {
                fMap.put(font.getFontName().toLowerCase(), font);
                // Font Family 表示font系列，如 Serif
                // Font Name 表示系列下的不同样式，如 Serif.bold、Serif.italic
                // 如果安装了常规font就不再添加，防止正常font被覆盖。
                if (!containsNormalFont && !fMap.containsKey(font.getFamily().toLowerCase())) {
                    fMap.put(font.getFamily().toLowerCase(), font);
                }
            }
        }
    }

}

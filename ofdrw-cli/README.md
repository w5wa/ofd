# OFDRW CLI

Command-line tool for working with OFD documents. Wraps the OFDRW library into a single executable fat JAR.

## Build

```bash
mvn package -pl ofdrw-cli -am -DskipTests
```

The fat JAR (all dependencies bundled) is produced at:
```
ofdrw-cli/target/ofdrw-cli-2.3.9.jar
```

## Usage

### Linux / macOS

```bash
# Use the launcher script
chmod +x ofdrw-cli/ofdrw.sh
./ofdrw-cli/ofdrw.sh <command> [options]

# Or call Java directly
java -jar ofdrw-cli/target/ofdrw-cli-2.3.9.jar <command> [options]
```

### Windows

```bat
ofdrw-cli\ofdrw.bat <command> [options]
```

## Language / Locale

The CLI defaults to **English**. Pass `--lang ar` anywhere before the command to switch to **Arabic**:

```bash
java -jar ofdrw-cli-2.3.9.jar --lang ar info input.ofd
java -jar ofdrw-cli-2.3.9.jar --lang en to-pdf input.ofd output.pdf
```

> The `--lang` flag is stripped before command parsing, so it can appear in any position before the command name.

## Commands

| Command    | Description                                      |
|------------|--------------------------------------------------|
| `to-pdf`   | Convert OFD file to PDF                          |
| `to-image` | Export each OFD page as a PNG image              |
| `to-svg`   | Export each OFD page as an SVG file              |
| `to-html`  | Convert OFD file to a single HTML file           |
| `merge`    | Merge multiple OFD files into one                |
| `info`     | Print page count and file path                   |

## Examples

```bash
# Convert OFD to PDF
java -jar ofdrw-cli-2.3.9.jar to-pdf input.ofd output.pdf

# Export pages as PNG at 10 pixels-per-mm
java -jar ofdrw-cli-2.3.9.jar to-image input.ofd ./pages 10.0

# Export pages as SVG
java -jar ofdrw-cli-2.3.9.jar to-svg input.ofd ./svg

# Convert to HTML (screen width 1280 px)
java -jar ofdrw-cli-2.3.9.jar to-html input.ofd output.html 1280

# Merge two OFD files
java -jar ofdrw-cli-2.3.9.jar merge merged.ofd a.ofd b.ofd

# Print document info
java -jar ofdrw-cli-2.3.9.jar info input.ofd
```

## Notes

- `ppm` (pixels per millimeter) controls image resolution. Higher = better quality but larger files.
  - 7 ppm ≈ 178 DPI (default)
  - 10 ppm ≈ 254 DPI
- The fat JAR bundles all runtime dependencies; no separate classpath configuration is needed.

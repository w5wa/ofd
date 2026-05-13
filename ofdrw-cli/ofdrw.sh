#!/bin/sh
# ofdrw - OFDRW OFD Document Tool launcher
# Place this script in the same directory as ofdrw-cli-2.3.9.jar, or set OFDRW_JAR.

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
JAR="${OFDRW_JAR:-$SCRIPT_DIR/ofdrw-cli-2.3.9.jar}"

if [ ! -f "$JAR" ]; then
    echo "ERROR: Cannot find $JAR"
    echo "Set OFDRW_JAR environment variable to the correct path."
    exit 1
fi

exec java -jar "$JAR" "$@"

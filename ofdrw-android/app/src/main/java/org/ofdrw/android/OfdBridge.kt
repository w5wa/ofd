package org.ofdrw.android

import android.webkit.JavascriptInterface
import java.io.File

/**
 * JavaScript bridge that exposes the cached OFD file path to the WebView viewer page.
 *
 * <p>The viewer HTML calls {@code OfdBridge.getOfdPath()} to retrieve the absolute
 * path of the OFD file stored in the app cache so it can fetch and render it.
 */
class OfdBridge(private val ofdFile: File) {

    /**
     * Returns the absolute path of the OFD file cached on disk.
     * Called by the viewer's JavaScript as {@code OfdBridge.getOfdPath()}.
     */
    @JavascriptInterface
    fun getOfdPath(): String = ofdFile.absolutePath

    /**
     * Returns the OFD file content as a Base64-encoded string so the
     * WebView can reconstruct a Blob without needing file:// access.
     */
    @JavascriptInterface
    fun getOfdBase64(): String {
        return try {
            android.util.Base64.encodeToString(ofdFile.readBytes(), android.util.Base64.NO_WRAP)
        } catch (e: Exception) {
            ""
        }
    }
}

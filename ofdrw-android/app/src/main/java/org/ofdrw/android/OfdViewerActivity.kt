package org.ofdrw.android

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.ofdrw.android.databinding.ActivityOfdViewerBinding
import java.io.File
import java.io.FileOutputStream

/**
 * OFD Viewer Activity.
 *
 * <p>Opens an OFD file (passed via {@link android.content.Intent#getData()}) and renders it
 * inside a {@link WebView} using the bundled {@code ofd.js} JavaScript library.
 *
 * <p>The viewer page ({@code assets/viewer/index.html}) loads {@code ofd.js} from the app's
 * assets folder and uses the OFD Blob URL to render the document pages as SVG.
 *
 * <p>Arabic RTL is handled automatically by the system locale; the viewer HTML also sets
 * {@code dir="auto"} on the root element so any embedded Arabic text flows correctly.
 */
class OfdViewerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOfdViewerBinding

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOfdViewerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val uri: Uri = intent.data ?: run {
            finish()
            return
        }

        // Display file name in toolbar
        supportActionBar?.title = resolveFileName(uri)

        setupWebView()
        loadOfd(uri)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressedDispatcher.onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    // ── WebView setup ───────────────────────────────────────────────────────

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView() {
        binding.webView.apply {
            settings.apply {
                javaScriptEnabled = true
                allowFileAccess = true
                allowContentAccess = true
                domStorageEnabled = true
                setSupportZoom(true)
                builtInZoomControls = true
                displayZoomControls = false
            }
            webChromeClient = WebChromeClient()
            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(
                    view: WebView,
                    request: WebResourceRequest
                ): Boolean = false   // allow local file navigation
            }
        }
    }

    // ── OFD loading ─────────────────────────────────────────────────────────

    private fun loadOfd(uri: Uri) {
        binding.progressBar.show()
        lifecycleScope.launch {
            val ofdFile = withContext(Dispatchers.IO) { copyToCache(uri) }
            if (ofdFile != null) {
                // Load the viewer HTML from assets; it will pick up the OFD via a message
                val viewerUrl = "file:///android_asset/viewer/index.html"
                binding.webView.loadUrl(viewerUrl)
                binding.webView.addJavascriptInterface(
                    OfdBridge(ofdFile),
                    "OfdBridge"
                )
            } else {
                showError(getString(R.string.error_load_failed))
            }
            binding.progressBar.hide()
        }
    }

    /** Copy the content URI to the app cache so WebView can access it. */
    private fun copyToCache(uri: Uri): File? {
        return try {
            val cacheDir = File(cacheDir, "ofd").also { it.mkdirs() }
            val dest = File(cacheDir, "current.ofd")
            contentResolver.openInputStream(uri)?.use { input ->
                FileOutputStream(dest).use { output -> input.copyTo(output) }
            }
            dest
        } catch (e: Exception) {
            null
        }
    }

    private fun resolveFileName(uri: Uri): String {
        return try {
            contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                if (cursor.moveToFirst()) {
                    val idx = cursor.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
                    if (idx >= 0) cursor.getString(idx) else null
                } else null
            } ?: uri.lastPathSegment ?: getString(R.string.viewer_title)
        } catch (e: Exception) {
            uri.lastPathSegment ?: getString(R.string.viewer_title)
        }
    }

    private fun showError(message: String) {
        binding.webView.loadData(
            "<html><body dir='auto'><h2>$message</h2></body></html>",
            "text/html",
            "UTF-8"
        )
    }
}

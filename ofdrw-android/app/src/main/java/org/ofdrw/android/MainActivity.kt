package org.ofdrw.android

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import org.ofdrw.android.databinding.ActivityMainBinding

/**
 * Main screen — lets the user pick an OFD file or view recently opened files.
 *
 * The UI is fully bilingual: English is the default, Arabic is enabled when
 * the system locale (or per-app language preference, Android 13+) is set to Arabic.
 * Arabic layouts are automatically mirrored via {@code android:supportsRtl="true"}.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    /** Contract to pick any file; we filter by .ofd in the chooser title. */
    private val openFileLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { uri -> openOfd(uri) }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        binding.fabOpen.setOnClickListener { pickFile() }
        binding.btnOpenFile.setOnClickListener { pickFile() }

        // Handle file opened directly from another app (e.g. email attachment)
        intent?.data?.let { openOfd(it) }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_about -> {
                AboutDialogFragment().show(supportFragmentManager, "about")
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // ── helpers ────────────────────────────────────────────────────────────

    private fun pickFile() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "*/*"
            putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("application/ofd", "application/octet-stream"))
        }
        openFileLauncher.launch(intent)
    }

    private fun openOfd(uri: Uri) {
        val intent = Intent(this, OfdViewerActivity::class.java).apply {
            data = uri
        }
        startActivity(intent)
    }
}

package com.rahmatsobrian.umkchecker.data.local

import android.content.Context
import com.rahmatsobrian.umkchecker.data.dto.UmkDataset
import com.rahmatsobrian.umkchecker.data.local.dao.UmkDao
import com.rahmatsobrian.umkchecker.data.toEntity
import com.rahmatsobrian.umkchecker.util.AppLogger
import com.rahmatsobrian.umkchecker.util.Constants
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.json.Json
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Populates the local database from the bundled `assets/umk_data.json` file the
 * very first time the app runs (or whenever the table is empty). Because the data
 * source is a plain JSON asset, updating the dataset is as simple as replacing
 * that file — no code change, no migration, no API needed.
 */
@Singleton
class AssetDataSeeder @Inject constructor(
    @ApplicationContext private val context: Context,
    private val umkDao: UmkDao,
    private val json: Json
) {

    suspend fun seedIfNeeded() {
        if (umkDao.count() > 0) return
        runCatching {
            val raw = readAsset(Constants.SEED_ASSET_FILE)
            val dataset = json.decodeFromString(UmkDataset.serializer(), raw)
            val entities = dataset.items.map { it.toEntity() }
            umkDao.insertAll(entities)
            AppLogger.d(message = "Seeded ${entities.size} UMK records from assets")
        }.onFailure { throwable ->
            AppLogger.e(message = "Failed to seed UMK data from assets", throwable = throwable)
        }
    }

    private fun readAsset(fileName: String): String {
        context.assets.open(fileName).use { stream ->
            BufferedReader(InputStreamReader(stream)).use { reader ->
                return reader.readText()
            }
        }
    }
}

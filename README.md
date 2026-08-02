# UMK Checker

Aplikasi Android native (Kotlin + Jetpack Compose) untuk mengecek Upah Minimum
Provinsi/Kabupaten/Kota (UMP/UMK) seluruh Indonesia.

## ⚠️ Penting — tentang data

Dataset di `app/src/main/assets/umk_data.json` (133 entri: 38 provinsi + ~95
kabupaten/kota) adalah **data ilustrasi** yang disusun dari angka UMP 2025
yang pernah dipublikasikan secara luas, **bukan** hasil tarikan resmi dari
Kemnaker/Kepgub yang terverifikasi saat build ini dibuat. Sebelum dipakai
untuk keperluan resmi (HR, laporan, dsb), **verifikasi ulang** setiap angka
dengan Keputusan Menteri Ketenagakerjaan / Keputusan Gubernur terbaru.

Mengupdate data **tidak perlu mengubah kode** — cukup edit
`umk_data.json` mengikuti struktur:

```json
{
  "updatedAt": "2026-01-01",
  "source": "Kepmenaker No. ... Tahun ...",
  "items": [
    {
      "regionName": "Karawang",
      "provinceName": "Jawa Barat",
      "amount": 5599593,
      "year": 2025,
      "isActive": true,
      "isProvinceLevel": false
    }
  ]
}
```

Data di-seed ke Room database otomatis saat pertama kali app dijalankan
(dan setiap kali tombol "Refresh"/pull-to-refresh ditekan, tabel akan
dikosongkan lalu diisi ulang dari file ini). Kolom favorit disimpan di
tabel terpisah sehingga refresh data **tidak** menghapus bookmark pengguna.

## Arsitektur

Clean Architecture + MVVM, 3 layer:

```
presentation/   → Compose UI (screens, ViewModel, UiState) — StateFlow only
domain/         → Model murni, UseCase, interface Repository (tidak bergantung Android/Room)
data/           → Implementasi Repository, Room (DAO/Entity/Database), DataStore, DTO + mapper
di/             → Modul Hilt (Database, Repository binding, Dispatcher)
util/           → Resource<T> wrapper, formatter, logger, constants
```

Repository diakses hanya lewat interface `domain.repository.UmkRepository`,
jadi menambah sumber data lain (mis. Retrofit API resmi Kemnaker di masa
depan) hanya butuh implementasi baru — tidak ada layer di atasnya yang perlu
berubah.

## Stack

- Kotlin, Jetpack Compose, Material 3 (Dynamic Color / Monet, edge-to-edge)
- Navigation Compose (bottom nav: Beranda ↔ Favorit, + Detail)
- Hilt (DI), Room (local DB + full-text style search via SQL LIKE),
  DataStore Preferences (setting ringan), kotlinx.serialization (parsing
  JSON seed), Coroutines/Flow (StateFlow reaktif end-to-end)
- minSdk 29 (Android 10) — targetSdk 36

## Fitur

Pencarian real-time (debounced) dengan riwayat & saran, filter provinsi +
kota/kabupaten, 4 mode urutan (A-Z, Z-A, UMK tertinggi/terendah), halaman
detail, bookmark/favorit (persisten terpisah dari refresh data), salin
nominal, bagikan (share sheet), pull-to-refresh, loading/empty/error state,
retry, snackbar, offline-first (semua data lokal di Room).

## Menjalankan project

1. Buka folder ini di Android Studio (Ladybug/Meerkat atau lebih baru).
2. Biarkan Android Studio melakukan Gradle sync (akan otomatis mengunduh
   Gradle 8.9 sesuai `gradle/wrapper/gradle-wrapper.properties` bila
   `gradle-wrapper.jar` belum ada di cache lokal Anda).
3. Run ▶ pada device/emulator API 29+.

Build release (`./gradlew assembleRelease`) sudah dikonfigurasi dengan
R8 minify + shrink resources dan ProGuard rules yang menjaga entity Room
serta serializer kotlinx.serialization tetap berfungsi.

## Struktur folder singkat

```
app/src/main/java/com/rahmatsobrian/umkchecker/
├── data/
│   ├── local/          (Room: entity, dao, database, AssetDataSeeder)
│   ├── datastore/       (preferensi ringan: dynamic color)
│   ├── dto/              (model JSON seed)
│   └── repository/    (implementasi UmkRepository)
├── domain/
│   ├── model/, repository/, usecase/
├── presentation/
│   ├── home/, detail/, favorite/, filter/, common/, navigation/, theme/
├── di/                    (modul Hilt)
└── util/                  (Resource, CurrencyFormatter, AppLogger, Constants)
```

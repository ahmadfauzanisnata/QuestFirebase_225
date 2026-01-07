package com.example.myfirebase.modeldata

data class Siswa(
    val id: Long = 0,
    val nama: String = "",
    val alamat: String = "",
    val telpon: String = ""
)

data class DetailSiswa(
    val id: Long = 0,
    val nama: String = "",
    val alamat: String = "",
    val telpon: String = ""
)

// Fungsi ekstensi untuk mengubah DetailSiswa menjadi model data Siswa
fun DetailSiswa.toDataSiswa(): Siswa = Siswa(id, nama, alamat, telpon)

// Fungsi ekstensi untuk mengubah model data Siswa menjadi DetailSiswa
fun Siswa.toDetailSiswa(): DetailSiswa = DetailSiswa(id, nama, alamat, telpon)

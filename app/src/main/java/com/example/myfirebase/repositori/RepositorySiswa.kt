package com.example.myfirebase.repositori

import com.example.myfirebase.modeldata.Siswa
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

interface RepositorySiswa {
    suspend fun getDataSiswa(): List<Siswa>
    suspend fun postDataSiswa(siswa: Siswa)
    suspend fun getSiswaById(id: Long): Siswa?
    suspend fun upsertSiswa(siswa: Siswa)
}

class FirebaseRepositorySiswa : RepositorySiswa {
    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("siswa")

    override suspend fun getDataSiswa(): List<Siswa> {
        return try {
            collection.get().await().documents.map { doc ->
                Siswa(
                    id = doc.getLong("id") ?: 0L,
                    nama = doc.getString("nama") ?: "",
                    alamat = doc.getString("alamat") ?: "",
                    telpon = doc.getString("telpon") ?: ""
                )
            }
        } catch (_: Exception) {
            emptyList()
        }
    }

    override suspend fun postDataSiswa(siswa: Siswa) {
        val docRef = if (siswa.id == 0L) collection.document() else collection.document(siswa.id.toString())
        val data = hashMapOf(
            "id" to (siswa.id.takeIf { it != 0L } ?: docRef.id.hashCode().toLong()),
            "nama" to siswa.nama,
            "alamat" to siswa.alamat,
            "telpon" to siswa.telpon
        )
        docRef.set(data).await()
    }

    override suspend fun getSiswaById(id: Long): Siswa? {
        return try {
            val query = collection.whereEqualTo("id", id).get().await()
            val doc = query.documents.firstOrNull() ?: return null
            Siswa(
                id = doc.getLong("id") ?: 0L,
                nama = doc.getString("nama") ?: "",
                alamat = doc.getString("alamat") ?: "",
                telpon = doc.getString("telpon") ?: ""
            )
        } catch (_: Exception) {
            null
        }
    }

    override suspend fun upsertSiswa(siswa: Siswa) {
        try {
            if (siswa.id == 0L) {
                // Create new document and set its numeric id field
                val docRef = collection.document()
                val newId = docRef.id.hashCode().toLong()
                val data = hashMapOf(
                    "id" to newId,
                    "nama" to siswa.nama,
                    "alamat" to siswa.alamat,
                    "telpon" to siswa.telpon
                )
                docRef.set(data).await()
            } else {
                // Find existing document by field `id` and update it. If not found, create new doc with that id as string key.
                val query = collection.whereEqualTo("id", siswa.id).get().await()
                val doc = query.documents.firstOrNull()
                if (doc != null) {
                    val data = hashMapOf(
                        "id" to siswa.id,
                        "nama" to siswa.nama,
                        "alamat" to siswa.alamat,
                        "telpon" to siswa.telpon
                    )
                    collection.document(doc.id).set(data).await()
                } else {
                    // fallback: create document with key = siswa.id.toString()
                    val data = hashMapOf(
                        "id" to siswa.id,
                        "nama" to siswa.nama,
                        "alamat" to siswa.alamat,
                        "telpon" to siswa.telpon
                    )
                    collection.document(siswa.id.toString()).set(data).await()
                }
            }
        } catch (_: Exception) {
            // swallow or rethrow depending on desired behavior; keep silent to match prior behavior
        }
    }
}
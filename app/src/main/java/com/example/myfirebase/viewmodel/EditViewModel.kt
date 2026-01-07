package com.example.myfirebase.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfirebase.modeldata.DetailSiswa
import com.example.myfirebase.modeldata.Siswa
import com.example.myfirebase.modeldata.UIStateSiswa
import com.example.myfirebase.modeldata.toDataSiswa
import com.example.myfirebase.modeldata.toUiStateSiswa
import com.example.myfirebase.repositori.RepositorySiswa
import kotlinx.coroutines.launch

class EditViewModel(private val repositorySiswa: RepositorySiswa) : ViewModel() {
    var uiStateSiswa by mutableStateOf(UIStateSiswa())
        private set

    /* Load siswa by id and populate ui state */
    fun loadSiswaById(id: Long) {
        viewModelScope.launch {
            val siswa = repositorySiswa.getSiswaById(id)
            if (siswa != null) {
                uiStateSiswa = siswa.toUiStateSiswa(isEntryValid = true)
            }
        }
    }

    private fun validasiInput(uiState: DetailSiswa = uiStateSiswa.detailSiswa): Boolean {
        return with(uiState) {
            nama.isNotBlank() && alamat.isNotBlank() && telpon.isNotBlank()
        }
    }

    fun updateUiState(detailSiswa: DetailSiswa) {
        uiStateSiswa =
            UIStateSiswa(
                detailSiswa = detailSiswa, isEntryValid = validasiInput(detailSiswa)
            )
    }

    /* Save (upsert) siswa */
    fun saveSiswa(onDone: () -> Unit = {}) {
        viewModelScope.launch {
            repositorySiswa.upsertSiswa(uiStateSiswa.detailSiswa.toDataSiswa())
            onDone()
        }
    }
}
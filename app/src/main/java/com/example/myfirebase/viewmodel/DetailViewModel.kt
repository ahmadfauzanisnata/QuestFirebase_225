package com.example.myfirebase.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfirebase.modeldata.DetailSiswa
import com.example.myfirebase.modeldata.toDetailSiswa
import com.example.myfirebase.repositori.RepositorySiswa
import kotlinx.coroutines.launch

class DetailViewModel(private val repositorySiswa: RepositorySiswa) : ViewModel() {
    var detail by mutableStateOf(DetailSiswa())
        private set

    fun loadDetail(id: Long) {
        viewModelScope.launch {
            val siswa = repositorySiswa.getSiswaById(id)
            if (siswa != null) {
                detail = siswa.toDetailSiswa()
            }
        }
    }
}
package com.example.myfirebase.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myfirebase.R
import com.example.myfirebase.modeldata.Siswa
import com.example.myfirebase.view.route.DestinasiHome
import com.example.myfirebase.viewmodel.HomeViewModel
import com.example.myfirebase.viewmodel.PenyediaViewModel
import com.example.myfirebase.viewmodel.StatusUiSiswa

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navigateToItemEntry: () -> Unit,
    navigateToItemEdit: (Long) -> Unit,
    navigateToItemDetail: (Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            SiswaTopAppBar(
                title = stringResource(DestinasiHome.titleRes),
                canNavigateBack = false,
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToItemEntry,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_large))
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.entry_siswa)
                )
            }
        }
    ) { innerPadding ->
        HomeBody(
            statusUiSiswa = viewModel.statusUiSiswa,
            onSiswaEdit = { id -> navigateToItemEdit(id) },
            onSiswaDetail = { id -> navigateToItemDetail(id) },
            retryAction = viewModel::loadSiswa,
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
        )
    }
}

@Composable
fun HomeBody(
    statusUiSiswa: StatusUiSiswa,
    onSiswaEdit: (Long) -> Unit,
    onSiswaDetail: (Long) -> Unit,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        when (statusUiSiswa) {
            is StatusUiSiswa.Loading -> LoadingScreen()
            is StatusUiSiswa.Success -> DaftarSiswa(
                itemSiswa = statusUiSiswa.siswa,
                onEditClick = { onSiswaEdit(it.id) },
                onDetailClick = { onSiswaDetail(it.id) }
            )
            is StatusUiSiswa.Error -> ErrorScreen(
                retryAction = retryAction,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(R.drawable.pascol),
        contentDescription = stringResource(R.string.loading),
        modifier = modifier.size(200.dp)
    )
}

@Composable
fun ErrorScreen(
    retryAction: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.gagal),
            modifier = Modifier.padding(16.dp)
        )
        Button(onClick = retryAction) {
            Text(stringResource(R.string.retry))
        }
    }
}

@Composable
fun DaftarSiswa(
    itemSiswa: List<Siswa>,
    onEditClick: (Siswa) -> Unit,
    onDetailClick: (Siswa) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(items = itemSiswa, key = { it.id }) { person ->
            ItemSiswa(
                siswa = person,
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.padding_small)),
                onEditClick = { onEditClick(person) },
                onDetailClick = { onDetailClick(person) }
            )
        }
    }
}

@Composable
fun ItemSiswa(
    siswa: Siswa,
    modifier: Modifier = Modifier,
    onEditClick: () -> Unit = {},
    onDetailClick: () -> Unit = {}
) {
    Card(
        modifier = modifier.clickable { onDetailClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_large)),
            verticalArrangement = Arrangement.spacedBy(
                dimensionResource(id = R.dimen.padding_small)
            )
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = siswa.nama,
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = Icons.Default.Phone,
                    contentDescription = null
                )
                Text(
                    text = siswa.telpon,
                    style = MaterialTheme.typography.titleMedium
                )
                IconButton(onClick = onEditClick) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = stringResource(R.string.edit_siswa))
                }
            }
            Text(
                text = siswa.alamat,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}
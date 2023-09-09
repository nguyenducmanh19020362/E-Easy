package com.example.mydictionary.viewmodels

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mydictionary.database.dao.FolderDao
import com.example.mydictionary.database.table.Folder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


@Suppress("UNCHECKED_CAST")
class HomeViewModel: ViewModel() {
    private val _listFolder = MutableStateFlow(listOf<Folder>())
    val listFolder = _listFolder.asStateFlow()

    private val _progressBar = MutableStateFlow(0)
    val progressBar = _progressBar.asStateFlow()

    fun getAllFolders(folderDao: FolderDao){
        viewModelScope.launch (Dispatchers.IO){
            _listFolder.tryEmit(folderDao.getAllFolders())
        }
    }

    fun addFolder(folderDao: FolderDao, folder: Folder){
        viewModelScope.launch(Dispatchers.IO){
            folderDao.addFolder(folder)
            _progressBar.tryEmit(0)
        }
    }

    private fun deleteFolder(folderDao: FolderDao, folder: Folder){
        viewModelScope.launch(Dispatchers.IO){
            folderDao.deleteFolder(folder)
        }
    }

    fun deleteFolders(folderDao: FolderDao,  listFolders: List<Folder>){
        for(folder in listFolders){
            deleteFolder(folderDao, folder)
        }
        _progressBar.tryEmit(0)
    }

    fun deleteAll(folderDao: FolderDao){
        viewModelScope.launch (Dispatchers.IO){
            folderDao.deleteAll()
        }
    }

    fun getDeletedFolders(listFolders: List<Folder>, checked: SnapshotStateList<Boolean>): List<Folder>{
        val list = mutableListOf<Folder>()
        for(folder in listFolders){
            if(checked[listFolders.indexOf(folder)]){
                list.add(folder)
            }
        }
        return list.toList()
    }

    fun setProgressBar(){
        viewModelScope.launch (Dispatchers.IO){
            _progressBar.emit(1)
        }
    }
}
package com.example.nots.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.nots.entities.LibraryItem
import com.example.nots.entities.NoteItem
import com.example.nots.entities.ShopListItem
import com.example.nots.entities.ShopListNameItem
import kotlinx.coroutines.flow.Flow


@Dao
interface Dao {
    //notes
    @Insert
    suspend fun insertNote(note: NoteItem)
    @Query("SELECT * FROM note_list")
    fun getAllNotes(): Flow<List<NoteItem>>
    @Query("DELETE FROM note_list WHERE id IS :id")
    suspend fun deleteNote(id: Int)
    @Update
    suspend fun updateNote(note: NoteItem)

    //lists
    @Insert
    suspend fun insertShopListName(name: ShopListNameItem)
    @Query("SELECT * FROM shopping_list_names")
    fun getAllShopListNames(): Flow<List<ShopListNameItem>>
    @Query("DELETE FROM shopping_list_names WHERE id IS :id")
    suspend fun deleteShopListName(id: Int)
    @Update
    suspend fun updateListName(shopListName: ShopListNameItem)

    //listItem
    @Insert
    suspend fun insertItem(shopListItem: ShopListItem)
    @Query("SELECT * FROM shopping_list_item WHERE listId LIKE :listId")
    fun getAllShopListItems(listId: Int): Flow<List<ShopListItem>>
    @Update
    suspend fun updateListItem(item: ShopListItem)
    @Query("DELETE FROM shopping_list_item WHERE listId LIKE :listId")
    suspend fun deleteShopItemsByListId(listId: Int)

    //library
    @Query("SELECT * FROM library WHERE name LIKE :name")
    suspend fun getAllLibraryItems(name: String): List<LibraryItem>
    @Insert
    suspend fun insertLibraryItem(libraryItem: LibraryItem)
    @Update
    suspend fun updateLibraryItem(item: LibraryItem)
    @Query("DELETE FROM library WHERE id IS :id")
    suspend fun deleteLibraryItem(id: Int)

}
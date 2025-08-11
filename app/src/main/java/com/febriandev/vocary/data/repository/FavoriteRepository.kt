package com.febriandev.vocary.data.repository

import com.febriandev.vocary.data.db.dao.VocabularyDao
import com.febriandev.vocary.data.db.entity.VocabularyEntity
import com.febriandev.vocary.domain.Vocabulary
import com.febriandev.vocary.domain.toVocabulary
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FavoriteRepository @Inject constructor(
    private val dao: VocabularyDao
) {
    suspend fun addToFavorite(id: String) {
        dao.addToFavorite(id)
    }

    suspend fun removeFromFavorite(id: String) {
        dao.removeFromFavorite(id)
    }

    suspend fun isFavorite(detailId: String): Boolean = dao.isFavorite(detailId)

//    fun isFavoriteFlow(id: String): Flow<Boolean> {
//        return dao.isFavorite(id).flowOn(Dispatchers.IO)
//    }

    fun getAllFavorites(): Flow<List<Vocabulary>> =
        dao.getFavorites().map { it.map { it.toVocabulary() } }

//    suspend fun getAllFavoriteWithDetail(): List<FavoriteVocabJoinResult> = dao.getAllFavoriteWithDetail().distinctBy { it.definition }
//
//    fun getAllFavoriteWithDetailFlow():Flow<List<FavoriteVocabJoinResult>> = dao.getAllFavoriteWithDetailFlow().map { list ->
//        list.distinctBy { it.definition?.trim()?.lowercase() }
//    }

    fun searchFavorite(query: String): Flow<List<Vocabulary>> {
        return dao.searchFavorite(query).map { it.map { it.toVocabulary() } }
    }
}

package com.data.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

//@Dao
//interface CartDao {
//	@Insert(onConflict = OnConflictStrategy.REPLACE)
//	suspend fun insertCartItem(item: CartItemEntity)
//
//	@Query("DELETE FROM cart_items WHERE id = :itemId")
//	suspend fun deleteCartItem(itemId: String)
//
//	@Query("SELECT * FROM cart_items")
//	fun getAllCartItems(): Flow<List<CartItemEntity>>
//
//	@Query("DELETE FROM cart_items")
//	suspend fun clearCart()
//
//	@Update
//	suspend fun updateCartItem(item: CartItemEntity)
//
//	@Query("SELECT * FROM cart_items WHERE id = :itemId")
//	suspend fun getCartItemById(itemId: String): CartItemEntity?
//}
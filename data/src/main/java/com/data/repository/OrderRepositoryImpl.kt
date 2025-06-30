//package com.data.repository
//
//import com.domain.model.AddressDomainModel
//import com.domain.model.OrdersListModel
//import com.domain.network.NetworkService
//import com.domain.network.ResultWrapper
//import com.domain.repository.OrderRepository
//
//
//class OrderRepositoryImpl(private val networkService: NetworkService) : OrderRepository {
//    override suspend fun placeOrder(
//        addressDomainModel: AddressDomainModel,
//        userId: Long
//    ): ResultWrapper<Long> {
//        return networkService.placeOrder(addressDomainModel, userId)
//    }
//
//    override suspend fun getOrderList(userId: Long): ResultWrapper<OrdersListModel> {
//        return networkService.getOrderList(userId)
//    }
//}
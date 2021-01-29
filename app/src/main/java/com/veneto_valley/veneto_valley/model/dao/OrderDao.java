package com.veneto_valley.veneto_valley.model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.veneto_valley.veneto_valley.model.entities.Order;

import java.util.List;

@Dao
public interface OrderDao extends BaseDao<Order> {
	
	@Query("SELECT * FROM `Order` WHERE tableCode = :code")
	LiveData<List<Order>> getAllByTable(String code);
	
	@Query("SELECT SUM(price) FROM `Order` WHERE tableCode = :tavolo AND NOT receivedFromSlave")
	float getTotaleExtra(String tavolo);
	
	@Query("SELECT * FROM `Order` WHERE status = :status AND tableCode = :tavolo AND NOT receivedFromSlave")
	LiveData<List<Order>> getAllbyStatus(Order.OrderStatus status, String tavolo);
	
	@Query("SELECT * FROM `Order` WHERE tableCode = :tavolo AND status = :status")
	LiveData<List<Order>> getAllSynchronized(String tavolo, Order.OrderStatus status);
	
	@Query("SELECT * FROM `Order` WHERE status = :status AND tableCode = :tavolo AND dish = :piatto AND user = :utente")
	Order contains(Order.OrderStatus status, String tavolo, String piatto, String utente);
	
	@Query("DELETE FROM `Order` WHERE tableCode = :tavolo")
	void deleteByTable(String tavolo);
	
	@Query("DELETE FROM `Order` WHERE receivedFromSlave")
	void deleteSlaves();
	
	@Insert
	long[] insertAll(Order... obj);
	
	@Query("DELETE FROM `Order` WHERE id in (:idOrdine)")
	void deleteAllById(long[] idOrdine);
}

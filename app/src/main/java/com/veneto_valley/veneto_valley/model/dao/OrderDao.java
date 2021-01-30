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

    @Query("SELECT SUM(price) FROM `Order` WHERE tableCode = :table AND NOT receivedFromSlave")
    float getExtraPrice(String table);

    @Query("SELECT * FROM `Order` WHERE status = :status AND tableCode = :table AND NOT receivedFromSlave")
    LiveData<List<Order>> getAllByStatus(Order.OrderStatus status, String table);

    @Query("SELECT * FROM `Order` WHERE tableCode = :table AND status = :status")
    LiveData<List<Order>> getAllSynchronized(String table, Order.OrderStatus status);

    @Query("SELECT * FROM `Order` WHERE status = :status AND tableCode = :table AND dish = :dish AND user = :user")
    Order contains(Order.OrderStatus status, String table, String dish, String user);

    @Query("DELETE FROM `Order` WHERE tableCode = :table")
    void deleteByTable(String table);

    @Query("DELETE FROM `Order` WHERE receivedFromSlave")
    void deleteSlaves();

    @Insert
    long[] insertAll(Order... obj);

    @Query("DELETE FROM `Order` WHERE id in (:id)")
    void deleteAllById(long[] id);
}

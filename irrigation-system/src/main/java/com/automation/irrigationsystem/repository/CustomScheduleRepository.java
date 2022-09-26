package com.automation.irrigationsystem.repository;

import com.automation.irrigationsystem.entity.CustomSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface CustomScheduleRepository extends JpaRepository<CustomSchedule, BigDecimal> {

    public List<CustomSchedule> findByIrrigationDayInAndActiveFlg(List<String> irrigationDayList, BigDecimal activeFlg);

    public CustomSchedule findByIdAndCustomerIdAndActiveFlg(BigDecimal id, BigDecimal customerId, BigDecimal activeFlg);
}

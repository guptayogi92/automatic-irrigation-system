package com.automation.irrigationsystem.repository;

import com.automation.irrigationsystem.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, BigDecimal> {

    List<Schedule> findByCropNameAndSoilTypeAndWeatherTypeAndActiveFlg(String cropName
            , String soilType, String weatherType, BigDecimal activeFlg);

    List<Schedule> findByWeatherTypeAndIrrigationDayInAndActiveFlg(String weatherType, List<String> irrigationDaysList, BigDecimal activeFlg);
}

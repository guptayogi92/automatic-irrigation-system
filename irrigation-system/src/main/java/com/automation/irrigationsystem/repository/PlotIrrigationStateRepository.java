package com.automation.irrigationsystem.repository;

import com.automation.irrigationsystem.entity.PlotIrrigationState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface PlotIrrigationStateRepository extends JpaRepository<PlotIrrigationState, BigDecimal> {

    @Query(value="select * from PLOT_IRRIGATION_STATE pis where (pis.IRRIGATION_STATE = :irrState " +
            "AND pis.SCHEDULED_DT >= :startDate AND pis.SCHEDULED_DT < :endDate) OR " +
            "(pis.IRRIGATION_STATE = :retryIrrState AND " +
            "pis.SCHEDULED_DT >= :retryStartDate AND pis.SCHEDULED_DT < :endDate)", nativeQuery = true)
    public List<PlotIrrigationState> getPlotListToIrrigate(@Param("irrState") String irrState
            , @Param("startDate") Date startDate, @Param("endDate") Date endDate
            ,@Param("retryIrrState") String retryIrrState , @Param("retryStartDate") Date retryStartDate);

    @Query(value = "select * from PLOT_IRRIGATION_STATE pis where pis.SCHEDULED_DT >= :startDate AND pis.SCHEDULED_DT < :endDate", nativeQuery = true)
    public List<PlotIrrigationState> getPlotsForToday(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

}
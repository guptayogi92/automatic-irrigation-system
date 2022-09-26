package com.automation.irrigationsystem.repository;

import com.automation.irrigationsystem.entity.Plot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Repository
public interface PlotRepository extends JpaRepository<Plot, BigDecimal> {

    public List<Plot> findByCustomerIdAndActiveFlg(BigDecimal customerId, BigDecimal activeFlg);
    public Plot findByCustomerIdAndIdAndActiveFlg(BigDecimal customerId, BigDecimal id, BigDecimal activeFlg);

    public List<Plot> findByScheduleTypeAndCustomSchedIdInAndActiveFlg(String scheduleType, Set<BigDecimal> customSchedIdList, BigDecimal activeFlg);

    public List<Plot> findByScheduleTypeAndCropNameInAndSoilTypeInAndActiveFlg(String name, Set<String> cropNameSet, Set<String> soilTypeSet, BigDecimal activeFlg);

    public Plot findByIdAndCustomerIdAndCustomSchedIdAndActiveFlg(BigDecimal id, BigDecimal customerId, BigDecimal customSchedId, BigDecimal activeFlg);

    public List<Plot> findByCustomerIdAndCustomSchedIdAndActiveFlg(BigDecimal customerId, BigDecimal customSchedId, BigDecimal activeFlg);
}

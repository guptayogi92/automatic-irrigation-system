package com.automation.irrigationsystem.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "PLOT_IRRIGATION_STATE")
@Data
@NoArgsConstructor
public class PlotIrrigationState {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="ID", nullable = false, precision = 22, scale = 0)
    private BigDecimal id;

    @Column(name="PLOT_ID", nullable = false, precision = 2, scale = 0)
    private BigDecimal plotId;

    @Column(name = "DEVICE_ID", nullable = false, precision = 22, scale = 0)
    private BigDecimal deviceId;

    @Column(name="IRRIGATION_STATE", length = 20, nullable = false)
    private String irrigationState;

    @Column(name="RETRY_COUNT", nullable = false, precision = 1, scale = 0)
    private BigDecimal retryCount;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="SCHEDULED_DT", nullable = false)
    private Date scheduledDt;

    @Column(name="HOURS_TO_IRRIGATE", nullable = false, precision = 2, scale = 0)
    private BigDecimal hoursToIrrigate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="CREATED_DT", nullable = false)
    private Date createdDt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="MODIFIED_DT", nullable = true)
    private Date modifiedDt;
}

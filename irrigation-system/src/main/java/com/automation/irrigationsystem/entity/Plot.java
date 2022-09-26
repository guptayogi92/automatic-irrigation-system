package com.automation.irrigationsystem.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "PLOT")
@Data
@NoArgsConstructor
public class Plot {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
    private BigDecimal id;

    @Column(name = "CUSTOMER_ID", unique = false, nullable = false, precision = 22, scale = 0)
    private BigDecimal customerId;

    @Column(name="PLOT_NAME", length = 50, nullable = false)
    private String plotName;

    @Column(name="DESCRIPTION", length = 200, nullable = true)
    private String description;

    @Column(name="ADDRESS", length = 200, nullable = false)
    private String address;

    @Column(name="WIDTH", nullable = false)
    private BigDecimal width;

    @Column(name="HEIGHT", nullable = false)
    private BigDecimal height;

    @Column(name="UNIT", length = 10, nullable = false)
    private String unit;

    @Column(name="SCHEDULE_TYPE", length = 7, nullable = false)
    private String scheduleType;

    @Column(name = "CUSTOM_SCHED_ID", nullable = true, precision = 22, scale = 0)
    private BigDecimal customSchedId;

    @Column(name = "DEVICE_ID", nullable = false, precision = 22, scale = 0)
    private BigDecimal deviceId;

    @Column(name="CROP_NAME", length = 20, nullable = false)
    private String cropName;

    @Column(name="SOIL_TYPE", length = 20, nullable = false)
    private String soilType;

    @Column(name = "ACTIVE_FLG", nullable = false, precision = 1, scale = 0)
    private BigDecimal activeFlg;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="CREATED_DT", nullable = false)
    private Date createdDt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="MODIFIED_DT", nullable = true)
    private Date modifiedDt;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "CUSTOM_SCHED_ID", referencedColumnName = "id", insertable = false, updatable = false)
    private CustomSchedule customSchedule;
}
package com.automation.irrigationsystem.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "SCHEDULE")
@Data
@NoArgsConstructor
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
    private BigDecimal id;

    @Column(name="SCHEDULE_NAME", length = 50, nullable = false)
    private String scheduleName;

    @Column(name="DESCRIPTION", length = 200, nullable = true)
    private String description;

    @Column(name="CROP_NAME", length = 20, nullable = false)
    private String cropName;

    @Column(name="SOIL_TYPE", length = 20, nullable = false)
    private String soilType;

    @Column(name="WEATHER_TYPE", length = 20, nullable = false)
    private String weatherType;

    @Column(name="IRRIGATION_DAY", length = 20, nullable = false)
    private String irrigationDay;

    @Column(name="HOURS_TO_IRRIGATE", nullable = false, precision = 2, scale = 0)
    private BigDecimal hoursToIrrigate;

    @Column(name = "ACTIVE_FLG", nullable = false, precision = 1, scale = 0)
    private BigDecimal activeFlg;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="CREATED_DT", nullable = false)
    private Date createdDt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="MODIFIED_DT", nullable = true)
    private Date modifiedDt;
}
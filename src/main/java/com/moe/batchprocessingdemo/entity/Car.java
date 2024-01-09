package com.moe.batchprocessingdemo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.yaml.snakeyaml.events.Event;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "CARS_INFO")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Car {
    @Id
    @Column
    private Integer id;
    @Column
    private String name;
    @Column
    private String model;
    @Column
    private String color;
    @Column
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfCreation;
    @Column
    private Double price;

}

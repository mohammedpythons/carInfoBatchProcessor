package com.moe.batchprocessingdemo.config;

import com.moe.batchprocessingdemo.entity.Car;
import org.springframework.batch.item.ItemProcessor;

import java.util.Date;

public class CarProcessor implements ItemProcessor<Car, Car> {
    @Override
    public Car process(Car car) throws Exception {
        return car;
    }
}

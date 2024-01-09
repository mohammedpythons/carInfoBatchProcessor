package com.moe.batchprocessingdemo.config;

import com.moe.batchprocessingdemo.entity.Car;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CarFieldSetMapper implements FieldSetMapper<Car> {
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    @Override
    public Car mapFieldSet(FieldSet fieldSet) throws BindException {

        Car car = new Car();
        car.setId(fieldSet.readInt("id"));
        car.setName(fieldSet.readString("name"));
        car.setModel(fieldSet.readString("model"));
        String dateString = fieldSet.readString("dateOfCreation");
        LocalDate date = LocalDate.parse(dateString, dateFormatter);
        car.setDateOfCreation(date);
        car.setColor(fieldSet.readString("color"));
        car.setPrice(fieldSet.readDouble("price"));

        return car;
    }
}

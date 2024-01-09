package com.moe.batchprocessingdemo.repository;

import com.moe.batchprocessingdemo.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarRepository extends JpaRepository<Car, Integer> {
}

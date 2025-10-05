package com.guilherme.Vehicle_Register.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "vehicle")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "A placa não pode estar em branco.")
    @Column(unique = true, nullable = false)
    private String plate;

    @NotBlank(message = "O modelo não pode estar em branco.")
    private String model;

    @NotBlank(message = "O fabricante não pode estar em branco.")
    private String manufacturer;
    
    @NotNull(message = "O ano não pode ser nulo.")
    @Min(value = 1900, message = "O ano deve ser no mínimo 1900.")
    @Max(value = 2025, message = "O ano não pode ser maior que 2025.")
    private int year;
}
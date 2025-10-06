package com.guilherme.Vehicle_Register.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import com.guilherme.Vehicle_Register.entity.Vehicle;
import com.guilherme.Vehicle_Register.repository.VehicleRepository;

import jakarta.persistence.criteria.Predicate;



@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public Vehicle createVehicle(Vehicle vehicle){
        return vehicleRepository.save(vehicle);
    }

    public List<Vehicle> getAllVehicles() {
        try{
            return vehicleRepository.findAll();
        } catch(Exception e){
            throw new RuntimeException("Não foi possível listar os veículos.");
        }
    }

    public List<Vehicle> getFilteredVehicles(Vehicle filter) {
        return vehicleRepository.findAll((Specification<Vehicle>) (root, query, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();

            // Busca placa que possua sequencia de dígitos enviados no filtro
            if (filter.getPlate() != null && !filter.getPlate().isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("plate")), "%" + filter.getPlate().toLowerCase() + "%"));
            }

            // Busca modelo que possua sequencia de dígitos enviados no filtro
            if (filter.getModel() != null && !filter.getModel().isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("model")), "%" + filter.getModel().toLowerCase() + "%"));
            }

            // Busca fabricante que possua sequencia de dígitos enviados no filtro
            if (filter.getManufacturer() != null && !filter.getManufacturer().isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("manufacturer")), "%" + filter.getManufacturer().toLowerCase() + "%"));
            }

            // Busca carros que são do ano ou mais recentes que o filtro
            if (filter.getYear() > 0) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("year"), filter.getYear()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }
}

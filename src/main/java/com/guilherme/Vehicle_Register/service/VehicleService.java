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

    // Injeção de dependência via construtor (prática recomendada)
    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public Vehicle createVehicle(Vehicle vehicle){
        // O método save() retorna a entidade salva, incluindo o ID gerado.
        return vehicleRepository.save(vehicle);
    }

    public List<Vehicle> getAllVehicles() {
        // O método findAll() é fornecido pelo JpaRepository e retorna todos os registros da tabela.
        try{
            return vehicleRepository.findAll();
        } catch(Exception e){
            throw new RuntimeException("Não foi possível listar os veículos.");
        }
    }

    public List<Vehicle> getFilteredVehicles(Vehicle filter) {
        // A Specification é uma forma de construir consultas dinâmicas.
        // (root, query, criteriaBuilder) -> são os três argumentos para construir a query.
        return vehicleRepository.findAll((Specification<Vehicle>) (root, query, criteriaBuilder) -> {

            // Lista para armazenar as condições (predicados) do nosso filtro.
            List<Predicate> predicates = new ArrayList<>();

            // Se a placa foi informada no filtro, adiciona uma condição "LIKE" para ela.
            if (filter.getPlate() != null && !filter.getPlate().isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("plate")), "%" + filter.getPlate().toLowerCase() + "%"));
            }

            // Se o modelo foi informado, adiciona a condição para ele.
            if (filter.getModel() != null && !filter.getModel().isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("model")), "%" + filter.getModel().toLowerCase() + "%"));
            }

            // Se o fabricante foi informado, adiciona a condição para ele.
            if (filter.getManufacturer() != null && !filter.getManufacturer().isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("manufacturer")), "%" + filter.getManufacturer().toLowerCase() + "%"));
            }

            // Se o ano foi informado (e não é o valor padrão 0), adiciona uma condição de igualdade.
            if (filter.getYear() > 0) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("year"), filter.getYear()));
            }

            // Combina todos os predicados com "AND" e retorna a consulta final.
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }
}

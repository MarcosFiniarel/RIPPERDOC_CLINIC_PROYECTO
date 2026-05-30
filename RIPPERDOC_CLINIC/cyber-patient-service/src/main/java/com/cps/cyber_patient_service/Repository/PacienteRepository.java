package com.cps.cyber_patient_service.Repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.cps.cyber_patient_service.Model.Paciente;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente,Long>{
    boolean existsByAlias(String alias);
}

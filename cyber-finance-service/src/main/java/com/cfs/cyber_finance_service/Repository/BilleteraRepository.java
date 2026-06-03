package com.cfs.cyber_finance_service.Repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.cfs.cyber_finance_service.Model.Billetera;

import java.util.Optional;

@Repository
public interface BilleteraRepository extends JpaRepository<Billetera, Long>{
    Optional<Billetera> findByPacienteId(Long pacienteId);
}

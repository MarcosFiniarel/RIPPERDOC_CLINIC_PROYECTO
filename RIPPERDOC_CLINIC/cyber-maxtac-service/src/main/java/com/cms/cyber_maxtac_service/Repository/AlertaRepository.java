package com.cms.cyber_maxtac_service.Repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.cms.cyber_maxtac_service.Model.Alerta;

import java.util.List;

@Repository
public interface AlertaRepository extends JpaRepository<Alerta, Long>{
    List<Alerta> findByPacienteId(Long pacienteId);
    List<Alerta> findByAlias(String alias);
}

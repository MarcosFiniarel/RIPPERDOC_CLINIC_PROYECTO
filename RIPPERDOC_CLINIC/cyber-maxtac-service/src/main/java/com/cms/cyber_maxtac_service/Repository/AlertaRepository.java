package com.cms.cyber_maxtac_service.Repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.cms.cyber_maxtac_service.Model.Alerta;

@Repository
public interface AlertaRepository extends JpaRepository<Alerta, Long>{
}

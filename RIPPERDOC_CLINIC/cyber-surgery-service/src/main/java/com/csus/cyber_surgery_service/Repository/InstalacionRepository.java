package com.csus.cyber_surgery_service.Repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.csus.cyber_surgery_service.Model.Instalacion;

@Repository
public interface InstalacionRepository extends JpaRepository<Instalacion,Long>{
}

package com.ccos.cyber_compatibility_service.Repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.ccos.cyber_compatibility_service.Model.Compatibilidad;

import java.util.List;

@Repository
public interface CompatibilidadRepository extends JpaRepository<Compatibilidad, Long>{
    List<Compatibilidad> findByEstado(boolean estado);
}

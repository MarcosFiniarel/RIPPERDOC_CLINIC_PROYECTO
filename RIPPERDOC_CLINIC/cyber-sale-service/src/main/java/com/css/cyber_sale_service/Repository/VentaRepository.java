package com.css.cyber_sale_service.Repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.css.cyber_sale_service.Model.Venta;

@Repository
public interface VentaRepository extends JpaRepository<Venta,Long>{
    java.util.List<Venta> findByPacienteId(Long pacienteId);
    java.util.List<Venta> findByCiberwareId(Long ciberwareId);
}

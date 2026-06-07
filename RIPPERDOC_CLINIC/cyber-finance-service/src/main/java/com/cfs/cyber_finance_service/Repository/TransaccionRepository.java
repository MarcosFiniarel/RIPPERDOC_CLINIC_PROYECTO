package com.cfs.cyber_finance_service.Repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.cfs.cyber_finance_service.Model.Transaccion;

import java.util.List;

@Repository
public interface TransaccionRepository extends JpaRepository<Transaccion,Long>{
    List<Transaccion> findByBilleteraId(Long billeteraId);
}

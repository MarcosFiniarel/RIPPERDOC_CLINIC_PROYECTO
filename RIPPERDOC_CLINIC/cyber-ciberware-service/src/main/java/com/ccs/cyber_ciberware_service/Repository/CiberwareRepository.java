package com.ccs.cyber_ciberware_service.Repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.ccs.cyber_ciberware_service.Model.Ciberware;

@Repository
public interface CiberwareRepository extends JpaRepository<Ciberware,Long>{
}

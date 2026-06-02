package com.chs.cyber_humanity_service.Repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.chs.cyber_humanity_service.Model.Humanidad;

@Repository
public interface HumanidadRepository extends  JpaRepository<Humanidad,Long>{
}

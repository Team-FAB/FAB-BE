package com.fab.banggabgo.repository;

import com.fab.banggabgo.entity.Mate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MateRepository extends JpaRepository<Mate, Integer>, MateRepositoryCustom {

}

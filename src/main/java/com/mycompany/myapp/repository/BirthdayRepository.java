package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Birthday;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Birthday entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BirthdayRepository extends JpaRepository<Birthday, Long> {}

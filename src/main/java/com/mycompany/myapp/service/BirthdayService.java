package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Birthday;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Birthday}.
 */
public interface BirthdayService {
    /**
     * Save a birthday.
     *
     * @param birthday the entity to save.
     * @return the persisted entity.
     */
    Birthday save(Birthday birthday);

    /**
     * Updates a birthday.
     *
     * @param birthday the entity to update.
     * @return the persisted entity.
     */
    Birthday update(Birthday birthday);

    /**
     * Partially updates a birthday.
     *
     * @param birthday the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Birthday> partialUpdate(Birthday birthday);

    /**
     * Get all the birthdays.
     *
     * @return the list of entities.
     */
    List<Birthday> findAll();

    /**
     * Get the "id" birthday.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Birthday> findOne(Long id);

    /**
     * Delete the "id" birthday.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

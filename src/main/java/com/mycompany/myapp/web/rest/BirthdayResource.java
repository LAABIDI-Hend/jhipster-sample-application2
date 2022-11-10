package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Birthday;
import com.mycompany.myapp.repository.BirthdayRepository;
import com.mycompany.myapp.service.BirthdayService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Birthday}.
 */
@RestController
@RequestMapping("/api")
public class BirthdayResource {

    private final Logger log = LoggerFactory.getLogger(BirthdayResource.class);

    private static final String ENTITY_NAME = "birthday";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BirthdayService birthdayService;

    private final BirthdayRepository birthdayRepository;

    public BirthdayResource(BirthdayService birthdayService, BirthdayRepository birthdayRepository) {
        this.birthdayService = birthdayService;
        this.birthdayRepository = birthdayRepository;
    }

    /**
     * {@code POST  /birthdays} : Create a new birthday.
     *
     * @param birthday the birthday to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new birthday, or with status {@code 400 (Bad Request)} if the birthday has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/birthdays")
    public ResponseEntity<Birthday> createBirthday(@RequestBody Birthday birthday) throws URISyntaxException {
        log.debug("REST request to save Birthday : {}", birthday);
        if (birthday.getId() != null) {
            throw new BadRequestAlertException("A new birthday cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Birthday result = birthdayService.save(birthday);
        return ResponseEntity
            .created(new URI("/api/birthdays/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /birthdays/:id} : Updates an existing birthday.
     *
     * @param id the id of the birthday to save.
     * @param birthday the birthday to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated birthday,
     * or with status {@code 400 (Bad Request)} if the birthday is not valid,
     * or with status {@code 500 (Internal Server Error)} if the birthday couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/birthdays/{id}")
    public ResponseEntity<Birthday> updateBirthday(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Birthday birthday
    ) throws URISyntaxException {
        log.debug("REST request to update Birthday : {}, {}", id, birthday);
        if (birthday.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, birthday.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!birthdayRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Birthday result = birthdayService.update(birthday);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, birthday.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /birthdays/:id} : Partial updates given fields of an existing birthday, field will ignore if it is null
     *
     * @param id the id of the birthday to save.
     * @param birthday the birthday to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated birthday,
     * or with status {@code 400 (Bad Request)} if the birthday is not valid,
     * or with status {@code 404 (Not Found)} if the birthday is not found,
     * or with status {@code 500 (Internal Server Error)} if the birthday couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/birthdays/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Birthday> partialUpdateBirthday(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Birthday birthday
    ) throws URISyntaxException {
        log.debug("REST request to partial update Birthday partially : {}, {}", id, birthday);
        if (birthday.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, birthday.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!birthdayRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Birthday> result = birthdayService.partialUpdate(birthday);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, birthday.getId().toString())
        );
    }

    /**
     * {@code GET  /birthdays} : get all the birthdays.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of birthdays in body.
     */
    @GetMapping("/birthdays")
    public List<Birthday> getAllBirthdays() {
        log.debug("REST request to get all Birthdays");
        return birthdayService.findAll();
    }

    /**
     * {@code GET  /birthdays/:id} : get the "id" birthday.
     *
     * @param id the id of the birthday to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the birthday, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/birthdays/{id}")
    public ResponseEntity<Birthday> getBirthday(@PathVariable Long id) {
        log.debug("REST request to get Birthday : {}", id);
        Optional<Birthday> birthday = birthdayService.findOne(id);
        return ResponseUtil.wrapOrNotFound(birthday);
    }

    /**
     * {@code DELETE  /birthdays/:id} : delete the "id" birthday.
     *
     * @param id the id of the birthday to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/birthdays/{id}")
    public ResponseEntity<Void> deleteBirthday(@PathVariable Long id) {
        log.debug("REST request to delete Birthday : {}", id);
        birthdayService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

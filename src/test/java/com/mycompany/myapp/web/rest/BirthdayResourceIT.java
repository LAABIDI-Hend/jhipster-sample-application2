package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Birthday;
import com.mycompany.myapp.repository.BirthdayRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link BirthdayResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BirthdayResourceIT {

    private static final String DEFAULT_DAY = "AAAAAAAAAA";
    private static final String UPDATED_DAY = "BBBBBBBBBB";

    private static final String DEFAULT_MONTH = "AAAAAAAAAA";
    private static final String UPDATED_MONTH = "BBBBBBBBBB";

    private static final String DEFAULT_YEAR = "AAAAAAAAAA";
    private static final String UPDATED_YEAR = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/birthdays";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BirthdayRepository birthdayRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBirthdayMockMvc;

    private Birthday birthday;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Birthday createEntity(EntityManager em) {
        Birthday birthday = new Birthday().day(DEFAULT_DAY).month(DEFAULT_MONTH).year(DEFAULT_YEAR);
        return birthday;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Birthday createUpdatedEntity(EntityManager em) {
        Birthday birthday = new Birthday().day(UPDATED_DAY).month(UPDATED_MONTH).year(UPDATED_YEAR);
        return birthday;
    }

    @BeforeEach
    public void initTest() {
        birthday = createEntity(em);
    }

    @Test
    @Transactional
    void createBirthday() throws Exception {
        int databaseSizeBeforeCreate = birthdayRepository.findAll().size();
        // Create the Birthday
        restBirthdayMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(birthday)))
            .andExpect(status().isCreated());

        // Validate the Birthday in the database
        List<Birthday> birthdayList = birthdayRepository.findAll();
        assertThat(birthdayList).hasSize(databaseSizeBeforeCreate + 1);
        Birthday testBirthday = birthdayList.get(birthdayList.size() - 1);
        assertThat(testBirthday.getDay()).isEqualTo(DEFAULT_DAY);
        assertThat(testBirthday.getMonth()).isEqualTo(DEFAULT_MONTH);
        assertThat(testBirthday.getYear()).isEqualTo(DEFAULT_YEAR);
    }

    @Test
    @Transactional
    void createBirthdayWithExistingId() throws Exception {
        // Create the Birthday with an existing ID
        birthday.setId(1L);

        int databaseSizeBeforeCreate = birthdayRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBirthdayMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(birthday)))
            .andExpect(status().isBadRequest());

        // Validate the Birthday in the database
        List<Birthday> birthdayList = birthdayRepository.findAll();
        assertThat(birthdayList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllBirthdays() throws Exception {
        // Initialize the database
        birthdayRepository.saveAndFlush(birthday);

        // Get all the birthdayList
        restBirthdayMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(birthday.getId().intValue())))
            .andExpect(jsonPath("$.[*].day").value(hasItem(DEFAULT_DAY)))
            .andExpect(jsonPath("$.[*].month").value(hasItem(DEFAULT_MONTH)))
            .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR)));
    }

    @Test
    @Transactional
    void getBirthday() throws Exception {
        // Initialize the database
        birthdayRepository.saveAndFlush(birthday);

        // Get the birthday
        restBirthdayMockMvc
            .perform(get(ENTITY_API_URL_ID, birthday.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(birthday.getId().intValue()))
            .andExpect(jsonPath("$.day").value(DEFAULT_DAY))
            .andExpect(jsonPath("$.month").value(DEFAULT_MONTH))
            .andExpect(jsonPath("$.year").value(DEFAULT_YEAR));
    }

    @Test
    @Transactional
    void getNonExistingBirthday() throws Exception {
        // Get the birthday
        restBirthdayMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBirthday() throws Exception {
        // Initialize the database
        birthdayRepository.saveAndFlush(birthday);

        int databaseSizeBeforeUpdate = birthdayRepository.findAll().size();

        // Update the birthday
        Birthday updatedBirthday = birthdayRepository.findById(birthday.getId()).get();
        // Disconnect from session so that the updates on updatedBirthday are not directly saved in db
        em.detach(updatedBirthday);
        updatedBirthday.day(UPDATED_DAY).month(UPDATED_MONTH).year(UPDATED_YEAR);

        restBirthdayMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBirthday.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedBirthday))
            )
            .andExpect(status().isOk());

        // Validate the Birthday in the database
        List<Birthday> birthdayList = birthdayRepository.findAll();
        assertThat(birthdayList).hasSize(databaseSizeBeforeUpdate);
        Birthday testBirthday = birthdayList.get(birthdayList.size() - 1);
        assertThat(testBirthday.getDay()).isEqualTo(UPDATED_DAY);
        assertThat(testBirthday.getMonth()).isEqualTo(UPDATED_MONTH);
        assertThat(testBirthday.getYear()).isEqualTo(UPDATED_YEAR);
    }

    @Test
    @Transactional
    void putNonExistingBirthday() throws Exception {
        int databaseSizeBeforeUpdate = birthdayRepository.findAll().size();
        birthday.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBirthdayMockMvc
            .perform(
                put(ENTITY_API_URL_ID, birthday.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(birthday))
            )
            .andExpect(status().isBadRequest());

        // Validate the Birthday in the database
        List<Birthday> birthdayList = birthdayRepository.findAll();
        assertThat(birthdayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBirthday() throws Exception {
        int databaseSizeBeforeUpdate = birthdayRepository.findAll().size();
        birthday.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBirthdayMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(birthday))
            )
            .andExpect(status().isBadRequest());

        // Validate the Birthday in the database
        List<Birthday> birthdayList = birthdayRepository.findAll();
        assertThat(birthdayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBirthday() throws Exception {
        int databaseSizeBeforeUpdate = birthdayRepository.findAll().size();
        birthday.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBirthdayMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(birthday)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Birthday in the database
        List<Birthday> birthdayList = birthdayRepository.findAll();
        assertThat(birthdayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBirthdayWithPatch() throws Exception {
        // Initialize the database
        birthdayRepository.saveAndFlush(birthday);

        int databaseSizeBeforeUpdate = birthdayRepository.findAll().size();

        // Update the birthday using partial update
        Birthday partialUpdatedBirthday = new Birthday();
        partialUpdatedBirthday.setId(birthday.getId());

        partialUpdatedBirthday.day(UPDATED_DAY).month(UPDATED_MONTH).year(UPDATED_YEAR);

        restBirthdayMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBirthday.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBirthday))
            )
            .andExpect(status().isOk());

        // Validate the Birthday in the database
        List<Birthday> birthdayList = birthdayRepository.findAll();
        assertThat(birthdayList).hasSize(databaseSizeBeforeUpdate);
        Birthday testBirthday = birthdayList.get(birthdayList.size() - 1);
        assertThat(testBirthday.getDay()).isEqualTo(UPDATED_DAY);
        assertThat(testBirthday.getMonth()).isEqualTo(UPDATED_MONTH);
        assertThat(testBirthday.getYear()).isEqualTo(UPDATED_YEAR);
    }

    @Test
    @Transactional
    void fullUpdateBirthdayWithPatch() throws Exception {
        // Initialize the database
        birthdayRepository.saveAndFlush(birthday);

        int databaseSizeBeforeUpdate = birthdayRepository.findAll().size();

        // Update the birthday using partial update
        Birthday partialUpdatedBirthday = new Birthday();
        partialUpdatedBirthday.setId(birthday.getId());

        partialUpdatedBirthday.day(UPDATED_DAY).month(UPDATED_MONTH).year(UPDATED_YEAR);

        restBirthdayMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBirthday.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBirthday))
            )
            .andExpect(status().isOk());

        // Validate the Birthday in the database
        List<Birthday> birthdayList = birthdayRepository.findAll();
        assertThat(birthdayList).hasSize(databaseSizeBeforeUpdate);
        Birthday testBirthday = birthdayList.get(birthdayList.size() - 1);
        assertThat(testBirthday.getDay()).isEqualTo(UPDATED_DAY);
        assertThat(testBirthday.getMonth()).isEqualTo(UPDATED_MONTH);
        assertThat(testBirthday.getYear()).isEqualTo(UPDATED_YEAR);
    }

    @Test
    @Transactional
    void patchNonExistingBirthday() throws Exception {
        int databaseSizeBeforeUpdate = birthdayRepository.findAll().size();
        birthday.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBirthdayMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, birthday.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(birthday))
            )
            .andExpect(status().isBadRequest());

        // Validate the Birthday in the database
        List<Birthday> birthdayList = birthdayRepository.findAll();
        assertThat(birthdayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBirthday() throws Exception {
        int databaseSizeBeforeUpdate = birthdayRepository.findAll().size();
        birthday.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBirthdayMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(birthday))
            )
            .andExpect(status().isBadRequest());

        // Validate the Birthday in the database
        List<Birthday> birthdayList = birthdayRepository.findAll();
        assertThat(birthdayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBirthday() throws Exception {
        int databaseSizeBeforeUpdate = birthdayRepository.findAll().size();
        birthday.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBirthdayMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(birthday)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Birthday in the database
        List<Birthday> birthdayList = birthdayRepository.findAll();
        assertThat(birthdayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBirthday() throws Exception {
        // Initialize the database
        birthdayRepository.saveAndFlush(birthday);

        int databaseSizeBeforeDelete = birthdayRepository.findAll().size();

        // Delete the birthday
        restBirthdayMockMvc
            .perform(delete(ENTITY_API_URL_ID, birthday.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Birthday> birthdayList = birthdayRepository.findAll();
        assertThat(birthdayList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

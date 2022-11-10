package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Birthday;
import com.mycompany.myapp.repository.BirthdayRepository;
import com.mycompany.myapp.service.BirthdayService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Birthday}.
 */
@Service
@Transactional
public class BirthdayServiceImpl implements BirthdayService {

    private final Logger log = LoggerFactory.getLogger(BirthdayServiceImpl.class);

    private final BirthdayRepository birthdayRepository;

    public BirthdayServiceImpl(BirthdayRepository birthdayRepository) {
        this.birthdayRepository = birthdayRepository;
    }

    @Override
    public Birthday save(Birthday birthday) {
        log.debug("Request to save Birthday : {}", birthday);
        return birthdayRepository.save(birthday);
    }

    @Override
    public Birthday update(Birthday birthday) {
        log.debug("Request to update Birthday : {}", birthday);
        return birthdayRepository.save(birthday);
    }

    @Override
    public Optional<Birthday> partialUpdate(Birthday birthday) {
        log.debug("Request to partially update Birthday : {}", birthday);

        return birthdayRepository
            .findById(birthday.getId())
            .map(existingBirthday -> {
                if (birthday.getDay() != null) {
                    existingBirthday.setDay(birthday.getDay());
                }
                if (birthday.getMonth() != null) {
                    existingBirthday.setMonth(birthday.getMonth());
                }
                if (birthday.getYear() != null) {
                    existingBirthday.setYear(birthday.getYear());
                }

                return existingBirthday;
            })
            .map(birthdayRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Birthday> findAll() {
        log.debug("Request to get all Birthdays");
        return birthdayRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Birthday> findOne(Long id) {
        log.debug("Request to get Birthday : {}", id);
        return birthdayRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Birthday : {}", id);
        birthdayRepository.deleteById(id);
    }
}

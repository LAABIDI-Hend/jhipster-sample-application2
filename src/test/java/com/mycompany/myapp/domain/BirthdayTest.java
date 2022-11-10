package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BirthdayTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Birthday.class);
        Birthday birthday1 = new Birthday();
        birthday1.setId(1L);
        Birthday birthday2 = new Birthday();
        birthday2.setId(birthday1.getId());
        assertThat(birthday1).isEqualTo(birthday2);
        birthday2.setId(2L);
        assertThat(birthday1).isNotEqualTo(birthday2);
        birthday1.setId(null);
        assertThat(birthday1).isNotEqualTo(birthday2);
    }
}

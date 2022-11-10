package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Birthday.
 */
@Entity
@Table(name = "birthday")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Birthday implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "day")
    private String day;

    @Column(name = "month")
    private String month;

    @Column(name = "year")
    private String year;

    @OneToMany(mappedBy = "birthday")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "jobs", "manager", "department", "birthday" }, allowSetters = true)
    private Set<Employee> employees = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Birthday id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDay() {
        return this.day;
    }

    public Birthday day(String day) {
        this.setDay(day);
        return this;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getMonth() {
        return this.month;
    }

    public Birthday month(String month) {
        this.setMonth(month);
        return this;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return this.year;
    }

    public Birthday year(String year) {
        this.setYear(year);
        return this;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public Set<Employee> getEmployees() {
        return this.employees;
    }

    public void setEmployees(Set<Employee> employees) {
        if (this.employees != null) {
            this.employees.forEach(i -> i.setBirthday(null));
        }
        if (employees != null) {
            employees.forEach(i -> i.setBirthday(this));
        }
        this.employees = employees;
    }

    public Birthday employees(Set<Employee> employees) {
        this.setEmployees(employees);
        return this;
    }

    public Birthday addEmployee(Employee employee) {
        this.employees.add(employee);
        employee.setBirthday(this);
        return this;
    }

    public Birthday removeEmployee(Employee employee) {
        this.employees.remove(employee);
        employee.setBirthday(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Birthday)) {
            return false;
        }
        return id != null && id.equals(((Birthday) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Birthday{" +
            "id=" + getId() +
            ", day='" + getDay() + "'" +
            ", month='" + getMonth() + "'" +
            ", year='" + getYear() + "'" +
            "}";
    }
}

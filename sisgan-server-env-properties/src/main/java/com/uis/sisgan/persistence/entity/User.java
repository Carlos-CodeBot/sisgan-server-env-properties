package com.uis.sisgan.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import java.time.LocalDate;
import org.hibernate.annotations.DiscriminatorFormula;

@Entity
@Table(name = "USERS")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorFormula("CASE "
        + "WHEN id_card_ica IS NOT NULL THEN 'IcaOfficial' "
        + "WHEN id_card_ica IS NULL AND id_driving_license IS NOT NULL THEN 'Transporter' "
        + "ELSE 'Propietary' END")
public abstract class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "id_card", length = 20)
    private String idCard;

    @Column(name = "first_name", length = 100)
    private String firstName;

    @Column(name = "last_name", length = 100)
    private String lastName;

    @Column(name = "password", length = 200)
    private String password;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "email", length = 100)
    private String email;

    public Integer getId() {
        return id;
    }

    /**
     * Cambia el identificador del usuario
     *
     * @param id Número que identifique el usuario.
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Retorna el identificador del usuario.
     *
     * @return Identificador del usuario.
     */
    public String getIdCard() {
        return idCard;
    }

    /**
     * Cambia el identificador de la tarjeta de identificación.
     *
     * @param idCard Identificador de la tarjeta de identificación.
     */
    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
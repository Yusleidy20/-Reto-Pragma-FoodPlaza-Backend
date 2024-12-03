package com.example.foodplaza_users.infrastructure.out.jpa.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    @Column(name = "nameUser")
    private String nameUser;
    @Column(name = "lastname")
    private String lastname;
    @Column(name = "docId",unique = true)
    private Long docId;
    @Column(name = "phoneNumber")
    private String phoneNumber;
    @Column(name = "birthDate", nullable = false)
    private LocalDate birthDate;
    @Column(name = "email")
    private String email;
    @Column(name = "passwordUser")
    private String passwordUser;
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "idUserRole", referencedColumnName = "idUserRole", nullable = false)
    private RoleEntity roleModel;

    public String toString() {
        return "UserEntity{" +
                "userId=" + userId +
                ", nameUser='" + nameUser + '\'' +
                ", lastname='" + lastname + '\'' +
                ", docId=" + docId +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", passwordUser='" + passwordUser + '\'' +
                ", roleModel=" + roleModel +
                '}';
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Long getDocId() {
        return docId;
    }

    public void setDocId(Long docId) {
        this.docId = docId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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

    public String getPasswordUser() {
        return passwordUser;
    }

    public void setPasswordUser(String passwordUser) {
        this.passwordUser = passwordUser;
    }

    public RoleEntity getRoleModel() {
        return roleModel;
    }

    public void setRoleModel(RoleEntity roleModel) {
        this.roleModel = roleModel;
    }
}

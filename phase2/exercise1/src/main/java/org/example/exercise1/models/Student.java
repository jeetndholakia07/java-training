package org.example.exercise1.models;

import jakarta.persistence.*;
import org.example.exercise1.utils.Status;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "student")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "middle_name")
    private String middleName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "full_name")
    private String fullName;
    @Column(name = "birth_date")
    private Date birthDate;
    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 1)
    private Status status;
    @Column(name = "creation_timestamp")
    @CreationTimestamp(source = SourceType.DB)
    private Timestamp creationTimestamp;
    @UpdateTimestamp(source = SourceType.DB)
    @Column(name = "last_updated_timestamp")
    private Timestamp lastUpdatedTimestamp;
    @ManyToMany
    @JoinTable(
            name = "student_course",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private List<Course> courses;

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFullName() {
        return fullName;
    }

    public Status getStatus() {
        return status;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public Timestamp getCreationTimestamp() {
        return creationTimestamp;
    }

    public Timestamp getLastUpdatedTimestamp() {
        return lastUpdatedTimestamp;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }
}

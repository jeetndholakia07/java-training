package org.example.exercise1.models;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "course")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "course_name")
    private String courseName;
    @CreationTimestamp(source = SourceType.DB)
    @Column(name = "creation_timestamp")
    private Timestamp creationTimestamp;
    @UpdateTimestamp(source = SourceType.DB)
    @Column(name = "last_updated_timestamp")
    private Timestamp lastUpdatedTimestamp;
    @ManyToMany(mappedBy = "courses")
    private List<Student> students;

    public int getId() {
        return id;
    }

    public String getCourseName() {
        return courseName;
    }

    public Timestamp getCreationTimestamp() {
        return creationTimestamp;
    }

    public Timestamp getLastUpdatedTimestamp() {
        return lastUpdatedTimestamp;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }
}

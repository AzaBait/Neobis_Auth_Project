package com.neobis.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "courses")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "serial")
    private Long id;
    @ManyToMany
    @JoinTable(
            name = "user_course",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> users = new ArrayList<>();
    private String name;
    private String description;
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<Lesson> lessonList = new ArrayList<>();

    public Course(List<User> users, String name, String description, List<Lesson> lessonList) {
        this.users = users;
        this.name = name;
        this.description = description;
        this.lessonList = lessonList;
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", users=" + users +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", lessonList=" + lessonList +
                '}';
    }
}

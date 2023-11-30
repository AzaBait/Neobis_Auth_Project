package com.neobis.demo.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "serial")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;
    private String name;
    private String description;

    public Lesson(Course course,String name, String description) {
        this.course = course;
        this.name = name;
        this.description = description;
    }
}

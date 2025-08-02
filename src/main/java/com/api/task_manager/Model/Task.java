package com.api.task_manager.Model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.*;

@Data
@Entity
@Table(name = "task")
public class Task {
    @Id
    @GeneratedValue
    Long id;

    LocalDate date;

    String taskName;

    boolean comp;

    public Task() {}

    //public Task(String taskName, LocalDate date, boolean comp) {}
}

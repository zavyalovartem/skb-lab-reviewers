package ru.codereviewers.codereviewersforskblab;

import lombok.Data;
import lombok.NonNull;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class Reviewers {
    @Id
    private Long userId; //??username??
    @NonNull
    private Status status;


    public Reviewers() {

    }
}
enum Status {
    OVERBURDENED,
    AVAILABLE,
    NOT_AVAILABLE,
    ON_VACATION //??? надо ли вообще
}
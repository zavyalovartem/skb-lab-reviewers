package ru.codereviewers.codereviewersforskblab;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ReviewersRepository extends CrudRepository<Reviewers, String> {

}

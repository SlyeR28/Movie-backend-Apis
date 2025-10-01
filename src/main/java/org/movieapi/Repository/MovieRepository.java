package org.movieapi.Repository;

import org.movieapi.Entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie , Integer> {

}

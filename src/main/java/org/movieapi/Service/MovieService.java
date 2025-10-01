package org.movieapi.Service;

import org.movieapi.Payloads.Request.MovieRequest;
import org.movieapi.Payloads.Response.MoviePageResponse;
import org.movieapi.Payloads.Response.MovieResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface MovieService {
    MovieResponse addMovie(MovieRequest movieRequest ,  MultipartFile file) throws IOException;
    MovieResponse getMovie(Integer movieId) throws FileNotFoundException;
    List<MovieResponse> getMovies();
    MovieResponse updateMovie(Integer movieId, MultipartFile file ,MovieRequest movieRequest) throws IOException;
    void deleteMovie(Integer movieId) throws IOException;
    MoviePageResponse getAllMoviesWithPagination(Integer pageNumber, Integer pageSize) throws IOException;
    MoviePageResponse getAllMoviesWithPaginationAndSorting(Integer pageNumber, Integer pageSize ,
                                                           String sortBy , String dir) throws IOException;

}

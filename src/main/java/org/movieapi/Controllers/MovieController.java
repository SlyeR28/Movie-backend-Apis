package org.movieapi.Controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.movieapi.Exceptions.EmptyFileException;
import org.movieapi.Payloads.Request.MovieRequest;
import org.movieapi.Payloads.Response.ApiResponse;
import org.movieapi.Payloads.Response.MoviePageResponse;
import org.movieapi.Payloads.Response.MovieResponse;
import org.movieapi.Service.MovieService;
import org.movieapi.utils.AppConstant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/movie")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    @PostMapping("/add")
    public ResponseEntity<MovieResponse> addMovieHandler(@Valid @RequestPart MultipartFile file ,
                                                          @RequestPart String movieRequest) throws IOException {
        if (file.isEmpty()) {
            throw  new EmptyFileException("The file is empty");
        }
        MovieRequest request = convertMovieRequestToMovieRequest(movieRequest);
        return new ResponseEntity<>(movieService.addMovie(request, file) , HttpStatus.CREATED);

    }

    @GetMapping("/{movieId}")
    public ResponseEntity<MovieResponse> getMovieHandler(@PathVariable Integer movieId) throws FileNotFoundException {
        MovieResponse movie = movieService.getMovie(movieId);
        return new ResponseEntity<>(movie, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<MovieResponse>> getAllMoviesHandler() throws FileNotFoundException {
        List<MovieResponse> movies = movieService.getMovies();
        return new ResponseEntity<>(movies, HttpStatus.OK);
    }


    @PutMapping("/update/{movieId}")
    public ResponseEntity<MovieResponse> updateMovieHandler(@Valid  @PathVariable Integer movieId,
                                                            @RequestPart MultipartFile file ,
                                                            @RequestPart("movieRequest") MovieRequest movieRequest)
            throws IOException {
        MovieResponse updatedMovie = movieService.updateMovie(movieId, file, movieRequest);
        return new ResponseEntity<>(updatedMovie, HttpStatus.OK);

    }

    @DeleteMapping("/{movieId}")
    public ResponseEntity<ApiResponse> deleteMovieHandler(@PathVariable Integer movieId) throws IOException {
        movieService.deleteMovie(movieId);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage("Movie has been deleted with id " + movieId);

        return new ResponseEntity<>( apiResponse,HttpStatus.OK);
    }

    @GetMapping("/allMoviePage")
    public ResponseEntity<MoviePageResponse> getAllMoviePageHandler(
            @RequestParam(defaultValue = AppConstant.PAGE_NUMBER , required = false) Integer pageNumber,
            @RequestParam(defaultValue = AppConstant.PAGE_SIZE , required = false) Integer pageSize

    ) throws IOException {
    return ResponseEntity.ok(movieService.getAllMoviesWithPagination(pageNumber, pageSize));
    }

    @GetMapping("/allMoviePageSort")
    public ResponseEntity<MoviePageResponse> getAllMoviePageSort(
            @RequestParam(defaultValue = AppConstant.PAGE_NUMBER , required = false) Integer pageNumber,
            @RequestParam(defaultValue = AppConstant.PAGE_SIZE , required = false) Integer pageSize,
            @RequestParam(defaultValue = AppConstant.SORT_BY , required = false) String sortBy,
            @RequestParam(defaultValue = AppConstant.SORT_DIR, required = false) String sortDir
    ) throws IOException {
        return ResponseEntity.ok(movieService.getAllMoviesWithPaginationAndSorting(pageNumber ,pageSize , sortBy ,sortDir));
    }


   // converting file to Json object
    private MovieRequest convertMovieRequestToMovieRequest(String movieRequestObj) throws JsonProcessingException {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(movieRequestObj, MovieRequest.class);
    }

}

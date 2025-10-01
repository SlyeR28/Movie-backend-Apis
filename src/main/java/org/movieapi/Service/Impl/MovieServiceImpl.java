package org.movieapi.Service.Impl;

import lombok.RequiredArgsConstructor;
import org.movieapi.Entity.Movie;
import org.movieapi.Exceptions.FileExistsException;
import org.movieapi.Exceptions.MovieNotFoundException;
import org.movieapi.Mappers.MovieMapper;
import org.movieapi.Payloads.Request.MovieRequest;
import org.movieapi.Payloads.Response.MoviePageResponse;
import org.movieapi.Payloads.Response.MovieResponse;
import org.movieapi.Repository.MovieRepository;
import org.movieapi.Service.FileService;
import org.movieapi.Service.MovieService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl  implements MovieService {

    private final MovieRepository movieRepository;
    private final FileService fileService;
    private final MovieMapper movieMapper;


    @Value("${project.poster}")
    private String path;

    @Value("${base.url}")
    private String baseUrl;


    @Override
    public MovieResponse addMovie(MovieRequest movieRequest, MultipartFile file) throws IOException {
        // 1. upload the file
        if(Files.exists(Paths.get(path + File.separator + file.getOriginalFilename()))){
            throw new FileExistsException("file already exists with filename" +file.getOriginalFilename());
        }
        String uploadedFileName = fileService.uploadFile(path, file);

        //2. set the value of field 'poster' as filename
        movieRequest.setPoster(uploadedFileName);

        //3. map request to entity
        Movie entity = movieMapper.toEntity(movieRequest);

        //4. save the entity
        Movie savedMovie = movieRepository.save(entity);
        //5. generate the poster url
        String posterUrl = baseUrl + "/file/" + uploadedFileName;

        //6. map movie entity to response and return it
        MovieResponse movieResponse = movieMapper.toResponse(savedMovie);
        movieResponse.setPoster(savedMovie.getPoster());
        movieResponse.setPosterUrl(posterUrl);
        return movieResponse;
    }

    @Override
    public MovieResponse getMovie(Integer movieId) throws FileNotFoundException {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() ->  new MovieNotFoundException("Movie not found by given id " + movieId));
       String posterUrl = baseUrl + "/file/" + movie.getPoster();
        MovieResponse response = movieMapper.toResponse(movie);
          response.setPosterUrl(posterUrl);
        return response;
     }

    @Override
    public List<MovieResponse> getMovies() {
        List<Movie> movieList = movieRepository.findAll();
        return movieList.stream().map(movie ->{
            MovieResponse movieResponse = movieMapper.toResponse(movie);
            movieResponse.setPosterUrl(baseUrl + "/file/" + movie.getPoster());
            return movieResponse;
                })
                .collect(Collectors.toList());
    }

    @Override
    public MovieResponse updateMovie(Integer movieId,MultipartFile file ,MovieRequest movieRequest ) throws IOException {
         Movie movie = movieRepository.findById(movieId)
                 .orElseThrow(() -> new MovieNotFoundException("Movie not found by given id " + movieId));
        movie.setTitle(movieRequest.getTitle());
        movie.setDirector(movieRequest.getDirector());
        movie.setStudio(movieRequest.getStudio());
        movie.setMovieCast(movieRequest.getMovieCast());
        movie.setReleaseYear(movieRequest.getReleaseYear());


        // 3️⃣ Upload new poster if file is provided
        if (file != null && !file.isEmpty()) {
            fileService.deleteFile(path , movie.getPoster());
            String uploadedFileName = fileService.uploadFile(path, file);
            movie.setPoster(uploadedFileName);
        }

        // 4️⃣ Save updated movie
        Movie saved = movieRepository.save(movie);

        // 5️⃣ Generate full poster URL
        String posterUrl = baseUrl + "/file/" + saved.getPoster();

        // 6️⃣ Map entity → response and set posterUrl
        MovieResponse response = movieMapper.toResponse(saved);
        response.setPosterUrl(posterUrl);
        return response;
    }

    @Override
    public void deleteMovie(Integer movieId) throws IOException {
         Movie movie = movieRepository.findById(movieId)
                 .orElseThrow(() -> new MovieNotFoundException("Movie not found by given id " + movieId));
        // 2️⃣ Delete poster file from server (optional)
        if (movie.getPoster() != null && !movie.getPoster().isEmpty()) {
            fileService.deleteFile(path, movie.getPoster());
        }
         movieRepository.delete(movie);
    }

    @Override
    public MoviePageResponse getAllMoviesWithPagination(Integer pageNumber, Integer pageSize) throws IOException {
        Pageable pageable = PageRequest.of(pageNumber , pageSize);
        return getMoviePageResponse(pageNumber, pageSize, pageable);

    }

    @Override
    public MoviePageResponse getAllMoviesWithPaginationAndSorting(Integer pageNumber, Integer pageSize, String sortBy, String dir) throws IOException {
        Sort sort = dir.equalsIgnoreCase("asc")?Sort.by(sortBy).ascending():Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber , pageSize , sort);
        return getMoviePageResponse(pageNumber, pageSize, pageable);

    }

    private MoviePageResponse getMoviePageResponse(Integer pageNumber, Integer pageSize, Pageable pageable) {
        Page<Movie> moviePage = movieRepository.findAll(pageable);
        List<Movie> movieList = moviePage.getContent();
        List<MovieResponse> collectedMovies = movieList.stream().map(movie -> {
                    MovieResponse movieResponse = movieMapper.toResponse(movie);
                    movieResponse.setPosterUrl(baseUrl + "/file/" + movie.getPoster());
                    return movieResponse;
                })
                .collect(Collectors.toList());

        return new MoviePageResponse(collectedMovies , pageNumber ,pageSize
                , moviePage.getTotalElements(),moviePage.getSize() ,moviePage.isLast());
    }
}

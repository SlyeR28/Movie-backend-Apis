    package org.movieapi.Mappers;

    import org.mapstruct.Mapper;
    import org.movieapi.Entity.Movie;
    import org.movieapi.Payloads.Request.MovieRequest;
    import org.movieapi.Payloads.Response.MovieResponse;

    @Mapper(componentModel = "spring")
    public interface MovieMapper {

        Movie toEntity(MovieRequest request);

        // Entity → Response DTO
        MovieResponse toResponse(Movie entity);

    }

package org.movieapi.Payloads.Request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.util.Set;


@Data
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovieRequest
{
    @NotBlank(message = "Title is mandatory")
    @Size(max = 100)
    private String title;

    @NotBlank(message = "Director is mandatory")
    @Size(max = 50)
    private String director;

    @NotBlank(message = "Studio is mandatory")
    @Size(max = 50)
    private String studio;

    @NotEmpty(message = "Movie cast cannot be empty")
    private Set<@NotBlank(message = "Cast member cannot be blank") String> movieCast;

    @NotNull(message = "Release year is mandatory")
    @Min(value = 1888, message = "Release year cannot be before 1888")
    @Max(value = 2100, message = "Release year seems invalid")
    private Integer releaseYear;

    private String poster;
    private String posterUrl;

}

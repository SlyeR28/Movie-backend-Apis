package org.movieapi.Payloads.Response;

import lombok.*;

import java.util.Set;

@Data
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovieResponse {
    private Integer id;
    private String title;
    private String director;
    private String studio;
    private Set<String> movieCast;
    private Integer releaseYear;
    private String poster;
    private String posterUrl;
}

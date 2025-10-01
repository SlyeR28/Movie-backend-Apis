package org.movieapi.Payloads.Response;

import java.util.List;

public record MoviePageResponse(List<MovieResponse> movieResponseList ,
                                Integer pageNumber ,
                                Integer pageSize , Long totalElements ,
                                int totalPages
        , Boolean isLast
) {
}

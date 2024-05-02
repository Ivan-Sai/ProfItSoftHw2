package internature.hw2.service;

import internature.hw2.entity.Genre;

import java.util.List;

public interface GenreService {
    List<Genre> getGenresFromNameList(List<String> genres);
}

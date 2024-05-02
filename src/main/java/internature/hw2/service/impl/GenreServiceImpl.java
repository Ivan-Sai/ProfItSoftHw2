package internature.hw2.service.impl;

import internature.hw2.entity.Genre;
import internature.hw2.repository.GenreRepository;
import internature.hw2.service.GenreService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.text.WordUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;
    @Override
    public List<Genre> getGenresFromNameList(List<String> genres) {
        List<Genre> result = new ArrayList<>();
        for (String genre : genres) {
            result.add(genreRepository.findByName(StringUtils.capitalize(genre.toLowerCase())).orElseGet(() -> {
                Genre newGenre = new Genre();
                newGenre.setName(StringUtils.capitalize(genre));
                return genreRepository.save(newGenre);
            }));
        }
        return result;
    }


}

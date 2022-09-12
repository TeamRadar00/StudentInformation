package com.studentinformation.web.converter;


import com.studentinformation.domain.Score;
import org.springframework.core.convert.converter.Converter;

import java.util.ArrayList;
import java.util.List;

public class ScoreRequestConverter implements Converter<String, List<Score>> {

    @Override
    public List<Score> convert(String source) {
        return Score.of(source);
    }
}

package br.com.leandro.crud.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

public class Objects {

    @SneakyThrows
    public static <T, R> R copy (T origin, Class<R> classe) {
        ObjectMapper mapper = new ObjectMapper();
        String jsonObject = mapper.writeValueAsString(origin);
        return mapper.readValue(jsonObject, classe);
    }
}

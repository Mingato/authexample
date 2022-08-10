package com.redcompany.receita.infra.json;

import java.io.IOException;
import java.io.StringWriter;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonConverter {

    private final static Logger LOGGER = Logger.getLogger(JsonConverter.class);

    public static <T> T convertFromString(Class<T> clazz, String data) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            T objeto = mapper.readValue(data.getBytes(), clazz);
            return objeto;
        } catch (IOException e) {
            LOGGER.error(String.format("Error on JSON parse %s", data));
            LOGGER.error(e);
            throw new RuntimeException(String.format("Error on Json parse %s", data));
        }
    }

    @SuppressWarnings("deprecation")
	public static String createJson(Object object){
        try {
            StringWriter sw = new StringWriter();
            ObjectMapper mapper = new ObjectMapper();
            MappingJsonFactory jsonFactory = new MappingJsonFactory();
            JsonGenerator jsonGenerator;
            jsonGenerator = jsonFactory.createJsonGenerator(sw);
            mapper.writeValue(jsonGenerator, object);
            return sw.getBuffer().toString();
        } catch (IOException e) {
            LOGGER.error("Error on Json parse: ", e);
            throw new RuntimeException(e);
        }
    }

}

package love.sola.netsupport.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;

import org.junit.Test;

import java.util.Date;

import love.sola.netsupport.enums.ISP;

/**
 * @author Sola {@literal <dev@sola.love>}
 */
public class GsonTest {

    @Test
    public void testJsonDate() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, (JsonDeserializer<Date>) (json, typeOfT, context) -> new Date(json.getAsJsonPrimitive().getAsLong()))
                .registerTypeAdapter(Date.class, (JsonSerializer<Date>) (src, typeOfSrc, context) -> new JsonPrimitive(src.getTime()))
                .registerTypeAdapter(ISP.class, (JsonDeserializer<ISP>) (json, typeOfT, context) -> ISP.fromId(json.getAsJsonPrimitive().getAsInt()))
                .registerTypeAdapter(ISP.class, (JsonSerializer<ISP>) (src, typeOfSrc, context) -> new JsonPrimitive(src.id))
                .create();
        Date date = new Date();
        assert gson.fromJson(gson.toJson(date), Date.class).compareTo(date) == 0;
        assert gson.fromJson(gson.toJson(ISP.TELECOM), ISP.class) == ISP.TELECOM;
        assert gson.toJson(ISP.TELECOM).equals("1");
    }

}

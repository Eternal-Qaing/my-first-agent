package com.qiang.myfirstagent.tools;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;

/**
 * 查天气的工具类
 * @author qiang
 * @since 2026/10/10 19:58
 *
 */
@Slf4j
@Component
public class WeatherTools {
    private final RestClient restClient;
    private final ObjectMapper objectMapper;
    @Value("${amap.api-key}")
    private String apiKey;
    private static final Map<String,String>CITY_CODES = Map.of(
      "北京","110000",
            "杭州", "330100",
            "上海", "310000",
            "广州", "440100",
            "深圳", "440300"
    );

    static final String MSG_UNSUPPORTED_CITY = "抱歉，暂时只支持查询北京、杭州、上海、广州、深圳的天气。";
    static final String MSG_EMPTY_RESPONSE = "高德 API 暂时没有返回该城市的天气数据。";
    static final String MSG_API_ERROR = "调用天气 API 时发生错误，请稍后重试。";

    public WeatherTools() {
        this.restClient = RestClient.create();
        this.objectMapper = new ObjectMapper();
    }

    @Tool(description = "获取指定城市的实时天气信息,传入城市中文名称即可, 如: 杭州、北京。")
    public String getWeather(@ToolParam(description = "城市名称，比如杭州、北京") String city){
        log.info("Agent 正在调用天气工具 | city={}", city);


        String cityCode = resolveCityCode(city);
        if(cityCode == null){
            return MSG_UNSUPPORTED_CITY;
        }

        try {
            String response = fetchWeatherFromAmap(cityCode);
            return parseWeatherResponse(city,response);
        }catch (Exception e){
            log.error("调用高德天气 API 失败 | city={}", city, e);
            return MSG_API_ERROR;
        }
    }

    /**
     * 把城市名解析成高德 adcode. 包级私有，方便测试
     * @param city 城市
     * @return 城市代码
     */
    String resolveCityCode(String city){
        if (city == null){
            return null;
        }
        return CITY_CODES.get(city.trim());
    }

    /**
     * 调用高德 API 拿原始 JSON。包级私有，方便子类覆写或 mock。
     */
    String fetchWeatherFromAmap(String adcode){
        String url = "https://restapi.amap.com/v3/weather/weatherInfo?key=" + apiKey+ "&city=" + adcode;
        return  restClient.get()
                .uri(url)
                .retrieve()
                .body(String.class);
    }
    /**
     * 解析高德返回的 JSON，转成自然语言。包级私有，方便测试。
     */
    String parseWeatherResponse(String city, String response){
        JsonNode root = objectMapper.readTree(response);
        JsonNode lives = root.path("lives").get(0);
        if (lives == null){
            log.warn("高德 API 未返回天气数据 | city={}", city);
            return MSG_EMPTY_RESPONSE;
        }
        String weather = lives.path("weather").asString();
        String temperature = lives.path("temperature").asString();
        String windDirection = lives.path("winddirection").asString();
        String windPower = lives.path("windpower").asString();

        String result = String.format("%s今天天气%s,气温%s度, %s风%s级。", city, weather, temperature, windDirection, windPower);
        log.info("天气查询成功 | city={} | result={}", city, result);
        return result;
    }




}

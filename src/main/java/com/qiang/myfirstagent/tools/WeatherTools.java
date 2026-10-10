package com.qiang.myfirstagent.tools;

import lombok.RequiredArgsConstructor;
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
    public WeatherTools() {
        this.restClient = RestClient.create();
        this.objectMapper = new ObjectMapper();
    }

    @Tool(description = "获取指定城市的实时天气信息")
    public String getWeather(@ToolParam(description = "城市名称，比如杭州、北京") String city){
        System.out.println("【系统日志】Agent 正在调用天气工具，查询城市：" + city);

        String cityCode = CITY_CODES.getOrDefault(city,null);
        if(cityCode == null){
            return "抱歉，暂时只支持查询北京、杭州、上海、广州、深圳的天气。";
        }
        try {
            String url ="https://restapi.amap.com/v3/weather/weatherInfo?key=" + apiKey+ "&city=" + cityCode;
            String response = restClient.get()
                    .uri(url)
                    .retrieve()
                    .body(String.class);
            JsonNode root = objectMapper.readTree(response);
            JsonNode lives = root.path("lives").get(0);
            if (lives == null){
                return "高德 API 暂时没有返回该城市的天气数据。";
            }
            String weather = lives.path("weather").asString();
            String temperature = lives.path("temperature").asString();
            String windDirection = lives.path("winddirection").asString();
            String windPower = lives.path("windpower").asString();
            return String.format("%s今天天气%s，气温%s度，%s风%s级。",
                    city, weather, temperature, windDirection, windPower);
        }catch (Exception e){
            e.printStackTrace();
            return "调用天气API时发生错误: " + e.getMessage();
        }
    }
}

package com.qiang.myfirstagent.tools;



import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;
/**
 *
 * @author qiang
 * @since 2026/10/13 00:06
 */
class WeatherToolsTest {

    private final WeatherTools weatherTools = new WeatherTools();

    // ========== resolveCityCode 测试 ==========

    @Test
    @DisplayName("5 个支持的城市都能解析出正确的 adcode")
    void resolveCityCode_shouldReturnAdcodeForSupportedCities() {
        assertEquals("110000", weatherTools.resolveCityCode("北京"));
        assertEquals("330100", weatherTools.resolveCityCode("杭州"));
        assertEquals("310000", weatherTools.resolveCityCode("上海"));
        assertEquals("440100", weatherTools.resolveCityCode("广州"));
        assertEquals("440300", weatherTools.resolveCityCode("深圳"));
    }

    @Test
    @DisplayName("不支持的城市返回 null")
    void resolveCityCode_shouldReturnNullForUnsupportedCity() {
        assertNull(weatherTools.resolveCityCode("拉萨"));
        assertNull(weatherTools.resolveCityCode("纽约"));
        assertNull(weatherTools.resolveCityCode(""));
        assertNull(weatherTools.resolveCityCode(null));
    }

    @Test
    @DisplayName("城市名前后有空格也能正确解析")
    void resolveCityCode_shouldTrimWhitespace() {
        assertEquals("330100", weatherTools.resolveCityCode("  杭州  "));
    }

    // ========== parseWeatherResponse 测试 ==========

    @Test
    @DisplayName("正常 JSON 能正确解析出天气、温度、风向")
    void parseWeatherResponse_shouldParseValidResponse() throws Exception {
        String json = """
                {
                    "status": "1",
                    "count": "1",
                    "lives": [
                        {
                            "province": "浙江",
                            "city": "杭州市",
                            "weather": "晴",
                            "temperature": "25",
                            "winddirection": "西南",
                            "windpower": "≤3"
                        }
                    ]
                }
                """;

        String result = weatherTools.parseWeatherResponse("杭州", json);

        assertTrue(result.contains("杭州"));
        assertTrue(result.contains("晴"));
        assertTrue(result.contains("25"));
        assertTrue(result.contains("西南"));
    }

    @Test
    @DisplayName("lives 为空数组时返回友好提示")
    void parseWeatherResponse_shouldHandleEmptyLives() throws Exception {
        String json = """
                {
                    "status": "1",
                    "count": "0",
                    "lives": []
                }
                """;

        String result = weatherTools.parseWeatherResponse("杭州", json);

        assertEquals(WeatherTools.MSG_EMPTY_RESPONSE, result);
    }

    @Test
    @DisplayName("lives 字段缺失时返回友好提示")
    void parseWeatherResponse_shouldHandleMissingLives() throws Exception {
        String json = "{\"status\":\"1\",\"count\":\"0\"}";

        String result = weatherTools.parseWeatherResponse("杭州", json);

        assertEquals(WeatherTools.MSG_EMPTY_RESPONSE, result);
    }
}

package com.qiang.myfirstagent.tools;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

/**
 * 查天气的工具类
 * @author qiang
 * @since 2026/10/10 19:58
 *
 */
@Component
public class WeatherTools {
    @Tool(description = "获取指定城市的实时天气信息")
    public String getWeather(@ToolParam(description = "城市名称，比如杭州、北京") String city){
        System.out.println("【系统日志】Agent 正在调用天气工具，查询城市：" + city);

        // 这里我们用 if/else 模拟查询，后续你可以换成真实的 HTTP API 请求
        if (city.contains("杭州")) {
            return "杭州今天晴，气温 25 度，微风，非常适合写代码。";
        } else if (city.contains("北京")) {
            return "北京今天多云，气温 18 度，有轻度雾霾。";
        }
        return "抱歉，暂时没有【" + city + "】的天气数据。";
    }
}

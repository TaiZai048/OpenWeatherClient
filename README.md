# OpenWeather 客户端库

这是一个用于访问 OpenWeatherMap API 的 Java 客户端库。它提供了简单的方法来获取天气数据，并将其转换为 Java 对象。本客户端主要对接的是：One Call API 3.0，在使用本客户端之前，一个硬性需求是获得一个 OpenWeather API Key：[OpenWeather API Key](https://openweathermap.org/api)

## 功能特点

- 支持 OpenWeatherMap API 3.0
- 尽量少的使用三方库，提高兼容性
- 使用 LocalDateTime 处理时间戳
- 自动配置，易于集成到 Spring Boot 项目中
- 支持不同详细程度的天气数据（简化版、标准版和详细版）
- 支持选择性获取详细天气数据的组件（分钟预报、小时预报、每日预报和天气预警）
- 提供经纬度验证，确保输入数据有效
- 异常处理机制，提供清晰的错误信息
- **灵活的缓存机制**，支持 Redis 和本地缓存，提高性能并减少 API 调用
- **缓存击穿防护**，避免高并发场景下的性能问题

## 安装

在您的 Maven 项目中添加以下依赖：

```xml
<dependency>
    <groupId>cn.aetherial</groupId>
    <artifactId>openweather-client</artifactId>
    <version>0.1.2</version>
</dependency>
```

## 配置

在您的 `application.yaml` 或 `application.properties` 文件中添加以下配置：

```yaml
open-weather-config:
  api-key: "您的 OpenWeather API Key" # 必须设置，否则会报错
  units: metric # 温度统计单位 默认 metric
  lang: zh_cn # 语言 默认 zh_cn
  exclude: minutely,alerts # 排除的天气数据组件（可选，默认不排除任何组件），多个组件用逗号分隔
  connection-timeout: 5000 # 连接超时（毫秒）
  read-timeout: 5000 # 读取超时（毫秒）
  api-domain: "https://api.openweathermap.org" # API域名前缀（可选，默认：https://api.openweathermap.org）

  # 缓存配置
  cache:
    # 是否启用缓存（可选，默认：fasle）
    enabled: false

    # 缓存类型（可选，默认：auto）
    # 可选值：
    # - auto: 自动选择（优先使用 Redis，如果不可用则使用本地缓存）
    # - redis: 强制使用 Redis 缓存
    # - local: 强制使用本地缓存
    type: auto

    # 缓存过期时间（秒，可选，默认：600，印10分钟）
    expire-time: 600

    # 本地缓存最大条目数（可选，默认：100）
    max-size: 100

    # Redis 键前缀（可选，默认：openweather:）
    key-prefix: "openweather:"
```

### 代理配置

如果您需要通过代理服务器访问 OpenWeatherMap API，可以配置 API 域名前缀：

```yaml
open-weather-config:
  api-domain: "https://your-proxy-server.com"  # 配置代理服务器域名
```

```properties
# 或在 application.properties 中配置
open-weather-config.api-domain=https://your-proxy-server.com
```

代理服务器需要支持以下 API 路径：
- `/data/3.0/onecall` - 天气数据API
- `/geo/1.0/direct` - 地理编码API

### 自定义RestTemplate

本库支持自定义 RestTemplate 配置。如果您的项目中已经定义了 RestTemplate Bean，库会优先使用您的配置：

```java
@Configuration
public class MyConfiguration {
    
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplateBuilder()
            .setConnectTimeout(Duration.ofMillis(10000))
            .setReadTimeout(Duration.ofMillis(15000))
            .interceptors(new MyCustomInterceptor())  // 添加自定义拦截器
            .errorHandler(new MyErrorHandler())       // 自定义错误处理
            .build();
    }
}
```

如果您的项目中没有定义 RestTemplate，库会自动创建一个默认的 RestTemplate，使用配置文件中的超时设置。

如果使用 Redis 缓存，还需要配置 Redis 连接信息：

```yaml
# Redis 配置 SpringBoot2
spring:
  redis:
    host: localhost
    port: 6379
    # password: your_redis_password  # 如果需要密码
    # database: 0                    # Redis 数据库索引
```

```yaml
# Redis 配置 SpringBoot3
spring:
  data:
    redis:
      host: localhost
      port: 6379
      # password: your_redis_password  # 如果需要密码
      # database: 0
```

## 使用示例

### 方式一：依赖注入

```java
import cn.aetherial.openweather.entity.WeatherDetails;
import cn.aetherial.openweather.entity.WeatherSimple;
import cn.aetherial.openweather.service.OpenWeatherService;
import cn.aetherial.openweather.config.WeatherDetailsConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MyWeatherService {

    private final OpenWeatherService openWeatherService;

    @Autowired
    public MyWeatherService(OpenWeatherService openWeatherService) {
        this.openWeatherService = openWeatherService;
    }

    public void getWeatherInfo() {
        try {
            // 获取简化版天气数据
            WeatherSimple simpleWeather = openWeatherService.getSimpleWeatherByCoordinates(39.9042, 116.4074);
            System.out.println("当前温度: " + simpleWeather.getTemp());

            // 获取标准版天气数据
            WeatherStandard standardWeather = openWeatherService.getWeatherByCoordinates(39.9042, 116.4074);
            System.out.println("当前体感温度: " + standardWeather.getCurrent().getFeelsLike());

            // 获取完整的详细版天气数据
            WeatherDetails fullDetails = openWeatherService.getDetailedWeatherByCoordinates(39.9042, 116.4074);
            System.out.println("当前体感温度: " + fullDetails.getCurrent().getFeelsLike());

            // 获取选择性的详细版天气数据（只包含小时预报和每日预报）
            WeatherDetailsConfig config = WeatherDetailsConfig.create(false, true, true, false);
            WeatherDetails customDetails = openWeatherService.getDetailedWeatherByCoordinates(39.9042, 116.4074, config);
            System.out.println("未来24小时预报数量: " + customDetails.getHourly().size());
        } catch (Exception e) {
            System.err.println("获取天气数据失败: " + e.getMessage());
        }
    }
}
```

### 方式二：静态工具类（推荐）

```java
import cn.aetherial.openweather.entity.WeatherDetails;
import cn.aetherial.openweather.entity.WeatherSimple;
import cn.aetherial.openweather.utils.OpenWeatherUtils;
import cn.aetherial.openweather.config.WeatherDetailsConfig;

public class WeatherExample {

    public static void main(String[] args) {
        try {
            // 根据经纬度获取简化版天气数据
            WeatherSimple simpleWeather = OpenWeatherUtils.getSimpleWeatherByCoordinates(39.9042, 116.4074);
            System.out.println("当前温度: " + simpleWeather.getTemp());
            System.out.println("天气状况: " + simpleWeather.getWeatherDescription());

            // 经纬度获取标准版天气数据
            WeatherStandard standardWeather = OpenWeatherUtils.getWeather(39.9042, 116.4074, WeatherDetailLevel.STANDARD);
            System.out.println("当前体感温度: " + standardWeather.getCurrent().getFeelsLike());

            // 根据城市名称获取详细版天气数据
            WeatherDetails beijingWeather = OpenWeatherUtils.getDetailedWeatherByCity("北京");
            System.out.println("北京当前温度: " + beijingWeather.getCurrent().getTemp());
            System.out.println("北京日出时间: " + beijingWeather.getCurrent().getSunrise());

            // 根据城市名称和国家代码获取详细版天气数据
            WeatherDetails londonWeather = OpenWeatherUtils.getDetailedWeatherByCity("London", "GB");
            System.out.println("伦敦当前温度: " + londonWeather.getCurrent().getTemp());

            // 获取选择性的详细版天气数据（只包含每日预报和天气预警）
            WeatherDetailsConfig config = WeatherDetailsConfig.create(false, false, true, true);
            WeatherDetails customWeather = OpenWeatherUtils.getDetailedWeatherByCoordinates(39.9042, 116.4074, config);

            // 访问每日预报数据
            if (customWeather.getDaily() != null && !customWeather.getDaily().isEmpty()) {
                System.out.println("未来一周最高温度: " + customWeather.getDaily().get(0).getTempMax());
            }

            // 访问天气预警数据
            if (customWeather.getAlerts() != null && !customWeather.getAlerts().isEmpty()) {
                System.out.println("天气预警: " + customWeather.getAlerts().get(0).getEvent());
            } else {
                System.out.println("没有天气预警");
            }
        } catch (Exception e) {
            System.err.println("获取天气数据失败: " + e.getMessage());
        }
    }
}
```

## 异常处理

库会抛出 `OpenWeatherException` 异常，包含以下错误代码：

- `INVALID_COORDINATES`: 经纬度超出有效范围
- `API_REQUEST_FAILED`: API 请求失败，可能是网络问题或 API Key 无效
- `API_KEY_NOT_SET`: API Key 未设置
- `INVALID_CACHE_TYPE`: 缓存类型无效

异常处理示例：

```java
import cn.aetherial.openweather.entity.WeatherDetails;
import cn.aetherial.openweather.exception.OpenWeatherException;
import cn.aetherial.openweather.utils.OpenWeatherUtils;

public class ErrorHandlingExample {

    public static void main(String[] args) {
        try {
            // 测试无效的经纬度
            WeatherDetails weather = OpenWeatherUtils.getDetailedWeatherByCoordinates(200, 300);
        } catch (OpenWeatherException e) {
            System.err.println("错误代码: " + e.getErrorCode());
            System.err.println("错误信息: " + e.getMessage());
        }
    }
}
```

## 配置选项说明

### 温度单位

- `standard`: 开尔文温度 (K)
- `metric`: 摄氏度 (°C)
- `imperial`: 华氏度 (°F)

### 语言选项

支持多种语言，包括：

- `en`: 英语
- `zh_cn`: 简体中文
- `zh_tw`: 繁体中文
- `ja`: 日语
- `ko`: 韩语
- `ru`: 俄语
- `de`: 德语
- `fr`: 法语
- `es`: 西班牙语
- `it`: 意大利语
- `pt_br`: 巴西葡萄牙语

## 数据模型

### WeatherSimple

简化版天气数据，包含基本天气信息：

- `temp`: 当前温度
- `feelsLike`: 体感温度
- `humidity`: 湿度
- `main`: 天气主要状况
- `description`: 天气描述
- `iconUrl`: 天气图标地址

### WeatherStandard

标准版天气数据，包含更多信息：

- `dt`: 日期时间
- `pressure`: 气压
- `humidity`: 湿度
- `dewPoint`: 露点
- `uvi`: 紫外线强度
- `clouds`: 云量
- `visibility`: 可见度
- `windSpeed`: 风速
- `windGust`: 最大风速
- `windDeg`: 风向
- `sunrise`: 日出时间
- `sunset`: 日落时间
- `temp`: 当前温度
- `feelsLike`: 体感温度
- `main`: 天气主要状况
- `description`: 天气描述
- `iconUrl`: 天气图标地址

### WeatherDetails

详细版天气数据，包含更多信息，具体可以参考 OpenWeather API 文档: [One Call API 3.0](https://openweathermap.org/api/one-call-3)

## 缓存机制

本库提供了灵活的缓存机制，可以大幅提高应用性能并减少 API 调用次数。

### 缓存类型说明

#### Redis 缓存

优点：

- 支持分布式环境
- 可以在多个应用实例间共享缓存
- 支持更大的缓存容量
- 支持为每个缓存项单独设置过期时间

适用场景：

- 生产环境
- 多实例部署
- 需要大容量缓存

#### 本地缓存

优点：

- 无需额外服务
- 响应速度更快
- 配置简单

适用场景：

- 开发环境
- 单实例部署
- 对缓存一致性要求不高的场景

### 缓存击穿防护

本库实现了缓存击穿防护机制，旨在减轻热点数据过期时导致的大量并发请求直接访问后端服务的问题。该机制在理论上可以减少重复请求，但在实际高并发场景下的效果可能需要进一步测试和验证。

#### 防护机制

1. **互斥锁**：对于同一个缓存键，使用互斥锁确保只有一个线程去查询天气 API，其他线程等待结果。

2. **双重检查**：在获取锁之后再次检查缓存，避免多个线程重复从 API 获取相同数据。

3. **细粒度锁**：锁的粒度是按缓存键划分的，不同缓存键之间互不影响，最大化并发性能。

4. **资源清理**：请求完成后立即移除锁对象，防止内存泄漏。

### 缓存键生成规则

缓存键由以下因素组成：

- 经度和纬度
- 单位系统（metric、imperial 等）
- 语言设置

这确保了当这些参数发生变化时，会获取新的数据而不是使用缓存的数据。

## 依赖要求

### Redis 缓存依赖

如果使用 Redis 缓存，需要在您的项目中添加以下依赖：

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```

### 本地缓存依赖

本地缓存使用 Caffeine 库，请在您的项目中添加以下依赖：

```xml
<dependency>
    <groupId>com.github.ben-manes.caffeine</groupId>
    <artifactId>caffeine</artifactId>
    <version>3.0.5</version>
</dependency>
```

注意：如果使用 `auto` 模式的缓存方式，需要确保项目中同时存在 Redis 和 Caffeine 的依赖，否则会出现报错

### Redis 缓存兼容性

本库基于 Spring Boot 2.4.3 开发，但已经进行了优化，可以更灵活地适应不同的 Redis 配置。

#### 自动适配 RedisTemplate

从版本 0.1.1 开始，本库会自动适配项目中的 RedisTemplate Bean：

1. 首先尝试查找名为 `openWeatherRedisTemplate` 的 Bean
2. 如果找不到，则使用项目中任何可用的 RedisTemplate Bean

这意味着在大多数情况下，您不需要额外的配置，库会自动使用您项目中已有的 RedisTemplate。

#### 方式一：使用默认 RedisTemplate

如果您的项目已经配置了 Spring Data Redis，无需任何额外配置，本库会自动使用默认的 RedisTemplate。

#### 方式二：创建专用的 RedisTemplate Bean（可选）

如果您希望为本库配置专用的 RedisTemplate，可以创建一个名为 `openWeatherRedisTemplate` 的 Bean：

```java
@Configuration
public class OpenWeatherRedisConfig {

    @Bean
    public RedisTemplate<String, Object> openWeatherRedisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // 使用 StringRedisSerializer 来序列化和反序列化 Redis 的 key
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());

        // 使用 Jackson2JsonRedisSerializer 来序列化和反序列化 Redis 的 value
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());

        template.afterPropertiesSet();
        return template;
    }
}
```

#### 方式三：为现有的 RedisTemplate 添加别名（可选）

如果您已经有了自己的 RedisTemplate Bean，也可以为其添加 `openWeatherRedisTemplate` 别名：

```java
@Configuration
public class OpenWeatherRedisConfig {

    @Bean(name = "openWeatherRedisTemplate")
    public RedisTemplate<String, Object> existingRedisTemplate(@Qualifier("yourExistingRedisTemplate") RedisTemplate<String, Object> existingTemplate) {
        return existingTemplate;
    }
}
```

## 注意事项

- 必须设置有效的 OpenWeather API Key，否则会抛出异常
- 所有时间戳字段都使用 `LocalDateTime` 类型
- 实体类提供了重载的 setter 方法，可以接受 Unix 时间戳（Long 类型）并自动转换为 `LocalDateTime`
- 天气数据会随时间变化，请根据您的业务需求设置合理的缓存过期时间
- 如果强制指定使用 Redis 缓存但 Redis 不可用，系统将抛出异常
- 缓存键包含单位和语言设置，如果这些设置发生变化，将使用新的缓存键

## 许可证

MIT License

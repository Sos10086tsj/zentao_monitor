基于禅道+钉钉的简易监控系统，采用Springboot + Vue + ElementUI框架。
项目开箱即用，配置文件查看application.yml，需要自定义的配置参数如下：

spring:
  profiles: {{profileName}}
  datasource:
    url: {{db link url}}
    username: {{db username}}
    password: {{db password}}
    
app:
  zentao:
    baseUrl: {{zentao root url}}
    dailyReportUrl: {{zentao root url}}/html/DailyReport.html?id=
    dailyReportPicUrl: 
  dingtalk:
    accessToken: {{dingtalk robot accesstoken url}}
    
logging:
  config: classpath:logback-${spring.profiles.active}.xml
  home: {{log root folder}}/${spring.application.name}
  
  commom.js 修改 ctx 变量为实际项目路径

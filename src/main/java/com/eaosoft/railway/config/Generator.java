package com.eaosoft.railway.config;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.Collections;

public class Generator {
 public static void main(String[] args) {
     FastAutoGenerator.create("jdbc:mysql://localhost:3306/railway?serverTimezone=Asia/Shanghai", "root", "123456")
         .globalConfig(builder -> {
             builder.author("zzs") // 设置作者
                 .enableSwagger()// 开启 swagger 模式
                 .fileOverride() // 覆盖已生成文件
                 .outputDir(".\\src\\main\\java\\"); // 指定输出目录
         })
         .packageConfig(builder -> {
             builder.parent("com.eaosoft") // 设置父包名
                 .moduleName("railway") // 设置父包模块名
                 .pathInfo(Collections.singletonMap(OutputFile.xml, "src\\main\\resources\\mapper\\")); // 设置mapperXml生成路径
         })
         .strategyConfig(builder -> {
             builder.addInclude("rw_picture")// 设置需要生成的表名
                 .addTablePrefix("rw_"); // 设置过滤表前缀
         })
         .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
         .execute();
 }
}

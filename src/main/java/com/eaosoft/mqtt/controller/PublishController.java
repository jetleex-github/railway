package com.eaosoft.mqtt.controller;

import com.eaosoft.mqtt.MQTTServer;
import com.eaosoft.railway.utils.RespValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
 
@RestController
@RequestMapping(value = "/railway/publish")
public class PublishController {
 
    @Autowired
    private MQTTServer mqttserver;

    /**
     * 发布内容
     * @param topic
     * @param msg
     * @param qos
     * @return
     */
    @PostMapping(value = "/testPublish.do")
    public RespValue testPublish(String topic, String msg, int qos) {
        mqttserver.sendMQTTMessage(topic, msg, qos);
        String data = "发送了一条主题是‘"+topic+"’，内容是:"+msg+"，消息级别 "+qos;
        return new RespValue(200,"success",data);
    }
 
    /**
     * 订阅主题
     * @param topic 主题
     * @param qos 消息级别
     * @return
     */
    @RequestMapping(value = "/testSubscribe.do")
    public String testSubsribe(String topic, int qos) {
        mqttserver.init(topic, qos);
        return "订阅主题'"+topic+"'成功";
    }
}
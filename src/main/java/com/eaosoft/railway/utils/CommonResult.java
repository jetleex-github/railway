package com.eaosoft.railway.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonResult {
    // 状态码：小于0 表示错误，大于等于0表示正确
    private int code;
    // 提示信息
    private String info;
    // 返回内容
    private Object data;
}

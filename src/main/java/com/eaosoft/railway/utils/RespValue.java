package com.eaosoft.railway.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RespValue {
    private Integer code;
    private String info;
    private Object data;

}

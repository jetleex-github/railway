package com.eaosoft.railway.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReqValue {
    private String callerName;
    private String token;
    private Object requestDatas;
}

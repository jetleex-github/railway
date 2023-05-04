package com.eaosoft.railway.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PictureVo {

    private String uid;

    private String stationUid;
    private Object rightPicture;
    private Object leftPicture;
    private Object frontPicture;

}

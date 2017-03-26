package com.fish.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by yudin on 2017/3/26.
 */
@Data
public class AddNodeBean implements Serializable {

    private String nodeName;

    private String encryptType;

    private String nodeData;


}

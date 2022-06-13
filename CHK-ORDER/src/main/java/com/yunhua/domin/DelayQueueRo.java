package com.yunhua.domin;

import lombok.Data;

@Data
public class DelayQueueRo {

    private String topic;

    private String key;

    private String value;
}

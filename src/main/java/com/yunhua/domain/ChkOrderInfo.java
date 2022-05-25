package com.yunhua.domain;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("chk_merchant_info")
public class ChkOrderInfo {

    private Integer id;
}

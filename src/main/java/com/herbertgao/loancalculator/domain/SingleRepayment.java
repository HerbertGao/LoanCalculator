package com.herbertgao.loancalculator.domain;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class SingleRepayment {

    /**
     * 期号
     */
    @JSONField(ordinal = 1)
    private int term;
    /**
     * 每月还款本息
     */
    @JSONField(ordinal = 2)
    private BigDecimal amount;
    /**
     * 每月还款利息
     */
    @JSONField(ordinal = 3)
    private BigDecimal interest;
    /**
     * 每月还款本金
     */
    @JSONField(ordinal = 4)
    private BigDecimal principal;
    /**
     * 平台抽息
     */
    @JSONField(ordinal = 5)
    private BigDecimal commission;

}

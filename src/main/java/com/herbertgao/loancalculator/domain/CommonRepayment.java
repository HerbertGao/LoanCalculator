package com.herbertgao.loancalculator.domain;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@Accessors(chain = true)
public class CommonRepayment {

    private List<SingleRepayment> repaymentList;

    /**
     * 每月还款本息
     */
    private Map<Integer, BigDecimal> amountMap;
    /**
     * 每月还款利息, 期数:利息
     */
    private Map<Integer, BigDecimal> interestMap;
    /**
     * 每月还款本金, 期数:本金
     */
    private Map<Integer, BigDecimal> principalMap;
    /**
     * 本息总和
     */
    private BigDecimal totalAmount;
    /**
     * 总利息
     */
    private BigDecimal totalInterest;
    /**
     * 平台抽息
     */
    private Map<Integer, BigDecimal> commissionMap;
    /**
     * 平台抽息总额
     */
    private BigDecimal totalCommission;
}

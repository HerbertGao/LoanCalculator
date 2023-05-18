package com.herbertgao.loancalculator;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;
import com.herbertgao.loancalculator.domain.CommonRepayment;
import com.herbertgao.loancalculator.util.RepaymentUtil;

import java.math.BigDecimal;

public class Main {

    private static final BigDecimal INVEST = BigDecimal.valueOf(10000);
    private static final BigDecimal YEAR_RATE = BigDecimal.valueOf(0.00345 * 12);
    private static final int MONTH = 12;
    private static final BigDecimal COMMISSION = BigDecimal.valueOf(0.001 * 12);

    public static void main(String[] args) {
        CommonRepayment result1 = RepaymentUtil.fixedRepayment(INVEST, YEAR_RATE, MONTH, COMMISSION);
        System.out.println("生成等额本息还款计划, result1:" + JSON.toJSONString(result1, JSONWriter.Feature.PrettyFormat));

        CommonRepayment result2 = RepaymentUtil.fixedCapital(INVEST, YEAR_RATE, MONTH, COMMISSION);
        System.out.println("生成等额本金还款计划, result2:" + JSON.toJSONString(result2, JSONWriter.Feature.PrettyFormat));
    }

}

package com.herbertgao.loancalculator.util;


import com.herbertgao.loancalculator.domain.CommonRepayment;
import com.herbertgao.loancalculator.domain.SingleRepayment;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * <P>
 * 还款计算工具类
 * </p>
 *
 * @author herbertgao
 * @version 1.0.0
 * @since 1.8
 */
public class RepaymentUtil {
    /**
     * 生成等额本息还款计划
     *
     * @param invest     本金
     * @param month      分期, 期数( 单位月 )
     * @param yearRate   年利率
     * @param commission 抽息，佣金
     * @return {@link CommonRepayment}
     */
    public static CommonRepayment fixedRepayment(BigDecimal invest, BigDecimal yearRate, int month, BigDecimal commission) {
        if (yearRate.compareTo(BigDecimal.ZERO) == 0) {
            return fixedCapital(invest, yearRate, month, commission);
        }
        // 计算月利率
        BigDecimal monthRate = yearRate.divide(BigDecimal.valueOf(12), 6, RoundingMode.HALF_EVEN);
        // 设置平台抽息
        BigDecimal comMonthRate = commission.divide(BigDecimal.valueOf(12), 6, RoundingMode.HALF_EVEN);
        // 封装数据
        CommonRepayment repayment = new CommonRepayment();
        List<SingleRepayment> repaymentList = new ArrayList<>();

        // 单月还款本息
        BigDecimal amount;
        // 单月还款利息
        BigDecimal interest;
        // 单月还款本金
        BigDecimal principal;
        // 单月平台抽息
        BigDecimal commissionMonth;
        // 本息总和
        BigDecimal totalAmount = BigDecimal.ZERO;
        // 总利息
        BigDecimal totalInterest = BigDecimal.ZERO;
        // 总平台抽息
        BigDecimal totalCommission = BigDecimal.ZERO;

        for (int i = 1; i < month + 1; i++) {
            SingleRepayment single = new SingleRepayment();
            amount = getRepaymentAmount(invest, monthRate, month);
            interest = getRepaymentInterest(invest, monthRate, month, i);
            principal = amount.subtract(interest);
            commissionMonth = getRepaymentInterest(invest, comMonthRate, month, i);

            totalAmount = totalAmount.add(amount);
            totalInterest = totalInterest.add(interest);
            totalCommission = totalCommission.add(commissionMonth);

            single.setTerm(i).setAmount(amount).setInterest(interest).setPrincipal(principal).setCommission(commissionMonth);
            repaymentList.add(single);
        }

        repayment.setRepaymentList(repaymentList).setTotalAmount(totalAmount).setTotalInterest(totalInterest).setTotalCommission(totalCommission);
        // 返回结果
        return repayment;
    }

    /**
     * 等额本息计算获取还款方式为等额本息的每月偿还本金和利息
     * 公式: 每月偿还本息=〔贷款本金×月利率×(1＋月利率)＾还款月数〕÷〔(1＋月利率)＾还款月数-1〕
     *
     * @param invest    总借款额(贷款本金)
     * @param monthRate 月利率
     * @param month     还款总月数
     * @return 每月偿还本金和利息, 不四舍五入, 直接截取小数点最后两位
     */
    private static BigDecimal getRepaymentAmount(BigDecimal invest, BigDecimal monthRate, int month) {
        BigDecimal monthInterest;
        if (monthRate.compareTo(BigDecimal.ZERO) != 0) {
            monthInterest = invest.multiply(monthRate.multiply(BigDecimal.ONE.add(monthRate).pow(month)))
                    .divide(BigDecimal.ONE.add(monthRate).pow(month).subtract(BigDecimal.ONE), 2, RoundingMode.HALF_EVEN);
        } else {
            monthInterest = invest.divide(BigDecimal.valueOf(month), 2, RoundingMode.HALF_EVEN);
        }
        return monthInterest;
    }

    /**
     * 等额本息计算获取还款方式为等额本息的每月偿还利息
     * 公式: 每月偿还利息=贷款本金×月利率×〔(1+月利率)^还款月数-(1+月利率)^(还款月序号-1)〕÷〔(1+月利率)^还款月数-1〕
     *
     * @param invest    总借款额(贷款本金)
     * @param monthRate 月利率
     * @param month     还款总月数
     * @param i         期号
     * @return 每月偿还利息
     */
    private static BigDecimal getRepaymentInterest(BigDecimal invest, BigDecimal monthRate, int month, int i) {
        BigDecimal monthInterest;
        if (monthRate.compareTo(BigDecimal.ZERO) != 0) {
            BigDecimal multiply = invest.multiply(monthRate);
            BigDecimal sub = BigDecimal.ONE.add(monthRate).pow(month).subtract(BigDecimal.ONE.add(monthRate).pow(i - 1));
            monthInterest = multiply.multiply(sub).divide(BigDecimal.ONE.add(monthRate).pow(month).subtract(BigDecimal.ONE), 2, RoundingMode.HALF_EVEN);
        } else {
            monthInterest = BigDecimal.ZERO;
        }
        monthInterest = monthInterest.setScale(2, RoundingMode.HALF_EVEN);
        return monthInterest;
    }


    /**
     * 生成等额本金计划
     *
     * @param invest     本金
     * @param month      分期, 期数
     * @param yearRate   年利率
     * @param commission 抽息，佣金
     * @return {@link CommonRepayment}
     */
    public static CommonRepayment fixedCapital(BigDecimal invest, BigDecimal yearRate, int month, BigDecimal commission) {
        // 计算月利率
        BigDecimal monthRate = yearRate.divide(BigDecimal.valueOf(12), 6, RoundingMode.HALF_EVEN);
        // 设置平台抽息
        BigDecimal comMonthRate = commission.divide(BigDecimal.valueOf(12), 6, RoundingMode.HALF_EVEN);
        // 封装数据
        CommonRepayment repayment = new CommonRepayment();
        List<SingleRepayment> repaymentList = new ArrayList<>();

        // 单月还款本息
        BigDecimal amount;
        // 单月还款利息
        BigDecimal interest;
        // 单月还款本金
        BigDecimal principal;
        // 单月平台抽息
        BigDecimal commissionMonth;
        // 本息总和
        BigDecimal totalAmount = BigDecimal.ZERO;
        // 总利息
        BigDecimal totalInterest = BigDecimal.ZERO;
        // 总平台抽息
        BigDecimal totalCommission = BigDecimal.ZERO;

        for (int i = 1; i < month + 1; i++) {
            SingleRepayment single = new SingleRepayment();
            principal = getCapitalPrincipal(invest, month, i);
            interest = getCapitalInterest(invest, monthRate, month, i);
            amount = principal.add(interest);
            commissionMonth = getCapitalInterest(invest, comMonthRate, month, i);

            totalAmount = totalAmount.add(amount);
            totalInterest = totalInterest.add(interest);
            totalCommission = totalCommission.add(commissionMonth);

            single.setTerm(i).setAmount(amount).setInterest(interest).setPrincipal(principal).setCommission(commissionMonth);
            repaymentList.add(single);
        }

        repayment.setRepaymentList(repaymentList).setTotalAmount(totalAmount).setTotalInterest(totalInterest).setTotalCommission(totalCommission);
        // 返回结果
        return repayment;
    }

    /**
     * 等额本金计算获取还款方式为等额本金的每月偿还本金
     * 公式: 每月应还本金=贷款本金÷还款月数
     *
     * @param invest 总借款额(贷款本金)
     * @param month  还款总月数
     * @return 每月偿还本金
     */
    private static BigDecimal getCapitalPrincipal(BigDecimal invest, int month) {
        return invest.divide(BigDecimal.valueOf(month), 2, RoundingMode.HALF_EVEN);
    }

    /**
     * 等额本金计算获取还款方式为等额本金的每月偿还本金
     * 公式: 每月应还本金=贷款本金÷还款月数
     *
     * @param invest 总借款额(贷款本金)
     * @param month  还款总月数
     * @param i      期号
     * @return 每月偿还本金
     */
    private static BigDecimal getCapitalPrincipal(BigDecimal invest, int month, int i) {
        BigDecimal principal = getCapitalPrincipal(invest, month);
        if (i == month) {
            principal = invest.subtract(principal.multiply(BigDecimal.valueOf(i - 1))).setScale(2, RoundingMode.HALF_EVEN);
        }
        return principal;
    }

    /**
     * 等额本金计算获取还款方式为等额本金的每月偿还利息
     * 公式: 每月应还利息=剩余本金×月利率=(贷款本金-已归还本金累计额)×月利率
     *
     * @param invest    总借款额(贷款本金)
     * @param monthRate 年利率
     * @param month     还款总月数
     * @param i         期号
     * @return 每月偿还利息
     */
    private static BigDecimal getCapitalInterest(BigDecimal invest, BigDecimal monthRate, int month, int i) {
        BigDecimal principal = getCapitalPrincipal(invest, month);
        return invest.subtract(principal.multiply(BigDecimal.valueOf(i - 1)))
                .multiply(monthRate)
                .setScale(2, RoundingMode.HALF_EVEN);
    }
}

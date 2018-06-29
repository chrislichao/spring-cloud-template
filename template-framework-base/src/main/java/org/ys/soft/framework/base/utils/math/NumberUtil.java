package org.ys.soft.framework.base.utils.math;

import java.math.BigDecimal;

import org.ys.soft.framework.base.utils.Assert;

/**
 * [计算的工具类]
 * 
 * @author Chris li[黎超]
 * @version [版本, 2017-04-12]
 * @see
 */
public class NumberUtil {

	/**
	 * [提供(相对)精确的除法运算.当发生除不尽的情况时,由scale参数指定精度四舍五入]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public static double div(double v1, double v2, int scale) {
		Assert.isTrue(scale < 0, "精度不允许小于0!");

		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
}

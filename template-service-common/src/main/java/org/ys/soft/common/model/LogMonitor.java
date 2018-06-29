package org.ys.soft.common.model;

import org.ys.soft.framework.base.annotation.database.Column;
import org.ys.soft.framework.base.annotation.database.Id;
import org.ys.soft.framework.base.annotation.database.LeftJoin;
import org.ys.soft.framework.base.annotation.database.Table;
import org.ys.soft.framework.base.enums.database.Group;
import org.ys.soft.framework.base.model.BaseEntity;
import org.ys.soft.framework.base.model.system.SysUser;

/**
 * [系统监控日志]
 * 
 * @author Chris li[黎超]
 * @version [版本, 2017-04-12]
 * @see
 */
@Table
public class LogMonitor extends BaseEntity {
	/**
	 * 权限ToString的格式
	 */
	private String toStrFormat = "{SysMonitorLog [id = %d][method = %s][timeConsuming = %s]}";
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 主键
	 */
	@Id
	protected Long id;
	/**
	 * 方法名称
	 */
	@Column
	private String method;
	/**
	 * 方法描述
	 */
	@Column
	private String description;
	/**
	 * 参数
	 */
	@Column
	private String params;
	/**
	 * 返回值
	 */
	@Column
	private String returnedValue;
	/**
	 * 调用状态,1:正常;-1:异常
	 */
	@Column
	private Integer status;
	/**
	 * 异常信息
	 */
	@Column
	private String errorMsg;
	/**
	 * 异常发生的类路径
	 */
	@Column
	private String errorClass;
	/**
	 * 异常发生的方法名
	 */
	@Column
	private String errorMethod;
	/**
	 * 异常发生点行号
	 */
	@Column
	private Integer errorLine;
	/**
	 * 耗时
	 */
	@Column
	private Long timeConsuming;
	/**
	 * 调用时间
	 */
	@Column
	private Integer invokeTime;
	/**
	 * 调用人(id)
	 */
	@Column
	private Long invokeBy;
	/**
	 * 调用人ip
	 */
	@Column
	private String invokeIp;
	/**
	 * 调用人名称
	 */
	@LeftJoin(refModel = SysUser.class, group = Group.ONE, selfOnField = "invokeBy", refValField = "nickname")
	private String invokeByName;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public String getReturnedValue() {
		return returnedValue;
	}

	public void setReturnedValue(String returnedValue) {
		this.returnedValue = returnedValue;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public void setErrorClass(String errorClass) {
		this.errorClass = errorClass;
	}

	public String getErrorClass() {
		return errorClass;
	}

	public void setErrorMethod(String errorMethod) {
		this.errorMethod = errorMethod;
	}

	public String getErrorMethod() {
		return errorMethod;
	}

	public void setErrorLine(Integer errorLine) {
		this.errorLine = errorLine;
	}

	public Integer getErrorLine() {
		return errorLine;
	}

	public Long getTimeConsuming() {
		return timeConsuming;
	}

	public void setTimeConsuming(Long timeConsuming) {
		this.timeConsuming = timeConsuming;
	}

	public Integer getInvokeTime() {
		return invokeTime;
	}

	public void setInvokeTime(Integer invokeTime) {
		this.invokeTime = invokeTime;
	}

	public Long getInvokeBy() {
		return invokeBy;
	}

	public void setInvokeBy(Long invokeBy) {
		this.invokeBy = invokeBy;
	}

	public String getInvokeIp() {
		return invokeIp;
	}

	public void setInvokeIp(String invokeIp) {
		this.invokeIp = invokeIp;
	}

	public String getInvokeByName() {
		return invokeByName;
	}

	public void setInvokeByName(String invokeByName) {
		this.invokeByName = invokeByName;
	}

	@Override
	public String toString() {
		return String.format(toStrFormat, id, method, timeConsuming);
	}

	@Override
	public boolean equals(Object obj) {
		if (null == obj) {
			return false;
		}
		if (null == id) {
			return false;
		}
		LogMonitor another = (LogMonitor) obj;
		return id.longValue() == another.getId().longValue();
	}
}

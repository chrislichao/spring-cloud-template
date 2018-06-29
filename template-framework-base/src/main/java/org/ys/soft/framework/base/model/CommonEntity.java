package org.ys.soft.framework.base.model;

import org.ys.soft.framework.base.annotation.database.Column;
import org.ys.soft.framework.base.annotation.database.Id;

/**
 * [共用的实体类,存放共用属性]
 * 
 * @author Chris li[黎超]
 * @version [版本, 2017-04-12]
 * @see
 */
public class CommonEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;
	/**
	 * 主键
	 */
	@Id
	protected Long id;
	/**
	 * 备注
	 */
	@Column
	protected String remark;
	/**
	 * 新增时间
	 */
	@Column
	protected Integer createTime;
	/**
	 * 新增人id
	 */
	@Column
	protected Long createBy;
	/**
	 * 修改时间
	 */
	@Column
	protected Integer updateTime;
	/**
	 * 修改人id
	 */
	@Column
	protected Long updateBy;

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getRemark() {
		return remark;
	}

	public Integer getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Integer createTime) {
		this.createTime = createTime;
	}

	public Long getCreateBy() {
		return createBy;
	}

	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}

	public Integer getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Integer updateTime) {
		this.updateTime = updateTime;
	}

	public Long getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(Long updateBy) {
		this.updateBy = updateBy;
	}
}

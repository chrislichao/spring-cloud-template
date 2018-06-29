package org.ys.soft.framework.base.model.system;

import org.ys.soft.framework.base.annotation.database.Column;
import org.ys.soft.framework.base.annotation.database.LeftJoin;
import org.ys.soft.framework.base.annotation.database.Table;
import org.ys.soft.framework.base.enums.database.Group;
import org.ys.soft.framework.base.model.CommonEntity;

/**
 * [系统用户]
 * 
 * @author Chris li[黎超]
 * @version [版本, 2017-04-12]
 * @see
 */
@Table
public class SysUser extends CommonEntity {
	private static final long serialVersionUID = 1L;
	/**
	 * 用户名
	 */
	@Column
	private String username;
	/**
	 * 密码
	 */
	@Column
	private String password;
	/**
	 * 昵称
	 */
	@Column
	private String nickname;
	/**
	 * 创建人名称
	 */
	@LeftJoin(refModel = SysUser.class, group = Group.ONE, selfOnField = "createBy", refValField = "nickname")
	private String createByName;
	/**
	 * 修改人名称
	 */
	@LeftJoin(refModel = SysUser.class, group = Group.TWO, selfOnField = "updateBy", refValField = "nickname")
	private String updateByName;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public void setCreateByName(String createByName) {
		this.createByName = createByName;
	}

	public String getCreateByName() {
		return createByName;
	}

	public String getUpdateByName() {
		return updateByName;
	}

	public void setUpdateByName(String updateByName) {
		this.updateByName = updateByName;
	}

}

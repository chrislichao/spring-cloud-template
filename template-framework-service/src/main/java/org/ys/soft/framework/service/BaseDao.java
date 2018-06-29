package org.ys.soft.framework.service;

import java.util.List;

import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.ys.soft.framework.base.Where;
import org.ys.soft.framework.base.model.BaseEntity;
import org.ys.soft.framework.service.annotation.AutoSetResultType;

/**
 * [基础DAO,自动完成增删查改基础功能]
 * 
 * @author Chris li[黎超]
 * @version [版本, 2017-04-12]
 * @see
 */
public interface BaseDao<T extends BaseEntity> {

	/**
	 * 基类的新增方法<br>
	 * 例如:<br>
	 * -----------------------------------------------------------------<br>
	 * model : id = 1, name = "Chris", remark = null <br>
	 * sql : INSERT INTO [TABLE](ID,NAME) VALUES (1,'Chris') <br>
	 * -----------------------------------------------------------------
	 */
	@InsertProvider(type = SqlProvider.class, method = "create")
	public abstract int create(T t);

	/**
	 * 基类的删除方法,通过主键删除数据<br>
	 * 例如:<br>
	 * -----------------------------------------------------------------<br>
	 * model : id = 1, name = "Chris", remark = null <br>
	 * sql : DELETE FROM [TABLE] WHERE ID = 1 <br>
	 * -----------------------------------------------------------------
	 */
	@DeleteProvider(type = SqlProvider.class, method = "deleteByPk")
	public abstract int deleteByPk(T t);

	/**
	 * 基类的批量删除方法,根据对象属性(查询条件不包括值为空的属性)删除<br>
	 * 例如:<br>
	 * -----------------------------------------------------------------<br>
	 * model : id = null, name = "Chris", remark = null <br>
	 * sql : DELETE FROM [TABLE] WHERE NAME = 'Chris' <br>
	 * -----------------------------------------------------------------
	 */
	@DeleteProvider(type = SqlProvider.class, method = "deleteBatch")
	public abstract int deleteBatch(T t);

	/**
	 * 基类的批量删除方法,根据对象属性(查询条件包括值为空的属性)删除<br>
	 * 例如:<br>
	 * -----------------------------------------------------------------<br>
	 * model : id = 1, name = "Chris", remark = null <br>
	 * sql : DELETE FROM [TABLE] WHERE ID = 1 AND NAME = 'Chris' AND REMARK IS
	 * NULL<br>
	 * -----------------------------------------------------------------
	 */
	@DeleteProvider(type = SqlProvider.class, method = "deleteBatchByAllFields")
	public abstract int deleteBatchByAllFields(T t);

	/**
	 * 基类的批量删除方法,自定义条件批量删除
	 */
	@DeleteProvider(type = SqlProvider.class, method = "deleteBatchWhere")
	public abstract int deleteBatchWhere(Where where);

	/**
	 * 基类的修改方法,根据主键,修改单个对象的属性,不包含值为空的字段<br>
	 * 例如:<br>
	 * ------------------------------------------------------------------<br>
	 * model : id = 1, name = "Chris", remark = null <br>
	 * sql : UPDATE [TABLE] SET NAME = 'Chris' WHERE ID = 1 <br>
	 * ------------------------------------------------------------------
	 */
	@UpdateProvider(type = SqlProvider.class, method = "updateByPk")
	public abstract int updateByPk(T t);

	/**
	 * 基类的修改方法,根据主键,修改单个对象的属性,包含值为空的字段<br>
	 * 例如:<br>
	 * ------------------------------------------------------------------------
	 * ----------------<br>
	 * model : id = 1, name = "Chris", remark = null <br>
	 * sql : UPDATE [TABLE] SET NAME = 'Chris' , REMARK IS NULL WHERE ID = 1 <br>
	 * ------------------------------------------------------------------------
	 * ----------------
	 */
	@UpdateProvider(type = SqlProvider.class, method = "updateAllFieldsByPk")
	public abstract int updateAllFieldsByPk(T t);

	/**
	 * 基类的修改方法,根据自定义查询条件,修改单个对象的属性,不包含值为空的字段<br>
	 */
	@UpdateProvider(type = SqlProvider.class, method = "updateWhere")
	public abstract int updateWhere(@Param("model") T t, @Param("where") Where where);

	/**
	 * 基类的修改方法,根据自定义查询条件,修改单个对象的属性,包含值为空的字段<br>
	 */
	@UpdateProvider(type = SqlProvider.class, method = "updateAllFieldsWhere")
	public abstract int updateAllFieldsWhere(@Param("model") T t, @Param("where") Where where);

	/**
	 * 基类的查询方法,通过主键查询,最多只能查到一个对象返回<如果查到多个,则抛异常<br>
	 * 例如:<br>
	 * ------------------------------------------------------------------<br>
	 * model : id = 1, name = "Chris", remark = null <br>
	 * sql : SELECT * FROM [TABLE] WHERE ID = 1 <br>
	 * ------------------------------------------------------------------
	 */
	@SelectProvider(type = SqlProvider.class, method = "retrieveByPk")
	@AutoSetResultType
	public abstract T retrieveByPk(T t);

	/**
	 * 基类的查询方法,根据对象属性(查询条件不包括值为空的属性)查到对象集合 例如:<br>
	 * ------------------------------------------------------------------<br>
	 * model : id = 1, name = "Chris", remark = null <br>
	 * sql : SELECT * FROM [TABLE] WHERE ID = 1 AND NAME = 'Chris'<br>
	 * ------------------------------------------------------------------
	 */
	@SelectProvider(type = SqlProvider.class, method = "retrieveList")
	@AutoSetResultType
	public abstract List<T> retrieveList(T t);

	/**
	 * 基类的查询方法,根据对象属性(查询条件包括值为空的属性)查到对象集合 例如:<br>
	 * ------------------------------------------------------------------<br>
	 * model : id = 1, name = "Chris", remark = null <br>
	 * sql : SELECT * FROM [TABLE] WHERE ID = 1 AND NAME = 'Chris' AND REMARK IS
	 * NULL<br>
	 * ------------------------------------------------------------------
	 */
	@SelectProvider(type = SqlProvider.class, method = "retrieveListByAllFields")
	@AutoSetResultType
	public abstract List<T> retrieveListByAllFields(T t);

	/**
	 * 基类的查询方法,根据自定义查询条件查到对象集合
	 */
	@SelectProvider(type = SqlProvider.class, method = "retrieveListWhere")
	@AutoSetResultType
	public abstract List<T> retrieveListWhere(Where where);

	/**
	 * 基类的查询个数方法,根据对象属性(查询条件不包括值为空的属性)查到对象个数 例如:<br>
	 * ------------------------------------------------------------------<br>
	 * model : id = 1, name = "Chris", remark = null <br>
	 * sql : SELECT COUNT(*) FROM [TABLE] WHERE ID = 1 AND NAME = 'Chris'<br>
	 * ------------------------------------------------------------------
	 */
	@SelectProvider(type = SqlProvider.class, method = "getCount")
	public abstract int getCount(T t);

	/**
	 * 基类的查询个数方法,根据对象属性(查询条件包括值为空的属性)查到对象个数 例如:<br>
	 * ------------------------------------------------------------------<br>
	 * model : id = 1, name = "Chris", remark = null <br>
	 * sql : SELECT COUNT(*) FROM [TABLE] WHERE ID = 1 AND NAME = 'Chris' AND
	 * REMARK IS NULL<br>
	 * ------------------------------------------------------------------
	 */
	@SelectProvider(type = SqlProvider.class, method = "getCountByAllFields")
	public abstract int getCountByAllFields(T t);

	/**
	 * 基类的查询个数方法,根据自定义查询条件查到对象个数
	 */
	@SelectProvider(type = SqlProvider.class, method = "getCountWhere")
	public abstract int getCountWhere(Where where);
}

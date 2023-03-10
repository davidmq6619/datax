package com.sinohealth.datax.entity.common;

import com.sinohealth.datax.reflection.annotations.db_alias;

/**
 * <p>
 * 诊断内容字典
 * </p>
 *
 * @author Tony
 * @date 2018/8/7
 */
@db_alias("bas_item_alias")
public class BasItemAlias {

	/**
	 * 编号
	 */
	public Long	id;

	public String name;

	@db_alias("alias_name")
	public String aliasName;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAliasName() {
		return aliasName;
	}

	public void setAliasName(String aliasName) {
		this.aliasName = aliasName;
	}
}

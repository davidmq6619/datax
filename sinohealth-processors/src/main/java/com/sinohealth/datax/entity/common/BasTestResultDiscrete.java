package com.sinohealth.datax.entity.common;

import com.sinohealth.datax.reflection.annotations.db_alias;

public class BasTestResultDiscrete {
	public String	id;
	@db_alias("item_name_comm")
	public String	itemNameComm;
	public String	discrete;
	@db_alias("discrete_type")
	public Integer	discreteType;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getItemNameComm() {
		return itemNameComm;
	}

	public void setItemNameComm(String itemNameComm) {
		this.itemNameComm = itemNameComm;
	}

	public String getDiscrete() {
		return discrete;
	}

	public void setDiscrete(String discrete) {
		this.discrete = discrete;
	}

	public Integer getDiscreteType() {
		return discreteType;
	}

	public void setDiscreteType(Integer discreteType) {
		this.discreteType = discreteType;
	}
}

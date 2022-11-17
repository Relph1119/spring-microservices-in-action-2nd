package com.optimagrowth.organization.events.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class OrganizationChangeModel {
	private String type;
	// 触发事件的动作
	private String action;
	// 与事件关联的组织ID
	private String organizationId;
	// 触发事件的服务调用的关联ID
	private String correlationId;

	public OrganizationChangeModel(String type, String action, String organizationId, String correlationId) {
		super();
		this.type = type;
		this.action = action;
		this.organizationId = organizationId;
		this.correlationId = correlationId;
	}
}

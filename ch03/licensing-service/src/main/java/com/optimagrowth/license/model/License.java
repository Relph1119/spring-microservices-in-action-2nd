package com.optimagrowth.license.model;

import org.springframework.hateoas.RepresentationModel;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class License extends RepresentationModel<License> {
    // RepresentationModel为License模型提供添加链接的能力
    private int id;
    private String licenseId;
    private String description;
    private String organizationId;
    private String productName;
    private String licenseType;

}
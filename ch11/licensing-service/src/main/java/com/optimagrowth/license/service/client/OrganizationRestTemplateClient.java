package com.optimagrowth.license.service.client;

import brave.ScopedSpan;
import brave.Tracer;
import com.optimagrowth.license.repository.OrganizationRedisRepository;
import com.optimagrowth.license.utils.UserContext;
import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.optimagrowth.license.model.Organization;

@Component
public class OrganizationRestTemplateClient {
    @Autowired
    private KeycloakRestTemplate restTemplate;

    private static final Logger logger = LoggerFactory.getLogger(OrganizationRestTemplateClient.class);

    @Autowired
    OrganizationRedisRepository redisRepository;

    @Autowired
    Tracer tracer;

    public Organization getOrganization(String organizationId){
        logger.debug("In Licensing Service.getOrganization: {}", UserContext.getCorrelationId());

        Organization organization = checkRedisCache(organizationId);

        if (organization != null){
            logger.debug("I have successfully retrieved an organization {} from the redis cache: {}", organizationId, organization);
            return organization;
        }

        logger.debug("Unable to locate organization from the redis cache: {}.", organizationId);

        ResponseEntity<Organization> restExchange =
                restTemplate.exchange(
                        "http://gateway:8072/organization/v1/organization/{organizationId}",
                        HttpMethod.GET,
                        null, Organization.class, organizationId);

        // 如果无法从Redis中检索出数据，那么将调用组织服务从源数据库检索数据，然后保存到Redis中
        organization = restExchange.getBody();
        if (organization != null) {
            cacheOrganizationObject(organization);
        }

        return restExchange.getBody();
    }

    private Organization checkRedisCache(String organizationId) {
        // 创建一个新的名为readLicensingDataFromRedis的自定义跨度
        ScopedSpan newSpan = tracer.startScopedSpan("readLicensingDataFromRedis");
        try {
            // 尝试使用组织ID从Redis中检索Organization类
            return redisRepository.findById(organizationId).orElse(null);
        }catch (Exception ex){
            logger.error("Error encountered while trying to retrieve organization {} check Redis Cache.  Exception {}", organizationId, ex);
            return null;
        }finally {
            // 将标签信息添加到跨度中，并提供了将要被Zipkin捕获的服务名称
            newSpan.tag("peer.service", "redis");
            newSpan.annotate("Client received");
            // 关闭并结束跟踪，如果没有这么做，在日志中会得到一条错误信息，显示跨度还未关闭
            newSpan.finish();
        }
    }

    private void cacheOrganizationObject(Organization organization) {
        try {
            // 在Redis中保持组织信息
            redisRepository.save(organization);
        }catch (Exception ex){
            logger.error("Unable to cache organization {} in Redis. Exception {}", organization.getId(), ex);
        }
    }
}

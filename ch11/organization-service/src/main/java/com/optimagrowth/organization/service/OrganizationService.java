package com.optimagrowth.organization.service;

import java.util.Optional;
import java.util.UUID;

import brave.ScopedSpan;
import brave.Tracer;
import com.optimagrowth.organization.events.source.SimpleSourceBean;
import com.optimagrowth.organization.model.ActionEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.optimagrowth.organization.model.Organization;
import com.optimagrowth.organization.repository.OrganizationRepository;

@Service
public class OrganizationService {

    @Autowired
    Tracer tracer;

    @Autowired
    private OrganizationRepository repository;

    private static final Logger logger = LoggerFactory.getLogger(OrganizationService.class);

    @Autowired
    SimpleSourceBean simpleSourceBean;

    public Organization findById(String organizationId) {
    	Optional<Organization> opt = null;
        ScopedSpan newSpan = tracer.startScopedSpan("getOrgDBCall");
        try {
            opt = repository.findById(organizationId);
            simpleSourceBean.publishOrganizationChange(ActionEnum.GET, organizationId);
            if (!opt.isPresent()) {
                String message = String.format("Unable to find an organization with the Organization id %s", organizationId);
                logger.error(message);
                throw new IllegalArgumentException(message);
            }
            logger.debug("Retrieving Organization Info: " + opt.get());
        } finally {
            newSpan.tag("peer.service", "postgres");
            newSpan.annotate("Client received");
            newSpan.finish();
        }
        return opt.get();
    }

    public Organization create(Organization organization){
    	organization.setId( UUID.randomUUID().toString());
        organization = repository.save(organization);
        // 对服务中修改组织数据的每一个方法，调用simpleSourceBean.publishOrganizationChange()
        simpleSourceBean.publishOrganizationChange(ActionEnum.CREATED, organization.getId());
        return organization;
    }

    public void update(Organization organization){
    	repository.save(organization);
        simpleSourceBean.publishOrganizationChange(ActionEnum.UPDATED, organization.getId());
    }

    public void delete(String organizationId){
        repository.deleteById(organizationId);
        simpleSourceBean.publishOrganizationChange(ActionEnum.DELETED, organizationId);
    }
}
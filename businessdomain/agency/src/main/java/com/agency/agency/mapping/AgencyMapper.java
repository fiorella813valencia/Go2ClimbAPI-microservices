package com.agency.agency.mapping;

import com.agency.agency.resource.CreateAgencyResource;
import com.agency.agency.shared.mapping.EnhancedModelMapper;
import com.agency.agency.domain.model.entity.Agency;
import com.agency.agency.resource.AgencyResource;
import com.agency.agency.resource.UpdateAgencyResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;
@Component
@EnableAutoConfiguration
public class AgencyMapper implements Serializable {
    @Autowired
    EnhancedModelMapper mapper;

    public AgencyResource toResource(Agency model) { return mapper.map(model, AgencyResource.class); }

    public Agency toModel(CreateAgencyResource resource) { return mapper.map(resource, Agency.class); }

    public Agency toModel(UpdateAgencyResource resource) { return mapper.map(resource, Agency.class); }

    public Page<AgencyResource> modelListPage(List<Agency> modelList, Pageable pageable){
        return new PageImpl<>(mapper.mapList(modelList, AgencyResource.class), pageable, modelList.size());
    }
}

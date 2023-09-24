package com.service.service.mapping;

import com.service.service.domain.model.entity.Service;
import com.service.service.resource.CreateServiceResource;
import com.service.service.resource.ServiceResource;
import com.service.service.resource.UpdateServiceResource;
import com.service.service.shared.mapping.EnhancedModelMapper;
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
public class ServiceMapper implements Serializable {
    @Autowired
    EnhancedModelMapper mapper;

    public ServiceResource toResource(Service model) { return mapper.map(model, ServiceResource.class); }

    public Service toModel(CreateServiceResource resource) { return mapper.map(resource, Service.class); }

    public Service toModel(UpdateServiceResource resource) { return mapper.map(resource, Service.class); }

    public Page<ServiceResource> modelListPage(List<Service> modelList, Pageable pageable){
        return new PageImpl<>(mapper.mapList(modelList, ServiceResource.class), pageable, modelList.size());
    }

}

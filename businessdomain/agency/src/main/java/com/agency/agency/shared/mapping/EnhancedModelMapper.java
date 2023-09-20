package com.agency.agency.shared.mapping;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;

public class EnhancedModelMapper extends ModelMapper {

    public EnhancedModelMapper() {
        super();
    }

    public <S, T> List<T> mapList(List<S> sourceList, Class<T> targetClass){
        return sourceList.stream().map(item -> this.map(item, targetClass)).collect(Collectors.toList());
    }

}

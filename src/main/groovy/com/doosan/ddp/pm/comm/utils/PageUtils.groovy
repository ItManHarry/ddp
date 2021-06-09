package com.doosan.ddp.pm.comm.utils

import org.apache.commons.lang3.StringUtils
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort

class PageUtils {
    public static PageRequest buildPageRequest(int pageNumber,int pageSize){
        return PageRequest.of(pageNumber-1,pageSize);
    }
    public static PageRequest buildPageRequest(int pageNumber,int pageSize,String sortName,Direction direction){
        if(StringUtils.isEmpty(sortName)||direction==null){
            sortName="tid";
            direction=Direction.DESC;
        }
        return PageRequest.of(pageNumber-1,pageSize,Sort.by(direction.equals(Direction.DESC)?Sort.Direction.DESC:Sort.Direction.ASC,sortName));
    }

    public static enum Direction{
        ASC,
        DESC;
    }
}

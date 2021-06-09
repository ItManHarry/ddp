package com.doosan.ddp.pm.service.base

import com.doosan.ddp.pm.comm.utils.PageUtils
import com.doosan.ddp.pm.dao.domain.base.TableEntityBaseModel
import com.doosan.ddp.pm.dao.jpa.base.BaseDao
import org.springframework.data.domain.Page


interface BaseService<D,M> {
    /**
     * 根据传入的model和model对应的DAO接口，动态查询符合条件的model集合
     * 使用该方法有几个前提
     *  1、model类继承model基类BaseModel
     *  2、model的DAO接口继承DAO基类BaseDao
     *  3、查询值直接set到model各属性，对比方法存到model中的mapCondition
     *  4、同一属性有多个条件，例如日期属性，需要按时间段查询，可以在model中新增“属性_2”的新属性存值。
     * @param d model类的DAO接口
     * @param m model实体
     * @return
     */
    List<M> searchByMultiParams(M m);

    Page<M> searchByMultiParamsAndPage(M m, int pageNumber, int pageSize);

    Page<M> searchByMultiParamsAndPage(M m, int pageNumber, int pageSize, String sortName, PageUtils.Direction direction);
}
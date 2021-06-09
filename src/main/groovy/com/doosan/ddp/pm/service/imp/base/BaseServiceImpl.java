package com.doosan.ddp.pm.service.imp.base;

import com.doosan.ddp.pm.comm.utils.ConditionTypeEnum;
import com.doosan.ddp.pm.comm.utils.PageUtils;
import com.doosan.ddp.pm.dao.domain.base.TableEntityBaseModel;
import com.doosan.ddp.pm.dao.jpa.base.BaseDao;
import com.doosan.ddp.pm.service.base.BaseService;
import groovy.util.logging.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import javax.persistence.criteria.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class BaseServiceImpl<D extends BaseDao,M extends TableEntityBaseModel> implements BaseService<D,M> {
    private  D d;

    public void setBaseDao(D d){
        this.d=d;
    }

    @Override
    public List<M> searchByMultiParams(M m) {
        List<M> result = d.findAll(getSpecification(m));
        return result;
    }
    @Override
    public Page<M> searchByMultiParamsAndPage(M m, int pageNumber, int pageSize){
        String sortName="tid";
        PageUtils.Direction direction=PageUtils.Direction.DESC;
        PageRequest pageRequest=PageUtils.buildPageRequest(pageNumber,pageSize,sortName,direction);
        Page<M> page = d.findAll(getSpecification(m),pageRequest);
        return page;
    }

    @Override
    public Page<M> searchByMultiParamsAndPage(M m, int pageNumber, int pageSize, String sortName, PageUtils.Direction direction){
        PageRequest pageRequest=PageUtils.buildPageRequest(pageNumber,pageSize,sortName,direction);
        Page<M> page = d.findAll(getSpecification(m),pageRequest);
        return page;
    }

    private Specification getSpecification(M m){
        Specification specification=new Specification<M>() {
            @Override
            public Predicate toPredicate(Root<M> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> list = new ArrayList<Predicate>();
                Map<String,ConditionTypeEnum> conditionMap=m.getMapCondition();
                conditionMap.forEach((k,v)->{
                    try {
                        list.add(predicateAdd(root,cb,list,k,v,m));
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                });
                Predicate[] p = new Predicate[list.size()];
                return cb.and(list.toArray(p));
            }
        };
        return specification;
    }

    private Predicate predicateAdd(Root<M> root, CriteriaBuilder cb, List<Predicate> list, String k, ConditionTypeEnum v,M m) throws NoSuchFieldException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Predicate p=null;
        //根据字段名获取model对应字段值
        Object[] fieldAndMethod=getFieldAndMethod(k,m);
        Field field= (Field) fieldAndMethod[0];
        Method method=(Method) fieldAndMethod[1];
        Object o= method.invoke(m);
        //字段类型
        String fieldType=field.getType().getName();
        //查询类型判断
        if(v== ConditionTypeEnum.EQ){
            p=cb.equal(root.get(orginalFiled(k)).as(String.class),o.toString());
        }else if(v==ConditionTypeEnum.GE){
            p=cb.greaterThanOrEqualTo(root.get(orginalFiled(k)),(Comparable)o);
        }else if(v==ConditionTypeEnum.LE){
            p=cb.lessThanOrEqualTo(root.get(orginalFiled(k)),(Comparable)o);
        }else if(v==ConditionTypeEnum.GT){
            p=cb.greaterThan(root.get(orginalFiled(k)),(Comparable)o);
        }else if(v==ConditionTypeEnum.LT){
            p=cb.lessThan(root.get(orginalFiled(k)),(Comparable)o);
        }else if(v==ConditionTypeEnum.IN){
            Path<Object> path=root.get(orginalFiled(k));
            CriteriaBuilder.In<Object> in =cb.in(path);
            for(Object o1:o.toString().split(",")){
                in.value(o1);
            }
            p=cb.and(in);
        }else if(v==ConditionTypeEnum.NotIN){
            Path<Object> path=root.get(orginalFiled(k));
            CriteriaBuilder.In<Object> in =cb.in(path);
            for(Object o1:o.toString().split(",")){
                in.value(o1);
            }
            p=cb.not(in);
        }else if(v==ConditionTypeEnum.Like){
            if(fieldType.contains("String")){
                p=cb.like(root.get(orginalFiled(k)).as(String.class),"%"+o.toString()+"%");
            }
        }else if(v==ConditionTypeEnum.LeftLike){
            if(fieldType.contains("String")){
                p= cb.like(root.get(orginalFiled(k)).as(String.class),"%"+o.toString());
            }
        }else if(v==ConditionTypeEnum.RightLike){
            if(fieldType.contains("String")){
                p=cb.like(root.get(orginalFiled(k)).as(String.class),o.toString()+"%");
            }
        }
        return p;
    }

    /*
    排查字段名，如果非表列字段，找到原表列字段
     */
    private String orginalFiled(String k){
        if(k.endsWith("_2")){
            k=k.substring(0,k.length()-2);
        }
        return k;
    }
    /*
    根据model类和field名，获取对应的field和method
     */
    private Object[] getFieldAndMethod(String field,M m) throws NoSuchFieldException, NoSuchMethodException {
        Class c=m.getClass();
        Field f=null;
        Method method=null;
        f=c.getDeclaredField(field);
        method=c.getMethod("get"+field.substring(0,1).toUpperCase()+field.substring(1));
        return new Object[]{f,method};
    }
}

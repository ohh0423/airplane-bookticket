package com.bookticket.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 为字段自动填充值(orders表)
 */
@Component
public class handler implements MetaObjectHandler {

    public static final Logger logger= LoggerFactory.getLogger(handler.class);

    /**
     * 插入时自动填充值
     * 订单创建时，获取当前时间为创建时间
     * 订单生成时订单的逻辑删除字段为未删除（0）
     * @param metaObject
     * @return void
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        logger.info("----------------------插入时自动填充值-------------------");
        this.strictInsertFill(metaObject, "order_create_time", Date.class,new Date());
        this.strictInsertFill(metaObject, "trips_delete_flag", Integer.class,0);
    }

    /**
     * 订单更新时间自动填充。初始为空，当更新订单时该字段自动设置为当前时间
     * 更新时自动为这个字段填充值（仅在数据库中该字段为空时才生效）
     * 可以覆盖strictFillStrategy方法来实现有值也可更新
     * @param metaObject
     * @return void
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        logger.info("-----------------------订单更新时间更新时自动填充------------------------");
        this.strictUpdateFill(metaObject,"order_update_time", Date.class,new Date());
    }
}

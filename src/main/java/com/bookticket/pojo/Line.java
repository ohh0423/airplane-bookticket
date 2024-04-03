package com.bookticket.pojo;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @TableName line
 */
@Data
@TableName(value = "line")
public class Line implements Serializable {
    /**
     * 编号
     *
     */
    @TableId(type = IdType.AUTO)
    private Integer line_id;

    /**
     * 起始站点
     *
     */
    private String line_start_station_name;

    /**
     * 到达站点
     *
     */
    private String line_end_station_name;


    public String line_start_station_name() {return this.line_start_station_name;}

    public String line_end_station_name() {return this.line_end_station_name;}
}
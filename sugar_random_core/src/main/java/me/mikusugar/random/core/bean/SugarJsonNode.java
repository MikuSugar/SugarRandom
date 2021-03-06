package me.mikusugar.random.core.bean;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import me.mikusugar.random.core.service.RandomCoreService;

import java.util.ArrayList;
import java.util.List;

/**
 * author: mikusugar
 */
@Data
@SuperBuilder
@EqualsAndHashCode(exclude="nexts")
@SuppressWarnings("ALL")
public class SugarJsonNode {

    public enum TYPE {
        NULL,
        LONG,
        DOUBLE,
        BOOLEAN,
        STRING,
        ARRAY,
        OBJECT,
        INT
    }

    private TYPE type;

    private String name;

    /**
     * 描述
     */
    private String desc;

    /**
     * 随机服务名
     */
    private String randomServiceName;

    private RandomCoreService randomService;

    @Builder.Default
    private List<SugarJsonNode> nexts = new ArrayList<>();

    private SugarJsonNode father;
}

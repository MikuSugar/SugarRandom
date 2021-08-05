package me.mikusugar.random.core.event;


import java.util.Map;

/**
 * @author mikusugar
 */
public interface ShowEventService {

    String getAllTypeInfo();

    String echo(String name, Map<String, String> aliasMap) throws Exception;


}

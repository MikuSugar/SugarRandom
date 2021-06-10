package me.mikusugar.randomsugar.app.constant;

import java.lang.reflect.Field;

public class ServiceNameValues {

  private static String[] values;

  public static String[] getValues() {
    if (values == null) {
      synchronized (ServiceNameValues.class) {
        if (values != null) return values;
        Field[] fields = ServiceName.class.getDeclaredFields();
        values = new String[fields.length];
        final Class<ServiceName> serviceName = ServiceName.class;
        for (int i = 0; i < fields.length; i++) {
          try {
            values[i] = fields[i].get(serviceName).toString();
          } catch (IllegalAccessException e) {
            e.printStackTrace();
          }
        }
      }
    }
    return values;
  }
}

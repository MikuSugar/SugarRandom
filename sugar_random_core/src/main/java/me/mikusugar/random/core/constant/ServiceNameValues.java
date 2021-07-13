package me.mikusugar.random.core.constant;

import java.lang.reflect.Field;
import java.util.Arrays;

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
    Arrays.sort(values, (o1, o2) -> {
      if(o1.length()==o2.length())return o1.compareTo(o2);
      return Integer.compare(o1.length(),o2.length());
    });
    return values;
  }
}

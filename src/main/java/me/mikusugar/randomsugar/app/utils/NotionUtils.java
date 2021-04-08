package me.mikusugar.randomsugar.app.utils;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;

/**
 * author: fangjie email: syfangjie@live.cn date: 2021/4/8 11:56 上午
 */
public class NotionUtils {

  public static void defaultNotion(String message){
    Notification.show(message, 5000, Position.TOP_CENTER);
  }

}

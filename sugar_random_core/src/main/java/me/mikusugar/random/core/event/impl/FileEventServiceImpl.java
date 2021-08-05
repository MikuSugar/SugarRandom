package me.mikusugar.random.core.event.impl;

import lombok.val;
import me.mikusugar.random.core.bean.SugarJsonNode;
import me.mikusugar.random.core.event.FileEventService;
import me.mikusugar.random.core.utils.SugarJsonUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;

/**
 * @author mikusugar
 */
@Service
public class FileEventServiceImpl implements FileEventService {

  @Override
  public void out(SugarJsonNode sugarJsonNode, int num, String path) throws Exception {
    if (!path.endsWith(".json")) path = path + ".json";
    final File outFile = new File(path);
    final FileWriter out = new FileWriter(outFile);
    int size = num;
    while (size-- > 0) {
      val res = new StringBuilder();
      SugarJsonUtils.toJsonStr(null, sugarJsonNode, res);
      out.write(res.toString());
      if (size > 0) {
        out.write("," + System.lineSeparator());
      }
    }
    out.flush();
    out.close();
  }
}

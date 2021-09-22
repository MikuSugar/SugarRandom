package me.mikusugar.sugar.random.cli.service;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * 模仿 zsh 的 d 命令
 *
 * @author mikusugar
 */
public class DService {
    //最大长度
    private final static int MAX = 10;

    private final Queue<String> queue;


    public DService() {
        this.queue = new ArrayDeque<>();
    }

    public void add(String path) {
        queue.add(path);
        if (queue.size() > MAX) queue.poll();
    }

    public String getShow() {
        int idx = 0;
        StringBuilder sb = new StringBuilder();
        for (String path : queue) {
            sb.append(idx++).append(" \t ").append(path).append(System.lineSeparator());
        }
        return sb.toString();
    }

    public String getPath(int idx) {
        assert idx < queue.size() && idx >= 0;
        int i = 0;
        for (String path : queue) {
            if (i == idx) return path;
            i++;
        }
        return "";
    }

}

package me.mikusugar.random.core.service;

import me.mikusugar.random.core.bean.ConfigSave;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfigSavaRepository
        extends JpaRepository<ConfigSave, String> {

}

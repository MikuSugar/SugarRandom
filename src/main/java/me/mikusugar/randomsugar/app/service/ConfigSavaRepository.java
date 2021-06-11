package me.mikusugar.randomsugar.app.service;

import me.mikusugar.randomsugar.app.bean.ConfigSave;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface ConfigSavaRepository
        extends JpaRepository<ConfigSave, String> {

}

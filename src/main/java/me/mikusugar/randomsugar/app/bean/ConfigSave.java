package me.mikusugar.randomsugar.app.bean;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
public class ConfigSave {

    @Id
    private String id;
    private String json;

}

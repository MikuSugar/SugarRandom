package me.mikusugar.random.core.bean;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Map;

/**
 * @author mikusugar
 */
@Entity
@Data
@NoArgsConstructor
public class AliasConfig {

  @Id
  private String id;

  @ElementCollection(fetch=FetchType.EAGER)
  @JoinTable(joinColumns=@JoinColumn(name="id"))
  @MapKeyColumn(name="aliasName")
  @Column(name="value")
  private Map<String,String> aliasMap;

}

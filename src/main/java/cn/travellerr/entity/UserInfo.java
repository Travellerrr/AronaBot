package cn.travellerr.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.Date;


@Entity(name = "UserInfo")
@Table(name = "UserInfo")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo {

    @Id
    private Long qq;
    private Integer fortuneID;

    @Builder.Default
    private Date generateDate = new Date();
}

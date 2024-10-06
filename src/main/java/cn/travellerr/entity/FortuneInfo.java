package cn.travellerr.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity(name = "FortuneInfo")
@Table(name = "FortuneInfo")
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FortuneInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String fortuneSummary;
    private String luckyStar;
    private String signText;
    private String unSignText;

}

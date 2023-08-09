package com.umc.cmap.domain.user.entity;

import com.umc.cmap.config.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Generated
@Getter
@Entity
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "mates",
                        columnNames = {"from_idx", "to_idx"}
                )
        }
)
public class Mates extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "from_idx")
    private User from;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "to_idx")
    private User to;

    @Builder
    public Mates(User from, User to){
        this.from = from;
        this.to = to;
    }
}

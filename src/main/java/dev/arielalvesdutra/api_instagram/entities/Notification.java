package dev.arielalvesdutra.api_instagram.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.OffsetDateTime;

@Entity
public class Notification {
    @Getter @Setter @Accessors(chain = true)
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @Getter @Setter @Accessors(chain = true)
    private User user;
    @Getter @Setter @Accessors(chain = true)
    private String title;
    @Getter @Accessors(chain = true)
    private boolean opened;
    @Getter @Setter @Accessors(chain = true)
    private OffsetDateTime createdAt = OffsetDateTime.now();
    @Getter @Setter @Accessors(chain = true)
    private OffsetDateTime openedAt;

    public Notification setOpened(boolean opened) {
        this.opened = opened;

        if (opened && openedAt == null) {
            openedAt = OffsetDateTime.now();
        }

        return this;
    }
}

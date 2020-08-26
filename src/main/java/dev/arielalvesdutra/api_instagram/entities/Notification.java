package dev.arielalvesdutra.api_instagram.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

public class Notification {
    @Getter @Setter @Accessors(chain = true)
    private String title;
    @Getter @Accessors(chain = true)
    private boolean read;
    @Getter @Setter @Accessors(chain = true)
    private OffsetDateTime createdAt = OffsetDateTime.now();
    @Getter @Setter @Accessors(chain = true)
    private OffsetDateTime readAt;

    public Notification setRead(boolean read) {
        this.read = read;

        if (readAt == null) {
            readAt = OffsetDateTime.now();
        }

        return this;
    }
}

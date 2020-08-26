package dev.arielalvesdutra.api_instagram.unit.entities;

import dev.arielalvesdutra.api_instagram.entities.Notification;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class NotificationTest {
    @Test
    public void emptyConstructor_shouldWork() {
        new Notification();
    }

    @Test
    public void gettersAndSetters_shouldWork() {
        String title = "User @geralt liked your post";
        boolean read = false;
        OffsetDateTime date = OffsetDateTime.now();

        Notification notification = new Notification()
                .setTitle(title)
                .setCreatedAt(date);

        assertThat(notification).isNotNull();
        assertThat(notification.getTitle()).isEqualTo(title);
        assertThat(notification.getCreatedAt()).isEqualTo(date);
        assertThat(notification.getReadAt()).isNull();
        assertThat(notification.isRead()).isEqualTo(read);
    }

    @Test
    public void setRead_shouldSetReadAt() {
        Notification notification = new Notification();

        notification.setRead(true);

        assertThat(notification.getReadAt()).isNotNull();
    }
}

package dev.arielalvesdutra.api_instagram.unit.entities;

import dev.arielalvesdutra.api_instagram.entities.Notification;
import dev.arielalvesdutra.api_instagram.entities.User;
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
        Long id = 1L;
        String title = "User @geralt liked your post";
        boolean read = false;
        OffsetDateTime date = OffsetDateTime.now();
        User user = new User();

        Notification notification = new Notification()
                .setId(id)
                .setUser(user)
                .setTitle(title)
                .setCreatedAt(date);

        assertThat(notification).isNotNull();
        assertThat(notification.getId()).isEqualTo(id);
        assertThat(notification.getTitle()).isEqualTo(title);
        assertThat(notification.getCreatedAt()).isEqualTo(date);
        assertThat(notification.getOpenedAt()).isNull();
        assertThat(notification.isOpened()).isEqualTo(read);
    }

    @Test
    public void setRead_withTrue_shouldSetReadAt() {
        Notification notification = new Notification();

        notification.setOpened(true);

        assertThat(notification.getOpenedAt()).isNotNull();
    }

    @Test
    public void setRead_withFalse_shouldNotSetReadAt() {
        Notification notification = new Notification();

        notification.setOpened(false);

        assertThat(notification.getOpenedAt()).isNull();
    }
}

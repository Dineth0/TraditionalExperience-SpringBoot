package lk.ijse.gdse.traditionalexperiencebackend.service;

import lk.ijse.gdse.traditionalexperiencebackend.dto.NotificationDTO;

import java.util.List;

public interface NotificationService {
    int createNotification(NotificationDTO notificationDTO);
    List<NotificationDTO> getAllNotifications();
    List<NotificationDTO> getUnReadNotifications(Long userId);
    int markAsRead(Long id);
}

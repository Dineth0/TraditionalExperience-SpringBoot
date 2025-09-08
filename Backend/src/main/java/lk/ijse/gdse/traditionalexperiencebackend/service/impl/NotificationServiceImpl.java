package lk.ijse.gdse.traditionalexperiencebackend.service.impl;

import lk.ijse.gdse.traditionalexperiencebackend.dto.NotificationDTO;
import lk.ijse.gdse.traditionalexperiencebackend.dto.TraditionalItemDTO;
import lk.ijse.gdse.traditionalexperiencebackend.entity.Notification;
import lk.ijse.gdse.traditionalexperiencebackend.entity.TraditionalItem;
import lk.ijse.gdse.traditionalexperiencebackend.entity.User;
import lk.ijse.gdse.traditionalexperiencebackend.repo.NotificationRepo;
import lk.ijse.gdse.traditionalexperiencebackend.repo.UserRepo;
import lk.ijse.gdse.traditionalexperiencebackend.service.NotificationService;
import lk.ijse.gdse.traditionalexperiencebackend.util.VarList;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepo notificationRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public int createNotification(NotificationDTO notificationDTO) {
        try{
            Notification notification = modelMapper.map(notificationDTO, Notification.class);

            if(notificationDTO.getUserId()!= null){
                User user = new User();
                user.setId(notificationDTO.getUserId());
                notification.setUser(user);
            }
            notificationRepo.save(notification);
            return VarList.Created;
        }catch (Exception e){
            return VarList.Bad_Gateway;
        }
    }

    @Override
    public List<NotificationDTO> getAllNotifications() {
        List<Notification> notifications = notificationRepo.findAll();
        return notifications.stream()
                .map(notification -> modelMapper.map(notification, NotificationDTO.class))
                .toList();
    }

    @Override
    public List<NotificationDTO> getUnReadNotifications(Long userId) {
        List<Notification> notifications = notificationRepo.findByUserIdAndReadStatusFalse(userId);
        return notifications.stream()
                .map(notification -> modelMapper.map(notification, NotificationDTO.class))
                .toList();
    }

    @Override
    public int markAsRead(Long userId) {
        
        List<Notification> notifications = notificationRepo.findByUserIdAndReadStatusFalse(userId);
        notifications.forEach(notification -> notification.setReadStatus(true));
        notificationRepo.saveAll(notifications);
        return notifications.size();
    }
}

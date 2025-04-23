package com.fynaloo.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FriendRequest {
    private Long id;
    private Long senderId;
    private String senderUsername;
    private Long receiverId;
    private String receiverUsername;
    private String status;
}

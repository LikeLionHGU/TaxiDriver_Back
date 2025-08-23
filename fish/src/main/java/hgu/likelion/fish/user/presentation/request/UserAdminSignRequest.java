package hgu.likelion.fish.user.presentation.request;


import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAdminSignRequest {
    private String name;
    private String phoneNumber;
    private String location;
}

package hgu.likelion.fish.user.presentation.request;


import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@RequiredArgsConstructor
public class UserAdminSignRequest {
    private String name;
    private String phoneNumber;
    private String location;
}

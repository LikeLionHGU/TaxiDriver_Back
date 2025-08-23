package hgu.likelion.fish.user.presentation.request;


import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserBuyerSignRequest {
    private String personName;
    private String companyName;
    private String phoneNumber;
    private String location;
}

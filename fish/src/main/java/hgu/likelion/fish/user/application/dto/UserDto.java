package hgu.likelion.fish.user.application.dto;


import hgu.likelion.fish.user.presentation.request.UserAdminSignRequest;
import hgu.likelion.fish.user.presentation.request.UserBuyerSignRequest;
import hgu.likelion.fish.user.presentation.request.UserSellerSignRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@RequiredArgsConstructor
public class UserDto {
    private String id;
    private int userStatus;
    private int bidCount;
    private String phoneNumber;
    private int totalBuyPrice;
    private int totalSellPrice;
    private String companyName;
    private String email;
    private String name;
    private String location;

    public static UserDto toUpdateAdmin(UserAdminSignRequest request, String userId) {
        return UserDto.builder()
                .id(userId)
                .phoneNumber(request.getPhoneNumber())
                .location(request.getLocation())
                .name(request.getName())
                .build();
    }

    public static UserDto toUpdateBuyer(UserBuyerSignRequest request, String userId) {
        return UserDto.builder()
                .id(userId)
                .phoneNumber(request.getPhoneNumber())
                .location(request.getLocation())
                .name(request.getPersonName())
                .companyName(request.getCompanyName())
                .build();
    }

    public static UserDto toUpdateSeller(UserSellerSignRequest request, String userId) {
        return UserDto.builder()
                .id(userId)
                .phoneNumber(request.getPhoneNumber())
                .location(request.getLocation())
                .name(request.getPersonName())
                .companyName(request.getCompanyName())
                .build();
    }

}

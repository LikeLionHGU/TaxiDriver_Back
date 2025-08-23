package hgu.likelion.fish.commons.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RegisterStatus {
    REGISTER_READY, REGISTER_SUCCESS, REGISTER_FAILED
}

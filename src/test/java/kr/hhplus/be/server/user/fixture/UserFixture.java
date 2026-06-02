package kr.hhplus.be.server.user.fixture;

import kr.hhplus.be.server.user.domain.model.User;

public class UserFixture {
    public static User create() {
        return User.builder()
                .point(40_000L)
                .build();
    }
}

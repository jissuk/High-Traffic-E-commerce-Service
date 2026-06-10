package kr.hhplus.be.server.user.fixture;

import kr.hhplus.be.server.user.domain.model.User;

public class UserFixture {

    private Long point;

    private UserFixture(){
        this.point = 40_000L;
    }

    public static UserFixture builder(){
        return new UserFixture();
    }

    public User build(){
        return User.builder()
                .point(point)
                .build();
    }

//    public static User create() {
//        return User.builder()
//                .point(40_000L)
//                .build();
//    }
}

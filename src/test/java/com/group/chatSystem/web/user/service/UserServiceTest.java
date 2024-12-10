package com.group.chatSystem.web.user.service;

import com.group.chatSystem.exception.DuplicateException;
import com.group.chatSystem.web.user.domain.Role;
import com.group.chatSystem.web.user.domain.User;
import com.group.chatSystem.web.user.dto.EditInfoForm;
import com.group.chatSystem.web.user.dto.SignUpForm;
import com.group.chatSystem.web.user.repository.RoleRepository;
import com.group.chatSystem.web.user.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ActiveProfiles({"test"})
@Transactional
@SpringBootTest
class UserServiceTest {

    @Autowired
    UserService userService;
    @MockBean
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @MockBean
    UserRepository userRepository;
    @MockBean
    RoleRepository roleRepository;

    @Mock
    User user;
    @Mock
    Role role;

    @Nested
    @DisplayName("회원가입 테스트")
    class SignUpTest {

        @Mock
        SignUpForm signUpForm;

        @BeforeEach
        void setup() {
            doReturn(user).when(userRepository).save(any());
            doReturn(role).when(roleRepository).findByAuthority(any());
            doReturn(Optional.empty()).when(userRepository).findByNicknameAndEnabledIsTrue(any());
            doReturn(Optional.empty()).when(userRepository).findByEmailAndEnabledIsTrue(any());
            doReturn(Optional.empty()).when(userRepository).findByPhoneAndEnabledIsTrue(any());
            doReturn("password").when(signUpForm).password();
        }

        @DisplayName("중복 닉네임이 있을 경우 실패")
        @Test
        void 실패1() {

            //given
            User otherUser = mock(User.class);

            //when
            doReturn(Optional.ofNullable(otherUser)).when(userRepository).findByNicknameAndEnabledIsTrue(any());

            //then
            assertThrows(DuplicateException.class, () -> userService.signUp(signUpForm));
        }

        @DisplayName("중복 이메일이 있을 경우 실패")
        @Test
        void 실패2() {

            //given
            User otherUser = mock(User.class);

            //when
            doReturn(Optional.ofNullable(otherUser)).when(userRepository).findByEmailAndEnabledIsTrue(any());

            //then
            assertThrows(DuplicateException.class, () -> userService.signUp(signUpForm));
        }

        @DisplayName("중복 휴대전화번호가 있을 경우 실패")
        @Test
        void 실패3() {

            //given
            User otherUser = mock(User.class);

            //when
            doReturn(Optional.ofNullable(otherUser)).when(userRepository).findByPhoneAndEnabledIsTrue(any());

            //then
            assertThrows(DuplicateException.class, () -> userService.signUp(signUpForm));
        }

        @DisplayName("모든 검증이 성공하면 user 저장 메서드가 호출됨")
        @Test
        void 성공() {

            //given
            doReturn(user).when(userRepository).save(any());
            doReturn(role).when(roleRepository).findByAuthority(any());

            //when
            userService.signUp(signUpForm);

            //then
            verify(userRepository, times(1)).save(any());
        }

        @DisplayName("bCryptPasswordEncoder 가 1회 encode 하여 비밀번호 암호화를 함")
        @Test
        void 성공2() {

            //given

            //when
            userService.signUp(signUpForm);

            //then
            verify(bCryptPasswordEncoder, times(1)).encode(any());
        }

    }

    @Nested
    @DisplayName("닉네임 변경 테스트")
    class EditNicknameTest {

        @Mock
        EditInfoForm editInfoForm;

        @Test
        @DisplayName("성공 테스트")
        void 성공() {

            //given
            doReturn(Optional.empty()).when(userRepository).findByNicknameAndEnabledIsTrue(any());
            doReturn(Optional.ofNullable(user)).when(userRepository).findById(any());

            //when
            userService.editNickname(1L, editInfoForm);

            //then
            verify(user, times(1)).editNickname(any());
            verify(userRepository, times(1)).save(any());
        }

        @Test
        @DisplayName("이미 동일 닉네임 유저가 존재")
        void 실패1() {

            //given

            //when
            doReturn(Optional.ofNullable(user)).when(userRepository).findByNicknameAndEnabledIsTrue(any());

            //then
            assertThrows(DuplicateException.class, () -> userService.editNickname(1L, editInfoForm));
        }

        @Test
        @DisplayName("바꾸고자하는 대상이 존재하지 않음")
        void 실패2() {

            //given
            doReturn(Optional.empty()).when(userRepository).findByNicknameAndEnabledIsTrue(any());

            //when
            doReturn(Optional.empty()).when(userRepository).findById(any());

            //then
            assertThrows(RuntimeException.class, () -> userService.editNickname(1L, editInfoForm));
        }

        @Test
        @DisplayName("이미 동일 전화번호 유저가 존재")
        void 실패3() {

            //given

            //when
            doReturn(Optional.ofNullable(user)).when(userRepository).findByPhoneAndEnabledIsTrue(any());

            //then
            assertThrows(DuplicateException.class, () -> userService.editNickname(1L, editInfoForm));
        }

    }
}
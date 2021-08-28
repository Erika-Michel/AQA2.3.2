package ru.netology;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static ru.netology.DataGenerator.Registration.getRegisteredUser;
import static ru.netology.DataGenerator.Registration.getUser;
import static ru.netology.DataGenerator.getRandomLogin;
import static ru.netology.DataGenerator.getRandomPassword;

public class AuthTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successfully login with active registered user")
    void shouldSuccessfulLoginIfRegisteredActiveUser() {
        var registeredUser = getRegisteredUser("active");
        $("[type=\"text\"]").setValue(registeredUser.getLogin());
        $("[name=\"password\"]").setValue(registeredUser.getPassword());
        $("[data-test-id=\"action-login\"]").click();
        $("[id = root]").shouldHave(exactText("Личны кабинет"));
    }

    @Test
    @DisplayName("Should get error message if login with not registered user")
    void shouldGetErrorIfNotRegisteredUser() {
        var notRegisteredUser = getUser("active");
        $("[type=\"text\"]").setValue(notRegisteredUser.getLogin());
        $("[name=\"password\"]").setValue(notRegisteredUser.getPassword());
        $("[data-test-id=\"action-login\"]").click();
        $("[class=\"notification__content\"]").shouldBe(visible)
                .shouldHave(text("Ошибка! Неверно указан логи или пароль"));
    }

    @Test
    @DisplayName("Should get error message if login with blocked registered user")
    void shouldGetErrorIfBlockedUser() {
        var blockedUser = getRegisteredUser("blocked");
        $("[type=\"text\"]").setValue(blockedUser.getLogin());
        $("[name=\"password\"]").setValue(blockedUser.getPassword());
        $("[data-test-id=\"action-login\"]").click();
        $("[class=\"notification__content\"]").shouldBe(visible)
                .shouldHave(text("Пользовател заблокирован"));
    }

    @Test
    @DisplayName("Should get error message if login with wrong login")
    void shouldGetErrorIfWrongLogin() {
        var registeredUser = getRegisteredUser("active");
        var wrongLogin = getRandomLogin();
        $("[type=\"text\"]").setValue(wrongLogin);
        $("[name=\"password\"]").setValue(registeredUser.getPassword());
        $("[data-test-id=\"action-login\"]").click();
        $("[class=\"notification__content\"]").shouldBe(visible)
                .shouldHave(text("Ошибка! Неверно указан логи или пароль"));
    }

    @Test
    @DisplayName("Should get error message if login with wrong password")
    void shouldGetErrorIfWrongPassword() {
        var registeredUser = getRegisteredUser("active");
        var wrongPassword = getRandomPassword();
        $("[type=\"text\"]").setValue(registeredUser.getLogin());
        $("[name=\"password\"]").setValue(wrongPassword);
        $("[data-test-id=\"action-login\"]").click();
        $("[class=\"notification__content\"]").shouldBe(visible)
                .shouldHave(text("Ошибка! Неверно указан логи или пароль"));
    }
}

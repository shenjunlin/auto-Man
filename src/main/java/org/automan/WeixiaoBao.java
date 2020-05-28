package org.automan;

import io.github.bonigarcia.wdm.WebDriverManager;
import java.util.Set;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.phantomjs.PhantomJSDriver;

/**
 * Created by shenjunlin on 2020/04/25 10:00 登录微小宝
 */
public class WeixiaoBao {

    public static String loginWxbGetCookie(String userName, String password) {
        WebDriver driver = null;
        try {
            WebDriverManager.phantomjs().useMirror().forceCache().setup();
            driver = new PhantomJSDriver();
            driver.get("http://account.wxb.com/?from=http%3A%2F%2Fwww.wxb.com%2Fhome");
            Thread.sleep(2000);
            WebElement loginTypeBtn = driver.findElement(By.xpath("//div[@class='login-type-toggle___2e_eO']/span[2]"));
            loginTypeBtn.click();
            Thread.sleep(1000);
            WebElement userNameInput = driver.findElement(By.id("email"));
            userNameInput.sendKeys(userName);
            WebElement passElement = driver.findElement(By.id("password"));
            passElement.sendKeys(password);
            WebElement loginBtn = driver.findElement(By.xpath("//button[@type='button']"));
            loginBtn.click();
            Thread.sleep(5000);

            StringBuilder cookieStr = new StringBuilder();
            Set<Cookie> cookies = driver.manage().getCookies();
            for (Cookie cookie : cookies) {
                cookieStr.append(cookie.getName()).append("=").append(cookie.getValue()).append(";").append(" ");
            }
            return cookieStr.substring(0, cookieStr.lastIndexOf(";"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
        return null;
    }
}

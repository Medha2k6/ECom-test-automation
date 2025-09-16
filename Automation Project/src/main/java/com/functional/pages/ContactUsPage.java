package com.functional.pages;

import org.openqa.selenium.By;

public class ContactUsPage {

    // Locators
    public final By LINK_CONTACT = By.xpath("//a[@href='/contact_us' or contains(normalize-space(.),'Contact')]");
    public final By HDR_GET_IN_TOUCH = By.xpath("//h2[contains(translate(.,'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ'),'GET IN TOUCH')]");
    public final By FLD_NAME = By.cssSelector("input[data-qa='name'], input[name='name']");
    public final By FLD_EMAIL = By.cssSelector("input[data-qa='email'], input[name='email']");
    public final By FLD_SUBJ = By.cssSelector("input[data-qa='subject'], input[name='subject']");
    public final By FLD_MSG = By.cssSelector("textarea[data-qa='message'], textarea[name='message']");
    public final By FLD_UPLOAD = By.cssSelector("input[type='file'][name='upload_file']");
    public final By BTN_SUBMIT = By.cssSelector("input[data-qa='submit-button'], input[type='submit'][name='submit']");
    public final By MSG_SUCCESS = By.xpath("//*[contains(translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'success')]");
    public final By BTN_HOME = By.cssSelector("a[href='/']");
    public final By FOOTER_SUB_INPUT = By.cssSelector("input[id='susbscribe_email'], footer input[type='email']");
    public final By FOOTER_SUB_BTN = By.cssSelector("button#subscribe, button.subscribe");
    public final By FEEDBACK_MAIL_LINK = By.xpath("//a[starts-with(@href,'mailto:')]");
}
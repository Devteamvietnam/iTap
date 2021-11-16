package com.devteam.module.account.data.db.sample;

import java.util.Arrays;

import com.devteam.core.module.common.ClientInfo;
import com.devteam.core.util.text.DateUtil;
import com.devteam.module.account.AccountService;
import com.devteam.module.account.NewAccountModel;
import com.devteam.module.account.ProfileLogic;
import com.devteam.module.account.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


public class DigitalData extends AccountData {
  static DateUtil.DateRandomizer DATE_RANDOMIZER = new DateUtil.DateRandomizer("1/1/2017@00:00:00", null);
  static AccountType USER         = AccountType.USER;
  
  @Autowired
  private AccountService service;

  public UserProfile PROFILE;

  public BankAccount BANK_ACCOUNT_1, BANK_ACCOUNT_2;
  public UserRelation RELATION_1, RELATION_2;
  public UserWork     WORK_1, WORK_2;
  public UserIdentity   IDENTITY_1, IDENTITY_2;
  public UserEducation  EDUCATION_1, EDUCATION_2;
 
  public void initialize(ClientInfo client) {
    PROFILE = new UserProfile("lien", "Lien Le",  "lien@digital.com");
    NewAccountModel model = new NewAccountModel().withUserProfile(PROFILE, "lien");
    PROFILE = service.createNewAccount(client, model).getUserProfile();

    BANK_ACCOUNT_1 =
        new BankAccount().
         withLoginId(PROFILE.getLoginId()).
         withAccountHolder("Lien").
            withAccountNumber("125157777");

    BANK_ACCOUNT_2 =
        new BankAccount().
         withLoginId(PROFILE.getLoginId()).
         withAccountHolder("Lien").
         withAccountNumber("158555651");
    service.saveBankAccounts(client, PROFILE.getLoginId(), Arrays.asList(BANK_ACCOUNT_1, BANK_ACCOUNT_2));

    RELATION_1 =
        new UserRelation()
          .withLabel("Lien relation label 1")
          .withLoginId(PROFILE.getLoginId())
          .withFirstName("Le")
          .withLastName("L").withRelation("daughter")
          .withBirthday(DateUtil.parseCompactDate("02/03/1995"))
          .withGender("female")
          .withFullName("Le Lien")
            .withContact("+84123456788")
          .withNote("Sister");

    RELATION_2 =
        new UserRelation()
          .withLabel("Lien relation label 2")
          .withLoginId(PROFILE.getLoginId())
          .withFirstName("Le")
          .withLastName("Lien").withRelation("mother")
          .withBirthday(DateUtil.parseCompactDate("02/03/1960"))
          .withGender("female")
          .withFullName("Lien Le")
          .withContact("+841234567999")
          .withNote("Mother");

    service.saveUserRelations(client, PROFILE.getLoginId(), Arrays.asList(RELATION_1, RELATION_2));

    WORK_1 = new UserWork()
        .withLabel("Lien work label 1")
        .withLoginId(PROFILE.getLoginId())
        .withFrom(DateUtil.parseCompactDate("02/02/2019"))
        .withTo(DateUtil.parseCompactDate("02/02/2021"))
        .withPosition("employee").withOrganization("Tel")
        .withLabel("Sale");

    WORK_2 = new UserWork()
        .withLabel("Lien work label 2")
        .withLoginId(PROFILE.getLoginId())
        .withLabel("Sale")
        .withFrom(DateUtil.parseCompactDate("12/02/2022"))
        .withTo(DateUtil.parseCompactDate("12/02/2023"))
        .withOrganization("Tel").withPosition("employee");
    service.saveUserWorks(client, PROFILE.getLoginId(), Arrays.asList(WORK_1, WORK_2));

    IDENTITY_1 = new UserIdentity().withLabel("Lien identity label 1")
        .withLoginId(PROFILE.getLoginId()).withInfo("CMND", "Lien Le", "258456987", "personal-id");

    IDENTITY_2 = new UserIdentity()
        .withLabel("Lien identity label 2")
        .withLoginId(PROFILE.getLoginId())
        .withInfo("passport", "Lien Hoang", "586741236", "passport");
    service.saveUserIdentitys(client, PROFILE.getLoginId(), Arrays.asList(IDENTITY_1, IDENTITY_2));

    EDUCATION_1 = new UserEducation()
        .withLabel("Lien education label 1")
        .withLoginId(PROFILE.getLoginId())
        .withSchoolOrInstitute("FPT University")
        .withLanguage("English")
        .withMajor("IT")
        .withCertificate("Dai Hoc")
        .withFrom(DateUtil.parseCompactDate("12/03/214"))
        .withTo(DateUtil.parseCompactDate("12/03/2018"));

    EDUCATION_2 = new UserEducation()
        .withLabel("Lien education label 2")
        .withLoginId(PROFILE.getLoginId())
        .withSchoolOrInstitute("Dai Hoc Back Khoa")
        .withCertificate("Dai Hoc").withMajor("IT");
    service.saveUserEducations(client, PROFILE.getLoginId(), Arrays.asList(EDUCATION_1, EDUCATION_2));
    jpaService.getEntityManager().flush();
  }

  @Transactional
  public void assertAll(ClientInfo client) throws Exception {
    ProfileLogic logic = accountService.getProfileLogic();
    
    new BankAssert(client, PROFILE.getLoginId())
    .assertLoadList(2)
    .assertSaveList(Arrays.asList(BANK_ACCOUNT_1, BANK_ACCOUNT_2));
  }
}

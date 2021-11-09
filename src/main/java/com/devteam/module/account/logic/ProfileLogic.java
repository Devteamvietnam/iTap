package com.devteam.module.account.logic;

import com.devteam.CachingConfig;
import com.devteam.core.DAOService;
import com.devteam.module.account.entity.Account;
import com.devteam.module.account.entity.AccountType;
import com.devteam.module.account.entity.BaseProfile;
import com.devteam.module.account.entity.UserProfile;
import com.devteam.module.account.repository.UserProfileRepository;
import com.devteam.module.common.ClientInfo;
import com.devteam.module.http.upload.UploadResource;
import com.devteam.module.http.upload.UploadService;
import com.devteam.module.storage.IStorageService;
import com.devteam.module.storage.StorageResource;
import com.devteam.module.storage.UserStorage;
import com.devteam.util.avatar.AvatarUtil;
import com.devteam.util.ds.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
public class ProfileLogic extends DAOService {
    final static String PROFILE_PREFIX_USER = "account_user_profile" ;

    @Autowired
    private AccountLogic accountLogic;

    @Autowired
    private UserProfileRepository userProfileRepo;

    @Autowired
    private UploadService uploadService;

    @Autowired
    private IStorageService storageService;

    public <T extends BaseProfile> T getProfile(ClientInfo clientInfo, String loginId) {
        Account account = accountLogic.getAccount(clientInfo, loginId);
        if(account == null) return null;
        if(account.getAccountType() == AccountType.USER) {
            return (T) getProfile(clientInfo, loginId);
        }
        return null;
    }

    @Cacheable(value = CachingConfig.REGION_ENTITY , key= "{'" + PROFILE_PREFIX_USER + "', #loginId}")
    public UserProfile getUserProfile(ClientInfo clientInfo, String loginId) {
        return userProfileRepo.getByLoginId(loginId);
    }

    @CacheEvict(value = CachingConfig.REGION_ENTITY , key= "{'" + PROFILE_PREFIX_USER + "', #profile.loginId}")
    public UserProfile saveUserProfile(ClientInfo client, UserProfile profile) {
        profile.set(client);
        Account account = accountLogic.getModifiableAccount(client, profile.getLoginId());
        account.setEmail(profile.getEmail());
        account.setMobile(profile.getMobile());
        account.setFullName(profile.getFullName());
        accountLogic.save(account);
        profile = userProfileRepo.save(profile);
        return profile;
    }

    public UploadResource uploadAvatar(ClientInfo client, String loginId, UploadResource resource, boolean saveOrigin) {
        Account account = accountLogic.getAccount(client, loginId);
        if(account == null) {
            Objects.assertNotNull(account, "Cannot find the account for {}", loginId);
        }
        byte[] imgData = uploadService.load(resource.getStoreId());
        byte[] pngImgData = AvatarUtil.toPng(imgData);
        UserStorage storage = storageService.createUserStorage(client, loginId);
        if(saveOrigin) {
            StorageResource origAvatarResource = new StorageResource("orig-avatar.png", pngImgData);
            origAvatarResource = storage.wwwSave("avatar", origAvatarResource);
        }
        StorageResource avatarResource = new StorageResource("avatar.png", pngImgData);
        avatarResource = storage.wwwSave("avatar", avatarResource);

        if(account.getAccountType() == AccountType.USER) {
            UserProfile profile = getUserProfile(client, loginId) ;
            profile.setAvatarUrl(avatarResource.getPublicDownloadUri());
            saveUserProfile(client, profile);
        } else {
            throw new RuntimeException("Cannot update");
        }
        return resource;
    }
}

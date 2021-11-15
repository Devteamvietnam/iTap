package com.devteam.core.data.db.entity;

import com.devteam.core.common.ClientInfo;

public interface ICompanyEntity  {
    public String getCompanyId() ;
    public void setCompanyId(String id);
    public void set(ClientInfo client, String companyId) ;
}


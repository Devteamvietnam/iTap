package com.devteam.security.controller;

import com.devteam.core.query.SqlQueryParams;
import com.devteam.module.account.controller.BaseAccountController;
import com.devteam.module.http.rest.RestResponse;
import com.devteam.module.http.rest.v1.BaseController;
import com.devteam.security.SecurityService;
import com.devteam.security.entity.AppAccessPermission;
import com.devteam.security.entity.AppPermission;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class AppSecurityController extends BaseAccountController {
    @Autowired
    private SecurityService service;

    protected AppSecurityController() {
        super("security", "/security");
    }

    @ApiOperation(value = "Save Permissions", responseContainer="List", response = AppPermission.class)
    @PutMapping("app/permissions")
    public @ResponseBody
    RestResponse savePermissions(HttpSession session, @RequestBody List<AppPermission> permissions) {
        Callable<List<AppPermission>> executor = () -> {
            AppSecurityController.ClientContext ctx = getClientContext(session);
            List<AppPermission> list = new ArrayList<>();
            for (AppPermission sel : permissions) {
                list.add(sel);
            }
            return service.saveAppPermissions(ctx.getClientInfo(), list);
        };
        return execute(BaseController.Method.PUT, "app/permissions", executor);
    }

    @ApiOperation(value = "Delete Permissions", response = Boolean.class)
    @DeleteMapping("app/permissions/delete")
    public @ResponseBody RestResponse deletePermissions(HttpSession session, @RequestBody List<Long> permissionIds) {
        Callable<Boolean> executor = () -> {
            AppSecurityController.ClientContext ctx = getClientContext(session);
            return service.deletePermissionsById(ctx.getClientInfo(), permissionIds);
        };
        return execute(BaseController.Method.DELETE, "app/permissions/delete", executor);
    }

    @ApiOperation(value = "Search Permission", responseContainer = "List", response = AppAccessPermission.class)
    @PostMapping("app/permission/search")
    public @ResponseBody RestResponse  searchCompanies(HttpSession session, @RequestBody SqlQueryParams params) {
        Callable<List<AppAccessPermission>> executor = () -> {
            AppSecurityController.ClientContext ctx = getClientContext(session);
            return service.searchPermissions(ctx.getClientInfo(), params);

        };
        return execute(BaseController.Method.POST, "app/permission/search", executor);
    }
}

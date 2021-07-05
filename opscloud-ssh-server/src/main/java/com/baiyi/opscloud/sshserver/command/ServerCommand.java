package com.baiyi.opscloud.sshserver.command;

import com.baiyi.opscloud.domain.param.server.ServerParam;
import com.baiyi.opscloud.sshserver.annotation.InvokeSessionUser;
import com.baiyi.opscloud.sshserver.annotation.ScreenClear;
import com.baiyi.opscloud.sshserver.command.base.BaseServerCommand;
import com.baiyi.opscloud.sshserver.command.component.SshShellComponent;
import com.baiyi.opscloud.sshserver.command.context.ListServerCommand;
import com.baiyi.opscloud.sshserver.command.context.SessionCommandContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

/**
 * @Author baiyi
 * @Date 2021/6/7 11:47 上午
 * @Version 1.0
 */
@Slf4j
@SshShellComponent
@ShellCommandGroup("Server")
public class ServerCommand extends BaseServerCommand {

    @ScreenClear
    @InvokeSessionUser(invokeAdmin = true)
    @ShellMethod(value = "List server", key = {"ls", "list"})
    public void listServer(@ShellOption(help = "Server Name", defaultValue = "") String name, @ShellOption(help = "IP", defaultValue = "") String ip) {
        String sessionId = buildSessionId();
        ServerParam.UserPermissionServerPageQuery pageQuery = ServerParam.UserPermissionServerPageQuery.builder()
                .name(name)
                .queryIp(ip)
                .build();
        pageQuery.setPage(1);
        ListServerCommand commandContext = ListServerCommand.builder()
                .sessionId(sessionId)
                .username(helper.getSshSession().getUsername())
                .queryParam(pageQuery)
                .build();
        doListServer(commandContext);
    }

    @ScreenClear
    @InvokeSessionUser(invokeAdmin = true)
    @ShellMethod(value = "List server before page", key = "b")
    public void beforePage() {
        String sessionId = buildSessionId();
        ServerParam.UserPermissionServerPageQuery pageQuery = SessionCommandContext.getServerQuery();
        if (pageQuery != null) {
            pageQuery.setPage(pageQuery.getPage() > 1 ? pageQuery.getPage() - 1 : pageQuery.getPage());
            ListServerCommand listServerCommand = ListServerCommand.builder()
                    .sessionId(sessionId)
                    .username(helper.getSshSession().getUsername())
                    .queryParam(pageQuery)
                    .build();
            doListServer(listServerCommand);
        } else {
            listServer("", "");
        }
    }

    @ScreenClear
    @InvokeSessionUser(invokeAdmin = true)
    @ShellMethod(value = "List server next page", key = "n")
    public void nextPage() {
        String sessionId = buildSessionId();
        ServerParam.UserPermissionServerPageQuery pageQuery = SessionCommandContext.getServerQuery();
        if (pageQuery != null) {
            pageQuery.setPage(pageQuery.getPage() + 1);
            ListServerCommand listServerCommand = ListServerCommand.builder()
                    .sessionId(sessionId)
                    .username(helper.getSshSession().getUsername())
                    .queryParam(pageQuery)
                    .build();
            doListServer(listServerCommand);
        } else {
            listServer("", "");
        }
    }


}

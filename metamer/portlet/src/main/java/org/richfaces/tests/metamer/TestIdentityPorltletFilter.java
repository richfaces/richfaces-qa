package org.richfaces.tests.metamer;

import java.io.IOException;
import java.security.Principal;

import javax.portlet.PortletException;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.filter.FilterChain;
import javax.portlet.filter.FilterConfig;
import javax.portlet.filter.RenderFilter;
import javax.portlet.filter.RenderRequestWrapper;

import org.richfaces.tests.metamer.bean.UserBean;

public class TestIdentityPorltletFilter implements RenderFilter {

    @Override
    public void init(FilterConfig filterConfig) throws PortletException {
    }

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(RenderRequest request, RenderResponse response, FilterChain chain) throws IOException,
            PortletException {
        RenderRequestWrapper wrapper = new RenderRequestWrapper(request) {

            private UserBean getUserBean() {
                PortletSession session = getPortletSession(false);
                if (session != null) {
                    return (UserBean) session.getAttribute("userBean");
                }
                return null;
            }

            @Override
            public boolean isUserInRole(String role) {
                UserBean userBean = getUserBean();
                if (userBean != null) {
                    return userBean.isUserInRole(role);
                }
                return false;
            }

            @Override
            public Principal getUserPrincipal() {
                UserBean userBean = getUserBean();
                if (userBean != null) {
                    return userBean.getPrincipal();
                }
                return null;
            }

            @Override
            public String getRemoteUser() {
                UserBean userBean = getUserBean();
                if (userBean != null) {
                    return userBean.getRolename();
                }
                return null;
            }
        };

        chain.doFilter(wrapper, response);
    }

}

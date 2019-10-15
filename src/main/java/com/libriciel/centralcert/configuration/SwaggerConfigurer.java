/*
 * Central-Cert Core
 * Copyright (C) 2019 Libriciel-SCOP
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.libriciel.centralcert.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.AuthorizationCodeGrantBuilder;
import springfox.documentation.builders.AuthorizationScopeBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.SecurityConfigurationBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static springfox.documentation.spi.DocumentationType.SWAGGER_2;


@Configuration
@ConditionalOnProperty(name = "keycloak.enabled", havingValue = "true", matchIfMissing = true)
@Slf4j
public class SwaggerConfigurer {

    @Value("${keycloak.resource}")
    private String resource;

    @Value("${swagger.oauth2.client-id}")
    private String clientId;

    @Value("${keycloak.auth-server-url}")
    private String authServer;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${application.version?:DEVELOP}")
    private String version;


    @Bean
    public SecurityConfiguration securityConfiguration() {
        return SecurityConfigurationBuilder.builder()
                .clientId(clientId).realm(realm).appName(resource)
                .additionalQueryStringParams(Collections.singletonMap("nonce", UUID.randomUUID().toString()))
                .build();
    }


    @Bean
    public Docket api() {
        return new Docket(SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.libriciel.centralcert"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo())
                .useDefaultResponseMessages(false)
                .securitySchemes(buildSecurityScheme())
                .securityContexts(buildSecurityContext());
    }


    private ApiInfo apiInfo() {
        return new ApiInfo(
                "Central-Cert Core",
                "Main core application.\n" +
                        "\n" +
                        "The main link between every sub-services, integrating business code logic.",
                version,
                "https://www.libriciel.fr/",
                new Contact("Libriciel SCOP", "http://libriciel.fr", "iparapheur@libriciel.coop"),
                "Affero GPL 3.0",
                "https://www.gnu.org/licenses/agpl-3.0.en.html",
                Collections.emptyList()
        );
    }


    private List<SecurityContext> buildSecurityContext() {
        SecurityReference securityReference = SecurityReference.builder().reference("oauth2").scopes(scopes().toArray(new AuthorizationScope[]{})).build();
        SecurityContext context = SecurityContext.builder().forPaths(s -> true)
                .securityReferences(Collections.singletonList(securityReference))
                .build();

        return Collections.singletonList(context);
    }


    private List<? extends SecurityScheme> buildSecurityScheme() {
        GrantType grantType = new AuthorizationCodeGrantBuilder()
                .tokenEndpoint(
                        new TokenEndpoint(authServer + "/realms/" + realm + "/protocol/openid-connect/token", "oauthtoken"))
                .tokenRequestEndpoint(
                        new TokenRequestEndpoint(authServer + "/realms/" + realm + "/protocol/openid-connect/auth", clientId, ""))
                .build();

        return Collections.singletonList(
                new OAuth("oauth2", scopes(), Collections.singletonList(grantType))
        );
    }


    private List<AuthorizationScope> scopes() {
        List<AuthorizationScope> scopes = new ArrayList<>();

        for (String scopeItem : new String[]{"openid=openid", "profile=profile"}) {
            String[] scope = scopeItem.split("=");
            if (scope.length == 2) {
                scopes.add(new AuthorizationScopeBuilder().scope(scope[0]).description(scope[1]).build());
            } else {
                log.warn("Scope '{}' is not valid (format is scope=description)", scopeItem);
            }
        }

        return scopes;
    }

}

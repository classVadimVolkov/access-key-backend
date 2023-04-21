package com.example.accesskeybackend.template.controller;

import com.example.accesskeybackend.exception.InvalidSiteUrlException;
import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.Record;
import org.xbill.DNS.TextParseException;
import org.xbill.DNS.Type;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/web")
@Validated
public class AccessKeyIPSupportController {

    @GetMapping("/checkIPv6Support")
    public boolean checkIPv6Support(@RequestParam @NotBlank(message = "Site URL shouldn't be empty")
                                        final String siteUrl) {
        try {
            Record[] records = new Lookup(getValidatedSiteUrl(siteUrl), Type.AAAA).run();
            return records != null;
        } catch (TextParseException e) {
            throw new InvalidSiteUrlException(String.format(
                    "Invalid site URL: %s", siteUrl
            ));
        }
    }

    private String getValidatedSiteUrl(final String siteUrl) {
        if (siteUrl.startsWith("http")) {
            URI uri;
            try {
                uri = new URI(siteUrl);
            } catch (URISyntaxException e) {
                throw new InvalidSiteUrlException(String.format(
                        "Invalid site URL: %s", siteUrl
                ));
            }
            return uri.getHost();
        } else if (isValidDomain(siteUrl)) {
            return siteUrl;
        }

        throw new InvalidSiteUrlException(String.format(
                "Invalid site URL: %s", siteUrl
        ));
    }

    private boolean isValidDomain(final String str) {
        if (str == null) {
            return false;
        }

        String regex = "^((?!-)[A-Za-z0-9-]{1,63}(?<!-)\\.)+[A-Za-z]{2,6}";
        return Pattern.compile(regex).matcher(str).matches();
    }
}

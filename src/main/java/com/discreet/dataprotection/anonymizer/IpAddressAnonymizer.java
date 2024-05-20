package com.discreet.dataprotection.anonymizer;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
    Anonymizer for ip4 and ip6 addresses.
 */
public class IpAddressAnonymizer extends BaseAnonymizer {
    private final int MAX_IP_NUMBER_PART = 255;

    @Override
    public String anonymize(String input) {
        boolean isIp4 = input.indexOf(".") >= 0;
        return isIp4 ? translateIp4Address(input): translateIp6Address(input);
    }

    private String translateIp4Address(String input) {
        String[] tokens = input.split("\\.");
        return Arrays.stream(tokens).map(this::traslateByteAsString)
                .collect(Collectors.joining("."));
    }

    private String traslateByteAsString(String token) {
        return String.valueOf(traslateSingleIpNumber(Integer.parseInt(token)));
    }

    private String traslateHexByteAsString(String token) {
        return String.format("%02X", traslateSingleIpNumber(Integer.parseInt(token, 16)));
    }

    private String translateIp6Address(String input) {
        String[] tokens = input.split(":");
        return Arrays.stream(tokens).map(tokenPair ->
                    traslateHexByteAsString(tokenPair.substring(0, 2)) +
                    traslateHexByteAsString(tokenPair.substring(2, 4))
                )
                .collect(Collectors.joining(":"));
    }

    private Integer traslateSingleIpNumber(int originalNum) {
        return (originalNum + Math.abs(getRandom().nextInt())) % (MAX_IP_NUMBER_PART + 1);
    }

}

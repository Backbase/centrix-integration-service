package com.backbase.accelerators.centrix.client;

public interface CentrixClient {

    String getOneTimePassword(String userId, String systemIdCode);
}

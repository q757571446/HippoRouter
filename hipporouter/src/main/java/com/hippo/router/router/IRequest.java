package com.hippo.router.router;

import android.net.Uri;

import java.util.List;
import java.util.Map;

/**
 * Created by kevin on 16-11-16.
 * the responsibility of IRequest is parse url
 */

public interface IRequest {

    String getUrl();

    String getScheme();

    String getHost();

    int getPort();

    List<String> getPath();

    Map<String, String> getParameters();
}

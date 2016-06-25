package com.docudile.app.services.impl;

import com.docudile.app.data.dto.OcrSdkTaskResponse;
import com.docudile.app.services.OpticalRecognitionService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by franc on 5/4/2016.
 */
@Service("opticalRecognitionService")
@Transactional
public class OpticalRecognitionServiceImpl implements OpticalRecognitionService {

    private static final Logger logger = LoggerFactory.getLogger(OpticalRecognitionServiceImpl.class);

    private final String APP_ID = "Docudi.le";
    private final String APP_PASSWORD = "BhBTNk1HZ5N5dyi2aCDp+KZR";

    private static byte[] encodeData;
    private static final String CHAR_SET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";

    private final String PROCESS_IMAGE = "https://cloud.ocrsdk.com/processImage";
    private final String TASK_STATUS = "https://cloud.ocrsdk.com/getTaskStatus";

    static {
        encodeData = new byte[64];
        for (int i = 0; i < 64; i++) {
            byte c = (byte) CHAR_SET.charAt(i);
            encodeData[i] = c;
        }
    }

    @Override
    public List<String> extract(File file) {
        List<String> lines = new ArrayList<>();
        String authentication = authentication();
        OcrSdkTaskResponse requestProcess = requestProcessing(file, authentication);
        String output = download(waitForResultUrl(requestProcess.getOcrSdkTask().getId(), authentication));
        for (String line : output.split("\r\n")) {
            if (!StringUtils.isBlank(line)) {
                lines.add(StringUtils.trim(line).replaceAll("\\s+", " "));
            }
        }
        return lines;
    }

    public OcrSdkTaskResponse requestProcessing(File file, String authentication) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + authentication);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.set("file", new FileSystemResource(file));
        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(body, headers);
        ResponseEntity<OcrSdkTaskResponse> result = restTemplate.exchange(PROCESS_IMAGE + "?profile=textExtraction&exportFormat=txt", HttpMethod.POST, entity, OcrSdkTaskResponse.class);
        if (result.getStatusCode().is2xxSuccessful()) {
            return result.getBody();
        }
        return null;
    }

    public String waitForResultUrl(String taskId, String authentication) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + authentication);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<OcrSdkTaskResponse> result;
        do {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            result = restTemplate.exchange(TASK_STATUS + "?taskId=" + taskId, HttpMethod.GET, entity, OcrSdkTaskResponse.class);
        } while (!result.getBody().getOcrSdkTask().getStatus().equalsIgnoreCase("completed"));
        return result.getBody().getOcrSdkTask().getResultUrl();
    }

    public String download(String resultUrl) {
        RestTemplate restTemplate = new RestTemplate();
        try {
            URI uri = new URI(resultUrl);
            ResponseEntity<byte[]> result = restTemplate.getForEntity(uri, byte[].class);
            if (result.getStatusCode().is2xxSuccessful()) {
                return new String(result.getBody());
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String authentication() {
        String toEncode = APP_ID + ":" + APP_PASSWORD;
        return encode(toEncode);
    }

    private String encode(String s) {
        return encode(s.getBytes());
    }

    /**
     * base-64 encode a byte array
     *
     * @param src The byte array to encode
     * @returns The base64 encoded result
     */
    private String encode(byte[] src) {
        return encode(src, 0, src.length);
    }

    /**
     * base-64 encode a byte array
     *
     * @param src    The byte array to encode
     * @param start  The starting index
     * @param length The number of bytes
     * @returns The base64 encoded result
     */
    private String encode(byte[] src, int start, int length) {
        byte[] dst = new byte[(length + 2) / 3 * 4 + length / 72];
        int x = 0;
        int dstIndex = 0;
        int state = 0;    // which char in pattern
        int old = 0;    // previous byte
        int len = 0;    // length decoded so far
        int max = length + start;
        for (int srcIndex = start; srcIndex < max; srcIndex++) {
            x = src[srcIndex];
            switch (++state) {
                case 1:
                    dst[dstIndex++] = encodeData[(x >> 2) & 0x3f];
                    break;
                case 2:
                    dst[dstIndex++] = encodeData[((old << 4) & 0x30)
                            | ((x >> 4) & 0xf)];
                    break;
                case 3:
                    dst[dstIndex++] = encodeData[((old << 2) & 0x3C)
                            | ((x >> 6) & 0x3)];
                    dst[dstIndex++] = encodeData[x & 0x3F];
                    state = 0;
                    break;
            }
            old = x;
            if (++len >= 72) {
                dst[dstIndex++] = (byte) '\n';
                len = 0;
            }
        }

	/*
     * now clean up the end bytes
	 */

        switch (state) {
            case 1:
                dst[dstIndex++] = encodeData[(old << 4) & 0x30];
                dst[dstIndex++] = (byte) '=';
                dst[dstIndex++] = (byte) '=';
                break;
            case 2:
                dst[dstIndex++] = encodeData[(old << 2) & 0x3c];
                dst[dstIndex++] = (byte) '=';
                break;
        }
        return new String(dst);
    }

    /**
     * A Base64 decoder.  This implementation is slow, and
     * doesn't handle wrapped lines.
     * The output is undefined if there are errors in the input.
     *
     * @param s a Base64 encoded string
     * @returns The byte array eith the decoded result
     */

    private byte[] decode(String s) {
        int end = 0;    // end state
        if (s.endsWith("=")) {
            end++;
        }
        if (s.endsWith("==")) {
            end++;
        }
        int len = (s.length() + 3) / 4 * 3 - end;
        byte[] result = new byte[len];
        int dst = 0;
        try {
            for (int src = 0; src < s.length(); src++) {
                int code = CHAR_SET.indexOf(s.charAt(src));
                if (code == -1) {
                    break;
                }
                switch (src % 4) {
                    case 0:
                        result[dst] = (byte) (code << 2);
                        break;
                    case 1:
                        result[dst++] |= (byte) ((code >> 4) & 0x3);
                        result[dst] = (byte) (code << 4);
                        break;
                    case 2:
                        result[dst++] |= (byte) ((code >> 2) & 0xf);
                        result[dst] = (byte) (code << 6);
                        break;
                    case 3:
                        result[dst++] |= (byte) (code & 0x3f);
                        break;
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        return result;
    }

}

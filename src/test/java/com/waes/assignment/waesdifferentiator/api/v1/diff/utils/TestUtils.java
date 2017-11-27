package com.waes.assignment.waesdifferentiator.api.v1.diff.utils;

import com.waes.assignment.waesdifferentiator.api.v1.diff.dto.DiffSideDTO;
import com.waes.assignment.waesdifferentiator.api.v1.diff.model.DiffPair;

import java.util.Base64;

/**
 * Created by lucas on 26/11/2017.
 */
public class TestUtils {

    public static DiffPair createDiffPair(Long diffId, String leftSideContent, String rightSideContent) {
        DiffPair diffPair = new DiffPair();
        diffPair.setDiffId(diffId);
        diffPair.setLeftSide(leftSideContent);
        diffPair.setRightSide(rightSideContent);

        return diffPair;
    }

    public static DiffSideDTO createDiffSideDTO(String diffSideContent) {
        DiffSideDTO diffSideDTO = new DiffSideDTO();
        diffSideDTO.setDiffSideContent(diffSideContent);
        return diffSideDTO;
    }

    public static String encodeBase64String(String simpleText) {
        byte[] base64EncodedBytes = Base64.getEncoder().encode(simpleText.getBytes());

        return new String(base64EncodedBytes);
    }

    public static Long getRandomId() {
        long leftLimit = 1L;
        long rightLimit = 10000L;
        return leftLimit + (long) (Math.random() * (rightLimit - leftLimit));
    }
}

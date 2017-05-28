package com.nonda.dtc.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by whaley on 2017/5/28.
 */

public class DTCError {
    private List<String> mErrors = new ArrayList<>();

    public static DTCError fromString(String error) {
        DTCError dtcError = new DTCError();
        String errorCode = error.substring(error.indexOf(",") + 1);
        String[] errorList = errorCode.split("&");
        for (int i = 0; i < errorList.length; i++) {
            String oneError = errorList[i];
            if (oneError.trim().length() > 3) {
                dtcError.mErrors.add(oneError);
            }
        }

        return dtcError;
    }

    public List<String> getErrors() {
        return mErrors;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (String error : mErrors) {
            stringBuilder.append(error).append(" ");
        }
        return stringBuilder.toString();
    }
}

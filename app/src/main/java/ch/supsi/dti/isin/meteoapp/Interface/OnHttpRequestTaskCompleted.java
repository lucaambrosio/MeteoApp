package ch.supsi.dti.isin.meteoapp.Interface;

import java.io.IOException;

public interface OnHttpRequestTaskCompleted {
    void onHttpRequestTaskCompleted(String result) throws IOException;
}

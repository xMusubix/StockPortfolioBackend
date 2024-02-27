package own.watcharapon.payload;

import lombok.*;

import java.util.List;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BotExchangeRatePayload {
    private Result result;

    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class Result {
        private String api;
        private String timestamp;
        private Data data;
    }

    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class Data {
        private DataHeader data_header;
        private List<DataDetail> data_detail;
    }

    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class DataHeader {
        private String report_name_eng;
        private String report_name_th;
        private String report_uoq_name_eng;
        private String report_uoq_name_th;
        private List<ReportSourceOfData> report_source_of_data;
        private List<ReportRemark> report_remark;
        private String last_updated;
    }

    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class ReportSourceOfData {
        private String source_of_data_eng;
        private String source_of_data_th;
    }

    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class ReportRemark {
        private String report_remark_eng;
        private String report_remark_th;
    }

    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class DataDetail {
        private String period;
        private String currency_id;
        private String currency_name_th;
        private String currency_name_eng;
        private String buying_sight;
        private String buying_transfer;
        private String selling;
        private String mid_rate;
    }
}

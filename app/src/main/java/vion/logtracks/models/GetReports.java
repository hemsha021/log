package vion.logtracks.models;

import java.util.List;

public class GetReports extends BaseApiResponse {


    /**
     * result : [{"online_created":"2020-02-05 11:25:41","offline_last_seen":"2020-02-05 05:55:57","offline_created":"2020-02-05 11:44:36"},{"online_created":"2020-02-05 11:25:22","offline_last_seen":"2020-02-05 05:55:57","offline_created":"2020-02-05 11:44:36"},{"online_created":"2020-02-05 10:04:14","offline_last_seen":"2020-02-05 05:55:57","offline_created":"2020-02-05 11:44:36"},{"online_created":"2020-02-05 10:03:50","offline_last_seen":"2020-02-05 05:55:57","offline_created":"2020-02-05 11:44:36"},{"online_created":"2020-02-05 10:01:01","offline_last_seen":"2020-02-05 05:55:57","offline_created":"2020-02-05 11:44:36"}]
     * weekly_day_reports : [{"online_created":"2020-02-05 11:25:41","offline_last_seen":"2020-02-05 05:55:57","offline_created":"2020-02-05 11:44:36"},{"online_created":"2020-02-05 11:25:22","offline_last_seen":"2020-02-05 05:55:57","offline_created":"2020-02-05 11:44:36"},{"online_created":"2020-02-05 10:04:14","offline_last_seen":"2020-02-05 05:55:57","offline_created":"2020-02-05 11:44:36"},{"online_created":"2020-02-05 10:03:50","offline_last_seen":"2020-02-05 05:55:57","offline_created":"2020-02-05 11:44:36"},{"online_created":"2020-02-05 10:01:01","offline_last_seen":"2020-02-05 05:55:57","offline_created":"2020-02-05 11:44:36"}]
     * new_status : Offline
     */

    private String new_status;
    private List<ResultBean> result;
    private List<WeeklyDayReportsBean> weekly_day_reports;

    public String getNew_status() {
        return new_status;
    }

    public void setNew_status(String new_status) {
        this.new_status = new_status;
    }

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public List<WeeklyDayReportsBean> getWeekly_day_reports() {
        return weekly_day_reports;
    }

    public void setWeekly_day_reports(List<WeeklyDayReportsBean> weekly_day_reports) {
        this.weekly_day_reports = weekly_day_reports;
    }

    public static class ResultBean {
        /**
         * online_created : 2020-02-05 11:25:41
         * offline_last_seen : 2020-02-05 05:55:57
         * offline_created : 2020-02-05 11:44:36
         */

        private String online_created;
        private String offline_last_seen;
        private String offline_created;

        public String getOnline_created() {
            return online_created;
        }

        public void setOnline_created(String online_created) {
            this.online_created = online_created;
        }

        public String getOffline_last_seen() {
            return offline_last_seen;
        }

        public void setOffline_last_seen(String offline_last_seen) {
            this.offline_last_seen = offline_last_seen;
        }

        public String getOffline_created() {
            return offline_created;
        }

        public void setOffline_created(String offline_created) {
            this.offline_created = offline_created;
        }
    }

    public static class WeeklyDayReportsBean {
        /**
         * online_created : 2020-02-05 11:25:41
         * offline_last_seen : 2020-02-05 05:55:57
         * offline_created : 2020-02-05 11:44:36
         */

        private String online_created;
        private String offline_last_seen;
        private String offline_created;

        public String getOnline_created() {
            return online_created;
        }

        public void setOnline_created(String online_created) {
            this.online_created = online_created;
        }

        public String getOffline_last_seen() {
            return offline_last_seen;
        }

        public void setOffline_last_seen(String offline_last_seen) {
            this.offline_last_seen = offline_last_seen;
        }

        public String getOffline_created() {
            return offline_created;
        }

        public void setOffline_created(String offline_created) {
            this.offline_created = offline_created;
        }
    }
}

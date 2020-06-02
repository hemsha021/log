package vion.logtracks.models;

import java.util.List;

public class CreateUser extends BaseApiResponse {

    private List<ResultBean> result;

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * id : 3
         * email : 7877838890477
         * password :
         * token : asfasdafasfsdsdfsafsadfsdf7
         * device_type : 2
         * plan_id : 0
         * start : 2020-01-28 13:24:41
         * end : 2020-01-28 21:25:44
         * number_count : 0
         * asign_ip :
         * user_type : normal
         */

        private String id;
        private String email;
        private String password;
        private String token;
        private String device_type;
        private String plan_id;
        private String start;
        private String end;
        private String number_count;
        private String asign_ip;
        private String user_type;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getDevice_type() {
            return device_type;
        }

        public void setDevice_type(String device_type) {
            this.device_type = device_type;
        }

        public String getPlan_id() {
            return plan_id;
        }

        public void setPlan_id(String plan_id) {
            this.plan_id = plan_id;
        }

        public String getStart() {
            return start;
        }

        public void setStart(String start) {
            this.start = start;
        }

        public String getEnd() {
            return end;
        }

        public void setEnd(String end) {
            this.end = end;
        }

        public String getNumber_count() {
            return number_count;
        }

        public void setNumber_count(String number_count) {
            this.number_count = number_count;
        }

        public String getAsign_ip() {
            return asign_ip;
        }

        public void setAsign_ip(String asign_ip) {
            this.asign_ip = asign_ip;
        }

        public String getUser_type() {
            return user_type;
        }

        public void setUser_type(String user_type) {
            this.user_type = user_type;
        }
    }
}

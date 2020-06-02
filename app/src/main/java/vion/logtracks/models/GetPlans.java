package vion.logtracks.models;

import java.util.List;

public class GetPlans extends BaseApiResponse {

    private List<ResultBean> result;

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * id : 1
         * name : Weekly Plan
         * price : 1.99
         * days : 7
         * number_count : 1
         * description : Per Week
         */

        private String id;
        private String name;
        private String price;
        private String days;
        private String number_count;
        private String description;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getDays() {
            return days;
        }

        public void setDays(String days) {
            this.days = days;
        }

        public String getNumber_count() {
            return number_count;
        }

        public void setNumber_count(String number_count) {
            this.number_count = number_count;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }
}

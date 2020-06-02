package vion.logtracks.network;


class AppConstant {

    companion object {

        var base64Key="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEApexvSCdK7s2TKR8wIS95YaVhvkq4GMJCpU6Jeg/c2CzW/xPFLw6mcsfJUBLEvGmAl/qfhQ5LJ6zYlQDrMwZOPMcrb34V4KZ/ZqJzEiBpMMOU8HK/PikYOFS6hc3tqH/iay0RLRfbNCRXkbTj1SvcEWixBexHy8GL/MJRsTu260ToJzVWd2HQC3cz/qMdrZTlzh/3sRYCVPSEW6DrmXWFX0CoRWj2QWr7faD5TQPZol1bzxvLnclmRbb/ijsGwhTntQcI7f/XYROA0VWFQFCAMvxNBnDHl48SO6GaPfQ+5TwivhpeaKoZ8p7zIQS7bv0IO+LVuIZAimBWkdNhjYJ3gwIDAQAB"

        var PlanMonthly="sub_monthly"
        var PlanWeekly="sub_weekly"
        var PlanThreeMonthly="sub_three_month"

        var PRIVACY_POLICY = "https://app.termly.io/document/privacy-notice/e481c7ba-5dac-464c-acf4-eacc7b893b5e"

        val APISUCCESS = 1
        val APIFAILED = 0
        val BASEURL = "http://18.221.80.189:3000/"


        //TODO - Track fragment
        //For recycler View(Done - Track fragment)
        val GET_REPORT = "getReports"

        //TODO - Splash Activity
        //When the app is installed (Done - Splash Activity)
        val CREATE_USER = "createUser"


        //(TODO - Add Fragment)
        //When the User Clicks on Start Tracking (Done - Splash Activity)
        val SUSCRIBE_USER = "subscribeUser"

        //Get User (Done - Splash Activity)
        val GET_SUSCRIBER = "getSubscriber"

        //(Done - Splash Activity)
        val UNSUSCRIBE_USER = "unSubscribeUser"


        //(Todo - PremiumPlan Activity ---Recycler View remaininfg)
        //For Premium Plan Activity
        val GET_PLANS = "getPlans"

        val PAYMENT_SUCCESS = "successPayment"

        //Device Type
        var DeviceType = "1"

    }

}

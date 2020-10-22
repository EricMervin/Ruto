package com.quarantino.ruto.HelperClasses;

import java.util.List;

public class parsertrial {

    /**
     * html_attributions : []
     * result : {"address_components":[{"long_name":"Sector 18","short_name":"Sector 18","types":["sublocality_level_1","sublocality","political"]},{"long_name":"Noida","short_name":"Noida","types":["locality","political"]},{"long_name":"Gautam Buddh Nagar","short_name":"Gautam Buddh Nagar","types":["administrative_area_level_2","political"]},{"long_name":"Uttar Pradesh","short_name":"UP","types":["administrative_area_level_1","political"]},{"long_name":"India","short_name":"IN","types":["country","political"]},{"long_name":"201301","short_name":"201301","types":["postal_code"]}],"adr_address":"L05 &amp; L06, DLF Mall of India, <span class=\"extended-address\">Sector 18<\/span>, <span class=\"locality\">Noida<\/span>, <span class=\"region\">Uttar Pradesh<\/span> <span class=\"postal-code\">201301<\/span>, <span class=\"country-name\">India<\/span>","business_status":"OPERATIONAL","formatted_address":"L05 & L06, DLF Mall of India, Sector 18, Noida, Uttar Pradesh 201301, India","geometry":{"location":{"lat":28.56742849999999,"lng":77.321135},"viewport":{"northeast":{"lat":28.5689006302915,"lng":77.32282633029149},"southwest":{"lat":28.5662026697085,"lng":77.32012836970848}}},"icon":"https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/generic_business-71.png","name":"Snow World","photos":[{"height":855,"html_attributions":["<a href=\"https://maps.google.com/maps/contrib/104211784272730444621\">monty bobby<\/a>"],"photo_reference":"CmRaAAAAkvxu06wEHzLl81Fa-RFjZo7t40sbgbgetbF3RsZ30QoC2osYVUfq8egEcPvksNtXDC_BgEosmj20V79gqDQZIwyVFj1P2nyMyFyPmlmyZe8Rg1RHCuhu3pdkwRNr1HboEhAgkNyIwYE6r61GL-BWE3SiGhSoYKNlIZDOeNp7E_1QFKNAH8rBXw","width":1280},{"height":3118,"html_attributions":["<a href=\"https://maps.google.com/maps/contrib/102797128579967527517\">rajesh kumar<\/a>"],"photo_reference":"CmRaAAAAzjntlwzQ6ifjAvm4DcAONKzGt1LJ-7USRpb24DUo978PsQBzOHwm2hHVaFQ6tGYO_FPt2rGn5Lc7cyeFz0SoHwUt4E-QnWYzuFdlIjIYea395i3ST43kfaxHcPjk2HeeEhDga_6FmcjhklyylMzDnRB1GhTeuOyZz0rFcODy3UDfpzvp3ziy6A","width":4158},{"height":842,"html_attributions":["<a href=\"https://maps.google.com/maps/contrib/118445050271447741098\">Shivamm Rastogi<\/a>"],"photo_reference":"CmRaAAAAIILggYOq2CFJGvIVYSMA7znLT4LdZoE72Ig61ILSSbPFYXk3AuUSB--5batpiTBU87LM3C-qDOGVMHp5iQHiOjQOy_h1mS9wKZrkXi2k_qDFNcwIW_TDe8NwwnsG2BZlEhDnVULYJheEBw-gaURbKdE1GhTQq4V8gwbmRFiBfS8r3E_m2GCeAA","width":1280},{"height":1920,"html_attributions":["<a href=\"https://maps.google.com/maps/contrib/109438908512732461685\">Sai Praneeth Konduru<\/a>"],"photo_reference":"CmRaAAAAkRysb03KQKvnbK6prk4TUM5Lt5PycpT_RaNB47cs5Dw4EsU9kxGdx6jKI1BHqHBSd2X1wqRFLsn7lwGJDeMiQWXj9-ThFn8PySHHET8EHyBfs1Tj5h-CBeMXLULoF3b9EhA5BbaupQyZodpiIzQTfQVaGhS08ktRcKQ7bucIF6rcOo9cq0_0wQ","width":1080},{"height":4000,"html_attributions":["<a href=\"https://maps.google.com/maps/contrib/118445050271447741098\">Shivamm Rastogi<\/a>"],"photo_reference":"CmRaAAAABtmx-eU8M0RDk7SXLXegiADJEfo2-G8woCk6VgQP_Ya-jACoF5aPZoobL7QKnM5Si-fsuHpE3qXD4ZmW4vMSrXL0wC6Rii4Cijz_G-DqzDIVkTb9j91NpIL5lqjKJKACEhC2_wM7wLSILYC8DqbVQPQXGhRGtYVQSHr5t3syjn6GI_QiLNJ0Wg","width":3000},{"height":3024,"html_attributions":["<a href=\"https://maps.google.com/maps/contrib/111631599690858508776\">Puneet Kumar<\/a>"],"photo_reference":"CmRaAAAAUMyQEfgt-DEb5l15-JxuZrmW39wXnxMsaY3Rgnw2PJtsFOTIgs3lWgCLYWNeL3T7sMYP2uR3RyyF_px405bg7Zz5M2Vad7u4htljRhZw2CWsUOlJB9OQBBmQ6UH2UqKREhBh9pXcMcA0Dbz_dYpCwiGbGhQ0uhxKq2Zs4Ms34nYlFQbv5bFUJw","width":4032},{"height":3120,"html_attributions":["<a href=\"https://maps.google.com/maps/contrib/107057767456963592204\">Sachin Kumar<\/a>"],"photo_reference":"CmRaAAAAWNy_iWJis7BvKiWYQ-49dDCv4BRZ01TWiigWvk-Qkf8M48HXA7CR1PJgQcylVPAP_z3M4qZgTDKSN9ZrbAbwwqfEwGpYlSsrCFv9z18NrWklzIk4axgOVGMBOqobM67SEhBQo4mpjjbrfHmZjEyETyoiGhTWpx0N35L6x4KCRlIS83KMJz4P3A","width":4160},{"height":3024,"html_attributions":["<a href=\"https://maps.google.com/maps/contrib/111069388916397538778\">DIVYANSH DAHIYA<\/a>"],"photo_reference":"CmRaAAAAzUDK_2Zr6JPnmSvwGRgN83i-y2zOtFI5yDyT4bZFR2caP5txs_pGvdY_-6km-dPzI85TfxZu3FEHLEj47ac3mFPXovTSN02aqVRXGWlICgcg0x6e4gGb_094t2ET5o_-EhD9Ix6bMGsV_XIJwPL98JdGGhQ2BdzAcz6_NwRlfC7PFCYqzdI4cA","width":4032},{"height":3456,"html_attributions":["<a href=\"https://maps.google.com/maps/contrib/112013559868118152146\">Rahul Sagar<\/a>"],"photo_reference":"CmRaAAAAawyLJgm6EgnVWCNStE8krlqPKWoIVmHVwYoMrPz2fyMye23cwmcs33sSOFLCkU7BidZPIVN_yI0AiZPmQgsAqafkbG_6z3XAavaQeHrRIj3V0kJ-JwmmFsfl4IQrQRSMEhAU4gADRBTCMtX-QrxIAL9jGhSJt7cuTcvZGAKp0pGG901yTkr6Wg","width":3456},{"height":3120,"html_attributions":["<a href=\"https://maps.google.com/maps/contrib/109921301083512292909\">Sauhard Sharma<\/a>"],"photo_reference":"CmRaAAAAPoeOB9-Iph3Lhpz5RcU3Mj_ELX5dPWGwMgAozyLxuh4Cs5FObPE6s1E5O0_l6CyYxsOoL4RCR-MT9IQxNmddraLXWT0f5lAbwr-YHKJRdmAOm-A8ZvJil16pYAuPRu6-EhDArxmTxu_ueD9QoT4gpGdWGhTTrSRwdzER9M1NJHTCk4BII6UStg","width":4160}],"place_id":"ChIJ-yw3J0bkDDkRMTxIHAK_Ous","plus_code":{"compound_code":"H88C+XF Noida, Uttar Pradesh, India","global_code":"7JWVH88C+XF"},"rating":4.1,"reference":"ChIJ-yw3J0bkDDkRMTxIHAK_Ous","reviews":[{"author_name":"AISHA KUMARI","author_url":"https://www.google.com/maps/contrib/104675812655178591862/reviews","language":"en","profile_photo_url":"https://lh3.googleusercontent.com/a-/AOh14Gi0hDM94qlwhCoyzImCHLDcpDYt0qeASC-DUazQ=s128-c0x00000000-cc-rp-mo","rating":4,"relative_time_description":"a month ago","text":"i loved this place.though jackets were a little smelly but apart from that its a fun place.","time":1599750517},{"author_name":"naman jain","author_url":"https://www.google.com/maps/contrib/104609273720080602866/reviews","language":"en","profile_photo_url":"https://lh3.googleusercontent.com/a-/AOh14GhGhKPk-M0f1EQtSQFipY_hWApO_NB2kw9sWHfapg=s128-c0x00000000-cc-rp-mo-ba4","rating":5,"relative_time_description":"a month ago","text":"This place is really good if you want to experience winters and see snow at any time of the year here in noida itself. You don't need to travel far away to experience this.\nThey have plenty of activities that you can do inside\nAs a whole, this place is amazing","time":1599914987},{"author_name":"Minni Jain","author_url":"https://www.google.com/maps/contrib/111659439169897897469/reviews","language":"en","profile_photo_url":"https://lh3.googleusercontent.com/a-/AOh14Gif2ircK_Q3wZyto-2xEh3RucZDb1aemDXd3GLRnw=s128-c0x00000000-cc-rp-mo","rating":5,"relative_time_description":"in the last week","text":"Very adventurous place to visit with your family or loved ones . You really gonna enjoy the snow world. This place is totally worth visit.","time":1603031407},{"author_name":"Manav Goel","author_url":"https://www.google.com/maps/contrib/102750610701018316175/reviews","language":"en","profile_photo_url":"https://lh3.googleusercontent.com/a-/AOh14GjDDTwyxEAgV7AqOLFpgXC2mVYyHcnVwzT0oTHrRw=s128-c0x00000000-cc-rp-mo-ba4","rating":1,"relative_time_description":"a month ago","text":"Yesterday I went there and you know what If u want to spend 1000INR just for ice skating ,then it's your choice but I would suggest people who have done snow activities or have went to any hill station not to go here. It's just a very small park , stinky and uncomfortable clothes, worse snow - just waste of money. Instead go to smaaash . They have other activities also but they all are very boring and not thrilling at all. Instead if you want to just experience snow first in your lifetime then it's ok but instead if u have a budget , go to Narkanda in March to experience the real and natural skiing and other thrilling activities.","time":1599291510},{"author_name":"Dharmveer Singh","author_url":"https://www.google.com/maps/contrib/114005531995962728069/reviews","language":"en","profile_photo_url":"https://lh3.googleusercontent.com/a-/AOh14GhWKnjnxM9bCSjpIoOrb9pKPfFlEC8sUEgiCOW4=s128-c0x00000000-cc-rp-mo","rating":4,"relative_time_description":"a month ago","text":"It's awesome place for enjoy in summers","time":1599153641}],"types":["tourist_attraction","amusement_park","point_of_interest","establishment"],"url":"https://maps.google.com/?cid=16950070163347618865","user_ratings_total":8840,"utc_offset":330,"vicinity":"L05 & L06, DLF Mall of India, Sector 18, Noida","website":"http://www.snowworlddelhi.com/"}
     * status : OK
     */

    private ResultBean result;
    private String status;
    private List<?> html_attributions;

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<?> getHtml_attributions() {
        return html_attributions;
    }

    public void setHtml_attributions(List<?> html_attributions) {
        this.html_attributions = html_attributions;
    }

    public static class ResultBean {
        /**
         * address_components : [{"long_name":"Sector 18","short_name":"Sector 18","types":["sublocality_level_1","sublocality","political"]},{"long_name":"Noida","short_name":"Noida","types":["locality","political"]},{"long_name":"Gautam Buddh Nagar","short_name":"Gautam Buddh Nagar","types":["administrative_area_level_2","political"]},{"long_name":"Uttar Pradesh","short_name":"UP","types":["administrative_area_level_1","political"]},{"long_name":"India","short_name":"IN","types":["country","political"]},{"long_name":"201301","short_name":"201301","types":["postal_code"]}]
         * adr_address : L05 &amp; L06, DLF Mall of India, <span class="extended-address">Sector 18</span>, <span class="locality">Noida</span>, <span class="region">Uttar Pradesh</span> <span class="postal-code">201301</span>, <span class="country-name">India</span>
         * business_status : OPERATIONAL
         * formatted_address : L05 & L06, DLF Mall of India, Sector 18, Noida, Uttar Pradesh 201301, India
         * geometry : {"location":{"lat":28.56742849999999,"lng":77.321135},"viewport":{"northeast":{"lat":28.5689006302915,"lng":77.32282633029149},"southwest":{"lat":28.5662026697085,"lng":77.32012836970848}}}
         * icon : https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/generic_business-71.png
         * name : Snow World
         * photos : [{"height":855,"html_attributions":["<a href=\"https://maps.google.com/maps/contrib/104211784272730444621\">monty bobby<\/a>"],"photo_reference":"CmRaAAAAkvxu06wEHzLl81Fa-RFjZo7t40sbgbgetbF3RsZ30QoC2osYVUfq8egEcPvksNtXDC_BgEosmj20V79gqDQZIwyVFj1P2nyMyFyPmlmyZe8Rg1RHCuhu3pdkwRNr1HboEhAgkNyIwYE6r61GL-BWE3SiGhSoYKNlIZDOeNp7E_1QFKNAH8rBXw","width":1280},{"height":3118,"html_attributions":["<a href=\"https://maps.google.com/maps/contrib/102797128579967527517\">rajesh kumar<\/a>"],"photo_reference":"CmRaAAAAzjntlwzQ6ifjAvm4DcAONKzGt1LJ-7USRpb24DUo978PsQBzOHwm2hHVaFQ6tGYO_FPt2rGn5Lc7cyeFz0SoHwUt4E-QnWYzuFdlIjIYea395i3ST43kfaxHcPjk2HeeEhDga_6FmcjhklyylMzDnRB1GhTeuOyZz0rFcODy3UDfpzvp3ziy6A","width":4158},{"height":842,"html_attributions":["<a href=\"https://maps.google.com/maps/contrib/118445050271447741098\">Shivamm Rastogi<\/a>"],"photo_reference":"CmRaAAAAIILggYOq2CFJGvIVYSMA7znLT4LdZoE72Ig61ILSSbPFYXk3AuUSB--5batpiTBU87LM3C-qDOGVMHp5iQHiOjQOy_h1mS9wKZrkXi2k_qDFNcwIW_TDe8NwwnsG2BZlEhDnVULYJheEBw-gaURbKdE1GhTQq4V8gwbmRFiBfS8r3E_m2GCeAA","width":1280},{"height":1920,"html_attributions":["<a href=\"https://maps.google.com/maps/contrib/109438908512732461685\">Sai Praneeth Konduru<\/a>"],"photo_reference":"CmRaAAAAkRysb03KQKvnbK6prk4TUM5Lt5PycpT_RaNB47cs5Dw4EsU9kxGdx6jKI1BHqHBSd2X1wqRFLsn7lwGJDeMiQWXj9-ThFn8PySHHET8EHyBfs1Tj5h-CBeMXLULoF3b9EhA5BbaupQyZodpiIzQTfQVaGhS08ktRcKQ7bucIF6rcOo9cq0_0wQ","width":1080},{"height":4000,"html_attributions":["<a href=\"https://maps.google.com/maps/contrib/118445050271447741098\">Shivamm Rastogi<\/a>"],"photo_reference":"CmRaAAAABtmx-eU8M0RDk7SXLXegiADJEfo2-G8woCk6VgQP_Ya-jACoF5aPZoobL7QKnM5Si-fsuHpE3qXD4ZmW4vMSrXL0wC6Rii4Cijz_G-DqzDIVkTb9j91NpIL5lqjKJKACEhC2_wM7wLSILYC8DqbVQPQXGhRGtYVQSHr5t3syjn6GI_QiLNJ0Wg","width":3000},{"height":3024,"html_attributions":["<a href=\"https://maps.google.com/maps/contrib/111631599690858508776\">Puneet Kumar<\/a>"],"photo_reference":"CmRaAAAAUMyQEfgt-DEb5l15-JxuZrmW39wXnxMsaY3Rgnw2PJtsFOTIgs3lWgCLYWNeL3T7sMYP2uR3RyyF_px405bg7Zz5M2Vad7u4htljRhZw2CWsUOlJB9OQBBmQ6UH2UqKREhBh9pXcMcA0Dbz_dYpCwiGbGhQ0uhxKq2Zs4Ms34nYlFQbv5bFUJw","width":4032},{"height":3120,"html_attributions":["<a href=\"https://maps.google.com/maps/contrib/107057767456963592204\">Sachin Kumar<\/a>"],"photo_reference":"CmRaAAAAWNy_iWJis7BvKiWYQ-49dDCv4BRZ01TWiigWvk-Qkf8M48HXA7CR1PJgQcylVPAP_z3M4qZgTDKSN9ZrbAbwwqfEwGpYlSsrCFv9z18NrWklzIk4axgOVGMBOqobM67SEhBQo4mpjjbrfHmZjEyETyoiGhTWpx0N35L6x4KCRlIS83KMJz4P3A","width":4160},{"height":3024,"html_attributions":["<a href=\"https://maps.google.com/maps/contrib/111069388916397538778\">DIVYANSH DAHIYA<\/a>"],"photo_reference":"CmRaAAAAzUDK_2Zr6JPnmSvwGRgN83i-y2zOtFI5yDyT4bZFR2caP5txs_pGvdY_-6km-dPzI85TfxZu3FEHLEj47ac3mFPXovTSN02aqVRXGWlICgcg0x6e4gGb_094t2ET5o_-EhD9Ix6bMGsV_XIJwPL98JdGGhQ2BdzAcz6_NwRlfC7PFCYqzdI4cA","width":4032},{"height":3456,"html_attributions":["<a href=\"https://maps.google.com/maps/contrib/112013559868118152146\">Rahul Sagar<\/a>"],"photo_reference":"CmRaAAAAawyLJgm6EgnVWCNStE8krlqPKWoIVmHVwYoMrPz2fyMye23cwmcs33sSOFLCkU7BidZPIVN_yI0AiZPmQgsAqafkbG_6z3XAavaQeHrRIj3V0kJ-JwmmFsfl4IQrQRSMEhAU4gADRBTCMtX-QrxIAL9jGhSJt7cuTcvZGAKp0pGG901yTkr6Wg","width":3456},{"height":3120,"html_attributions":["<a href=\"https://maps.google.com/maps/contrib/109921301083512292909\">Sauhard Sharma<\/a>"],"photo_reference":"CmRaAAAAPoeOB9-Iph3Lhpz5RcU3Mj_ELX5dPWGwMgAozyLxuh4Cs5FObPE6s1E5O0_l6CyYxsOoL4RCR-MT9IQxNmddraLXWT0f5lAbwr-YHKJRdmAOm-A8ZvJil16pYAuPRu6-EhDArxmTxu_ueD9QoT4gpGdWGhTTrSRwdzER9M1NJHTCk4BII6UStg","width":4160}]
         * place_id : ChIJ-yw3J0bkDDkRMTxIHAK_Ous
         * plus_code : {"compound_code":"H88C+XF Noida, Uttar Pradesh, India","global_code":"7JWVH88C+XF"}
         * rating : 4.1
         * reference : ChIJ-yw3J0bkDDkRMTxIHAK_Ous
         * reviews : [{"author_name":"AISHA KUMARI","author_url":"https://www.google.com/maps/contrib/104675812655178591862/reviews","language":"en","profile_photo_url":"https://lh3.googleusercontent.com/a-/AOh14Gi0hDM94qlwhCoyzImCHLDcpDYt0qeASC-DUazQ=s128-c0x00000000-cc-rp-mo","rating":4,"relative_time_description":"a month ago","text":"i loved this place.though jackets were a little smelly but apart from that its a fun place.","time":1599750517},{"author_name":"naman jain","author_url":"https://www.google.com/maps/contrib/104609273720080602866/reviews","language":"en","profile_photo_url":"https://lh3.googleusercontent.com/a-/AOh14GhGhKPk-M0f1EQtSQFipY_hWApO_NB2kw9sWHfapg=s128-c0x00000000-cc-rp-mo-ba4","rating":5,"relative_time_description":"a month ago","text":"This place is really good if you want to experience winters and see snow at any time of the year here in noida itself. You don't need to travel far away to experience this.\nThey have plenty of activities that you can do inside\nAs a whole, this place is amazing","time":1599914987},{"author_name":"Minni Jain","author_url":"https://www.google.com/maps/contrib/111659439169897897469/reviews","language":"en","profile_photo_url":"https://lh3.googleusercontent.com/a-/AOh14Gif2ircK_Q3wZyto-2xEh3RucZDb1aemDXd3GLRnw=s128-c0x00000000-cc-rp-mo","rating":5,"relative_time_description":"in the last week","text":"Very adventurous place to visit with your family or loved ones . You really gonna enjoy the snow world. This place is totally worth visit.","time":1603031407},{"author_name":"Manav Goel","author_url":"https://www.google.com/maps/contrib/102750610701018316175/reviews","language":"en","profile_photo_url":"https://lh3.googleusercontent.com/a-/AOh14GjDDTwyxEAgV7AqOLFpgXC2mVYyHcnVwzT0oTHrRw=s128-c0x00000000-cc-rp-mo-ba4","rating":1,"relative_time_description":"a month ago","text":"Yesterday I went there and you know what If u want to spend 1000INR just for ice skating ,then it's your choice but I would suggest people who have done snow activities or have went to any hill station not to go here. It's just a very small park , stinky and uncomfortable clothes, worse snow - just waste of money. Instead go to smaaash . They have other activities also but they all are very boring and not thrilling at all. Instead if you want to just experience snow first in your lifetime then it's ok but instead if u have a budget , go to Narkanda in March to experience the real and natural skiing and other thrilling activities.","time":1599291510},{"author_name":"Dharmveer Singh","author_url":"https://www.google.com/maps/contrib/114005531995962728069/reviews","language":"en","profile_photo_url":"https://lh3.googleusercontent.com/a-/AOh14GhWKnjnxM9bCSjpIoOrb9pKPfFlEC8sUEgiCOW4=s128-c0x00000000-cc-rp-mo","rating":4,"relative_time_description":"a month ago","text":"It's awesome place for enjoy in summers","time":1599153641}]
         * types : ["tourist_attraction","amusement_park","point_of_interest","establishment"]
         * url : https://maps.google.com/?cid=16950070163347618865
         * user_ratings_total : 8840
         * utc_offset : 330
         * vicinity : L05 & L06, DLF Mall of India, Sector 18, Noida
         * website : http://www.snowworlddelhi.com/
         */

        private String adr_address;
        private String business_status;
        private String formatted_address;
        private GeometryBean geometry;
        private String icon;
        private String name;
        private String place_id;
        private PlusCodeBean plus_code;
        private double rating;
        private String reference;
        private String url;
        private int user_ratings_total;
        private int utc_offset;
        private String vicinity;
        private String website;
        private List<AddressComponentsBean> address_components;
        private List<PhotosBean> photos;
        private List<ReviewsBean> reviews;
        private List<String> types;

        public String getAdr_address() {
            return adr_address;
        }

        public void setAdr_address(String adr_address) {
            this.adr_address = adr_address;
        }

        public String getBusiness_status() {
            return business_status;
        }

        public void setBusiness_status(String business_status) {
            this.business_status = business_status;
        }

        public String getFormatted_address() {
            return formatted_address;
        }

        public void setFormatted_address(String formatted_address) {
            this.formatted_address = formatted_address;
        }

        public GeometryBean getGeometry() {
            return geometry;
        }

        public void setGeometry(GeometryBean geometry) {
            this.geometry = geometry;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPlace_id() {
            return place_id;
        }

        public void setPlace_id(String place_id) {
            this.place_id = place_id;
        }

        public PlusCodeBean getPlus_code() {
            return plus_code;
        }

        public void setPlus_code(PlusCodeBean plus_code) {
            this.plus_code = plus_code;
        }

        public double getRating() {
            return rating;
        }

        public void setRating(double rating) {
            this.rating = rating;
        }

        public String getReference() {
            return reference;
        }

        public void setReference(String reference) {
            this.reference = reference;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getUser_ratings_total() {
            return user_ratings_total;
        }

        public void setUser_ratings_total(int user_ratings_total) {
            this.user_ratings_total = user_ratings_total;
        }

        public int getUtc_offset() {
            return utc_offset;
        }

        public void setUtc_offset(int utc_offset) {
            this.utc_offset = utc_offset;
        }

        public String getVicinity() {
            return vicinity;
        }

        public void setVicinity(String vicinity) {
            this.vicinity = vicinity;
        }

        public String getWebsite() {
            return website;
        }

        public void setWebsite(String website) {
            this.website = website;
        }

        public List<AddressComponentsBean> getAddress_components() {
            return address_components;
        }

        public void setAddress_components(List<AddressComponentsBean> address_components) {
            this.address_components = address_components;
        }

        public List<PhotosBean> getPhotos() {
            return photos;
        }

        public void setPhotos(List<PhotosBean> photos) {
            this.photos = photos;
        }

        public List<ReviewsBean> getReviews() {
            return reviews;
        }

        public void setReviews(List<ReviewsBean> reviews) {
            this.reviews = reviews;
        }

        public List<String> getTypes() {
            return types;
        }

        public void setTypes(List<String> types) {
            this.types = types;
        }

        public static class GeometryBean {
            /**
             * location : {"lat":28.56742849999999,"lng":77.321135}
             * viewport : {"northeast":{"lat":28.5689006302915,"lng":77.32282633029149},"southwest":{"lat":28.5662026697085,"lng":77.32012836970848}}
             */

            private LocationBean location;
            private ViewportBean viewport;

            public LocationBean getLocation() {
                return location;
            }

            public void setLocation(LocationBean location) {
                this.location = location;
            }

            public ViewportBean getViewport() {
                return viewport;
            }

            public void setViewport(ViewportBean viewport) {
                this.viewport = viewport;
            }

            public static class LocationBean {
                /**
                 * lat : 28.56742849999999
                 * lng : 77.321135
                 */

                private double lat;
                private double lng;

                public double getLat() {
                    return lat;
                }

                public void setLat(double lat) {
                    this.lat = lat;
                }

                public double getLng() {
                    return lng;
                }

                public void setLng(double lng) {
                    this.lng = lng;
                }
            }

            public static class ViewportBean {
                /**
                 * northeast : {"lat":28.5689006302915,"lng":77.32282633029149}
                 * southwest : {"lat":28.5662026697085,"lng":77.32012836970848}
                 */

                private NortheastBean northeast;
                private SouthwestBean southwest;

                public NortheastBean getNortheast() {
                    return northeast;
                }

                public void setNortheast(NortheastBean northeast) {
                    this.northeast = northeast;
                }

                public SouthwestBean getSouthwest() {
                    return southwest;
                }

                public void setSouthwest(SouthwestBean southwest) {
                    this.southwest = southwest;
                }

                public static class NortheastBean {
                    /**
                     * lat : 28.5689006302915
                     * lng : 77.32282633029149
                     */

                    private double lat;
                    private double lng;

                    public double getLat() {
                        return lat;
                    }

                    public void setLat(double lat) {
                        this.lat = lat;
                    }

                    public double getLng() {
                        return lng;
                    }

                    public void setLng(double lng) {
                        this.lng = lng;
                    }
                }

                public static class SouthwestBean {
                    /**
                     * lat : 28.5662026697085
                     * lng : 77.32012836970848
                     */

                    private double lat;
                    private double lng;

                    public double getLat() {
                        return lat;
                    }

                    public void setLat(double lat) {
                        this.lat = lat;
                    }

                    public double getLng() {
                        return lng;
                    }

                    public void setLng(double lng) {
                        this.lng = lng;
                    }
                }
            }
        }

        public static class PlusCodeBean {
            /**
             * compound_code : H88C+XF Noida, Uttar Pradesh, India
             * global_code : 7JWVH88C+XF
             */

            private String compound_code;
            private String global_code;

            public String getCompound_code() {
                return compound_code;
            }

            public void setCompound_code(String compound_code) {
                this.compound_code = compound_code;
            }

            public String getGlobal_code() {
                return global_code;
            }

            public void setGlobal_code(String global_code) {
                this.global_code = global_code;
            }
        }

        public static class AddressComponentsBean {
            /**
             * long_name : Sector 18
             * short_name : Sector 18
             * types : ["sublocality_level_1","sublocality","political"]
             */

            private String long_name;
            private String short_name;
            private List<String> types;

            public String getLong_name() {
                return long_name;
            }

            public void setLong_name(String long_name) {
                this.long_name = long_name;
            }

            public String getShort_name() {
                return short_name;
            }

            public void setShort_name(String short_name) {
                this.short_name = short_name;
            }

            public List<String> getTypes() {
                return types;
            }

            public void setTypes(List<String> types) {
                this.types = types;
            }
        }

        public static class PhotosBean {
            /**
             * height : 855
             * html_attributions : ["<a href=\"https://maps.google.com/maps/contrib/104211784272730444621\">monty bobby<\/a>"]
             * photo_reference : CmRaAAAAkvxu06wEHzLl81Fa-RFjZo7t40sbgbgetbF3RsZ30QoC2osYVUfq8egEcPvksNtXDC_BgEosmj20V79gqDQZIwyVFj1P2nyMyFyPmlmyZe8Rg1RHCuhu3pdkwRNr1HboEhAgkNyIwYE6r61GL-BWE3SiGhSoYKNlIZDOeNp7E_1QFKNAH8rBXw
             * width : 1280
             */

            private int height;
            private String photo_reference;
            private int width;
            private List<String> html_attributions;

            public int getHeight() {
                return height;
            }

            public void setHeight(int height) {
                this.height = height;
            }

            public String getPhoto_reference() {
                return photo_reference;
            }

            public void setPhoto_reference(String photo_reference) {
                this.photo_reference = photo_reference;
            }

            public int getWidth() {
                return width;
            }

            public void setWidth(int width) {
                this.width = width;
            }

            public List<String> getHtml_attributions() {
                return html_attributions;
            }

            public void setHtml_attributions(List<String> html_attributions) {
                this.html_attributions = html_attributions;
            }
        }

        public static class ReviewsBean {
            /**
             * author_name : AISHA KUMARI
             * author_url : https://www.google.com/maps/contrib/104675812655178591862/reviews
             * language : en
             * profile_photo_url : https://lh3.googleusercontent.com/a-/AOh14Gi0hDM94qlwhCoyzImCHLDcpDYt0qeASC-DUazQ=s128-c0x00000000-cc-rp-mo
             * rating : 4
             * relative_time_description : a month ago
             * text : i loved this place.though jackets were a little smelly but apart from that its a fun place.
             * time : 1599750517
             */

            private String author_name;
            private String author_url;
            private String language;
            private String profile_photo_url;
            private int rating;
            private String relative_time_description;
            private String text;
            private int time;

            public String getAuthor_name() {
                return author_name;
            }

            public void setAuthor_name(String author_name) {
                this.author_name = author_name;
            }

            public String getAuthor_url() {
                return author_url;
            }

            public void setAuthor_url(String author_url) {
                this.author_url = author_url;
            }

            public String getLanguage() {
                return language;
            }

            public void setLanguage(String language) {
                this.language = language;
            }

            public String getProfile_photo_url() {
                return profile_photo_url;
            }

            public void setProfile_photo_url(String profile_photo_url) {
                this.profile_photo_url = profile_photo_url;
            }

            public int getRating() {
                return rating;
            }

            public void setRating(int rating) {
                this.rating = rating;
            }

            public String getRelative_time_description() {
                return relative_time_description;
            }

            public void setRelative_time_description(String relative_time_description) {
                this.relative_time_description = relative_time_description;
            }

            public String getText() {
                return text;
            }

            public void setText(String text) {
                this.text = text;
            }

            public int getTime() {
                return time;
            }

            public void setTime(int time) {
                this.time = time;
            }
        }
    }
}

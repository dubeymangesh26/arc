package com.arcltd.staff.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PDFListResponse {

    private List<PdfList> pdf_list;
    @SerializedName("ResponseCode")
    private String responseCode;
    private String message;

    public List<PdfList> getPdf_list() {
        return pdf_list;
    }

    public void setPdf_list(List<PdfList> pdf_list) {
        this.pdf_list = pdf_list;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class PdfList {
        private String p_id;
        private String title;
        private String sub_title;
        private String descript;
        private String link;
        private String updated_date;
        private String created_date;

        public String getP_id() {
            return p_id;
        }

        public void setP_id(String p_id) {
            this.p_id = p_id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getSub_title() {
            return sub_title;
        }

        public void setSub_title(String sub_title) {
            this.sub_title = sub_title;
        }

        public String getDescript() {
            return descript;
        }

        public void setDescript(String descript) {
            this.descript = descript;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getUpdated_date() {
            return updated_date;
        }

        public void setUpdated_date(String updated_date) {
            this.updated_date = updated_date;
        }

        public String getCreated_date() {
            return created_date;
        }

        public void setCreated_date(String created_date) {
            this.created_date = created_date;
        }
    }
}

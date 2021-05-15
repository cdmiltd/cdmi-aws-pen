package pw.cdmi.aws.edu.weixin.rs.v1.domain;

import java.util.List;

public class UserOpenIdResponse {

    private long total;

    private long count;

    private OpenIdData data;

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public OpenIdData getData() {
        return data;
    }

    public void setData(OpenIdData data) {
        this.data = data;
    }

    public class OpenIdData {

        private List<String> openid;

        private String next_openid;

        public List<String> getOpenid() {
            return openid;
        }

        public void setOpenid(List<String> openid) {
            this.openid = openid;
        }

        public String getNext_openid() {
            return next_openid;
        }

        public void setNext_openid(String next_openid) {
            this.next_openid = next_openid;
        }

    }
}

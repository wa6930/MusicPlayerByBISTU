package com.example.erjike.bistu.MusicPlayer.Gson;

import java.util.List;

public class InputLike {
    //used address=http://10.3.149.67:3000/search/suggest?keywords=%20%E5%8D%83%E6%9C%AC%E6%A8%B1&type=mobile


    /**
     * result : {"allMatch":[{"keyword":"千本樱小提琴","type":1,"alg":"alg_psug_a","lastKeyword":""},{"keyword":"千本樱纯音乐","type":1,"alg":"alg_psug_a","lastKeyword":""},{"keyword":"千本樱","type":1,"alg":"alg_psug_a","lastKeyword":""},{"keyword":"千本樱钢琴","type":1,"alg":"alg_psug_a","lastKeyword":""},{"keyword":"千本樱唢呐","type":1,"alg":"alg_psug_a","lastKeyword":""},{"keyword":"千本樱伴奏","type":1,"alg":"alg_psug_a","lastKeyword":""}]}
     * code : 200
     */

    private ResultBean result;
    private int code;

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public static class ResultBean {
        private List<AllMatchBean> allMatch;//所有推荐输入项

        public List<AllMatchBean> getAllMatch() {
            return allMatch;
        }

        public void setAllMatch(List<AllMatchBean> allMatch) {
            this.allMatch = allMatch;
        }

        public static class AllMatchBean {
            /**
             * keyword : 千本樱小提琴
             * type : 1
             * alg : alg_psug_a
             * lastKeyword :
             */

            private String keyword;
            private int type;//推荐类型，为1的时候可以点击
            private String alg;
            private String lastKeyword;

            public String getKeyword() {
                return keyword;
            }

            public void setKeyword(String keyword) {
                this.keyword = keyword;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public String getAlg() {
                return alg;
            }

            public void setAlg(String alg) {
                this.alg = alg;
            }

            public String getLastKeyword() {
                return lastKeyword;
            }

            public void setLastKeyword(String lastKeyword) {
                this.lastKeyword = lastKeyword;
            }
        }
    }
}

package com.farinc.survivalstats.api;

public class SurvivalStatsAPI {
    
    public static final SurvivalStatsAPI instance = new SurvivalStatsAPI();

    public static enum TickRate {
        NONE(-1),
        LOW(60),
        MEDIUM(30),
        HIGH(10);

        private int rate;
        private TickRate(int rate){
            this.rate = rate;
        }

        public int getRate(){
            return this.rate;
        }
    }

    


}
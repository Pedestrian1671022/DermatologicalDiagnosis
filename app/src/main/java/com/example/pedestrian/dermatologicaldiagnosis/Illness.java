package com.example.pedestrian.dermatologicaldiagnosis;

import java.io.Serializable;

public class Illness implements Comparable<Illness> , Serializable{
    private String illness;
    private float probability;

    public Illness(String illness, float probability){
        this.illness = illness;
        this.probability = probability;
    }

    public void setIllness(String illness) {
        this.illness = illness;
    }

    public String getIllness() {
        return illness;
    }

    public void setProbability(float probability) {
        this.probability = probability;
    }

    public float getProbability() {
        return probability;
    }

    @Override
    public int compareTo(Illness illness){
        float i = this.getProbability() - illness.getProbability();
        if(i > 0)
            return -1;
        else if(i < 0)
            return 1;
        else
            return 0;
    }
}

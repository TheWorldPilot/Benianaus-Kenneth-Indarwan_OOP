package com.benianaus.frontend;

import java.awt.*;

public class Boss extends Enemy{
    public Boss(String name, int hp){
        super(380, 400, 48, 48, Color.BLUE, name, hp, 5000L);
    }

    public Boss(float x, float y, String name, int hp){
        super(x, y, 48, 48, Color.BLUE, name, hp, 5000L);
    }

    // Tipe inheritancenya adalah tipe inheritance Hierarchical karena Boss dan Fairy merupakan hasil dari satu Superclass yaitu GameObject->Enemy.

}

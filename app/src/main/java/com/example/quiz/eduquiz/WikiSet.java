package com.example.quiz.eduquiz;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by csaper6 on 3/23/17.
 */
public class WikiSet extends ArrayList<Wiki> {
    public WikiSet(Set<String> strings){

    }

    @Override
    public boolean add(Wiki wiki) {
        if(this.contains(wiki))
            return false;
        this.add(0,wiki);
        return true;
    }
}

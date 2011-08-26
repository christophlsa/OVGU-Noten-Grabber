package hisqisnoten;

import java.util.Comparator;

public class HQNContainerComparator implements Comparator<HQNContainer> {

	@Override
	public int compare(HQNContainer t1, HQNContainer t2) {
        int compSem =  t1.getSemester().substring(5).compareTo(t2.getSemester().substring(5));
        
        if (compSem == 0) {
            return t1.getFach().compareTo(t2.getFach());
        }
        
        return compSem;
    }

}
